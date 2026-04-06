import { AfterViewInit, Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import * as L from 'leaflet';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-mapa-selector',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './mapa-selector.html',
  styleUrl: './mapa-selector.css'
})
export class MapaSelectorComponent implements AfterViewInit {

  @ViewChild('mapContainer', { static: true }) mapContainer!: ElementRef<HTMLDivElement>;

  @Input() latitud: number | null = null;
  @Input() longitud: number | null = null;
  @Input() soloVista = false;

  @Output() ubicacionSeleccionada = new EventEmitter<{ latitud: number, longitud: number }>();

  private map!: L.Map;
  private marker: L.Marker | null = null;

  textoBusqueda = '';
  mensajeBusqueda = '';
  mostrarMensajeBusqueda = false;
  private timeoutMensaje: any;

  private readonly valenciaCentro: L.LatLngExpression = [39.4699, -0.3763];
  private readonly zoomValencia = 12;
  private readonly zoomDetalle = 17;

  private readonly limitesComunidadValenciana = L.latLngBounds(
    L.latLng(37.84, -1.58),
    L.latLng(40.79, 0.56)
  );

  constructor(private http: HttpClient) {}

  ngAfterViewInit(): void {
    this.inicializarMapa();
  }

  private inicializarMapa(): void {
    const tieneUbicacion = this.latitud !== null && this.longitud !== null;

    const lat = this.latitud ?? 39.4699;
    const lng = this.longitud ?? -0.3763;
    const zoom = tieneUbicacion ? this.zoomDetalle : this.zoomValencia;

    this.map = L.map(this.mapContainer.nativeElement, {
      center: [lat, lng],
      zoom,
      maxBounds: this.limitesComunidadValenciana,
      maxBoundsViscosity: 1.0,
      zoomControl: true,
      dragging: !this.soloVista,
      scrollWheelZoom: !this.soloVista,
      doubleClickZoom: !this.soloVista,
      boxZoom: !this.soloVista,
      keyboard: !this.soloVista,
      touchZoom: !this.soloVista
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

    if (tieneUbicacion) {
      this.colocarMarker(this.latitud!, this.longitud!);
    }

    if (!this.soloVista) {
      this.map.on('click', (e: L.LeafletMouseEvent) => {
        const nuevaLat = Number(e.latlng.lat.toFixed(6));
        const nuevaLng = Number(e.latlng.lng.toFixed(6));

        this.colocarMarker(nuevaLat, nuevaLng);
        this.ubicacionSeleccionada.emit({
          latitud: nuevaLat,
          longitud: nuevaLng
        });
      });
    }

    setTimeout(() => {
      this.map.invalidateSize();
    }, 200);
  }

  buscarDireccion(): void {
    const texto = this.textoBusqueda.trim();

    if (!texto || this.soloVista) {
      this.mostrarMensaje('Escribe una dirección, calle, zona o nombre de sitio.');
      return;
    }

    const consultas = [
      texto,
      `${texto}, Valencia`,
      `${texto}, València`,
      `${texto}, Comunidad Valenciana`,
      `${texto}, Comunitat Valenciana`,
      `${texto}, Valencia, España`,
      `${texto}, Comunidad Valenciana, España`
    ];

    this.buscarConFallback(consultas, 0);
  }

  private buscarConFallback(consultas: string[], indice: number): void {
    if (indice >= consultas.length) {
      this.mostrarMensaje('No se ha encontrado la ubicación. Prueba con una calle, zona o dirección más concreta.');
      return;
    }

    const consulta = consultas[indice];

    const url =
      `https://nominatim.openstreetmap.org/search` +
      `?format=json` +
      `&limit=5` +
      `&countrycodes=es` +
      `&bounded=1` +
      `&viewbox=-1.58,40.79,0.56,37.84` +
      `&q=${encodeURIComponent(consulta)}`;

    this.http.get<any[]>(url).subscribe({
      next: (resultados) => {
        if (!resultados || resultados.length === 0) {
          this.buscarConFallback(consultas, indice + 1);
          return;
        }

        const resultadoValido = resultados.find((resultado) => {
          const lat = Number(resultado.lat);
          const lng = Number(resultado.lon);
          return this.limitesComunidadValenciana.contains(L.latLng(lat, lng));
        });

        if (!resultadoValido) {
          this.buscarConFallback(consultas, indice + 1);
          return;
        }

        const lat = Number(Number(resultadoValido.lat).toFixed(6));
        const lng = Number(Number(resultadoValido.lon).toFixed(6));

        this.map.setView([lat, lng], 16);
        this.colocarMarker(lat, lng);

        this.ubicacionSeleccionada.emit({
          latitud: lat,
          longitud: lng
        });

        this.ocultarMensaje();
      },
      error: (err) => {
        console.error('ERROR BUSQUEDA DIRECCION:', err);
        this.buscarConFallback(consultas, indice + 1);
      }
    });
  }

  volverAValencia(): void {
    if (this.soloVista) {
      return;
    }

    this.map.setView(this.valenciaCentro, this.zoomValencia);
  }

  volverAMarcador(): void {
    if (this.soloVista || !this.marker) {
      return;
    }

    const posicion = this.marker.getLatLng();
    this.map.setView([posicion.lat, posicion.lng], 16);
  }

  private colocarMarker(lat: number, lng: number): void {
    if (this.marker) {
      this.marker.remove();
    }

    this.marker = L.marker([lat, lng]).addTo(this.map);
  }

  private mostrarMensaje(texto: string): void {
    this.mensajeBusqueda = texto;
    this.mostrarMensajeBusqueda = true;

    if (this.timeoutMensaje) {
      clearTimeout(this.timeoutMensaje);
    }

    this.timeoutMensaje = setTimeout(() => {
      this.mostrarMensajeBusqueda = false;
    }, 3500);
  }

  cerrarMensaje(): void {
    this.mostrarMensajeBusqueda = false;
  }

  private ocultarMensaje(): void {
    this.mostrarMensajeBusqueda = false;
  }
}
