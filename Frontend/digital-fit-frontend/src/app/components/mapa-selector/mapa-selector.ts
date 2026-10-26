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

  private readonly valenciaCentro: L.LatLngExpression = [39.4699, -0.3763];
  private readonly zoomValencia = 12;
  private readonly zoomDetalle = 17;

  private readonly limitesComunidadValenciana = L.latLngBounds(
    L.latLng(37.84, -1.58),
    L.latLng(40.79, 0.85)
  );

  constructor(private http: HttpClient) {}

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.inicializarMapa();
    }, 100);
  }

  private inicializarMapa(): void {
    const tieneUbicacion = this.latitud !== null && this.longitud !== null;

    const centroInicial: L.LatLngExpression = tieneUbicacion
      ? [this.latitud!, this.longitud!]
      : this.valenciaCentro;

    const zoomInicial = tieneUbicacion ? this.zoomDetalle : this.zoomValencia;

    this.map = L.map(this.mapContainer.nativeElement, {
      center: centroInicial,
      zoom: zoomInicial,
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

        if (!this.limitesComunidadValenciana.contains(L.latLng(nuevaLat, nuevaLng))) {
          return;
        }

        this.colocarMarker(nuevaLat, nuevaLng);
        this.ubicacionSeleccionada.emit({
          latitud: nuevaLat,
          longitud: nuevaLng
        });
      });
    }

    setTimeout(() => {
      this.map.invalidateSize();
      this.map.setView(centroInicial, zoomInicial);
    }, 300);
  }

  buscarDireccion(): void {
    const texto = this.textoBusqueda.trim();

    if (!texto || this.soloVista) {
      return;
    }

    const url = `https://nominatim.openstreetmap.org/search?format=json&limit=5&countrycodes=es&q=${encodeURIComponent(texto + ', Comunidad Valenciana, España')}`;

    this.http.get<any[]>(url).subscribe({
      next: (resultados) => {
        if (!resultados || resultados.length === 0) {
          return;
        }

        const resultado = resultados[0];
        const lat = Number(Number(resultado.lat).toFixed(6));
        const lng = Number(Number(resultado.lon).toFixed(6));

        if (!this.limitesComunidadValenciana.contains(L.latLng(lat, lng))) {
          return;
        }

        this.map.setView([lat, lng], 16);
        this.colocarMarker(lat, lng);
        this.ubicacionSeleccionada.emit({
          latitud: lat,
          longitud: lng
        });
      },
      error: (err) => {
        console.error('ERROR BUSQUEDA DIRECCION:', err);
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
}
