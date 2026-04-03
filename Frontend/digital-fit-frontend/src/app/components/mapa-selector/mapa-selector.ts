import { AfterViewInit, Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import * as L from 'leaflet';

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

  private readonly latitudValencia = 39.4699;
  private readonly longitudValencia = -0.3763;

  private readonly limitesComunidadValenciana = L.latLngBounds(
    L.latLng(37.80, -1.95),
    L.latLng(40.92, 0.95)
  );

  private ubicacionMarcadaLat: number | null = null;
  private ubicacionMarcadaLng: number | null = null;

  ngAfterViewInit(): void {
    this.inicializarMapa();
  }

  private inicializarMapa(): void {
    const latInicial = this.latitud ?? this.latitudValencia;
    const lngInicial = this.longitud ?? this.longitudValencia;

    let zoomInicial = 10;

    if (this.latitud !== null && this.longitud !== null) {
      zoomInicial = this.soloVista ? 17 : 15;
    }

    this.map = L.map(this.mapContainer.nativeElement, {
      maxBounds: this.limitesComunidadValenciana,
      maxBoundsViscosity: 1.0
    }).setView([latInicial, lngInicial], zoomInicial);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors',
      minZoom: 8,
      maxZoom: 19
    }).addTo(this.map);

    if (this.latitud !== null && this.longitud !== null) {
      this.colocarMarker(this.latitud, this.longitud);
      this.ubicacionMarcadaLat = this.latitud;
      this.ubicacionMarcadaLng = this.longitud;
    }

    if (!this.soloVista) {
      this.map.on('click', (e: L.LeafletMouseEvent) => {
        const nuevaLat = Number(e.latlng.lat.toFixed(6));
        const nuevaLng = Number(e.latlng.lng.toFixed(6));

        if (!this.limitesComunidadValenciana.contains(L.latLng(nuevaLat, nuevaLng))) {
          return;
        }

        this.colocarMarker(nuevaLat, nuevaLng);
        this.ubicacionMarcadaLat = nuevaLat;
        this.ubicacionMarcadaLng = nuevaLng;

        this.ubicacionSeleccionada.emit({
          latitud: nuevaLat,
          longitud: nuevaLng
        });
      });
    }

    setTimeout(() => {
      this.map.invalidateSize();
    }, 250);
  }

  buscarDireccion(): void {
    const texto = this.textoBusqueda.trim();

    if (!texto) {
      return;
    }

    const consulta = encodeURIComponent(`${texto}, Comunidad Valenciana, España`);

    fetch(`https://nominatim.openstreetmap.org/search?format=json&limit=1&countrycodes=es&q=${consulta}`)
      .then(response => response.json())
      .then(data => {
        if (!data || data.length === 0) {
          return;
        }

        const resultado = data[0];
        const lat = Number(Number(resultado.lat).toFixed(6));
        const lng = Number(Number(resultado.lon).toFixed(6));

        if (!this.limitesComunidadValenciana.contains(L.latLng(lat, lng))) {
          return;
        }

        this.map.setView([lat, lng], this.soloVista ? 17 : 16);
        this.colocarMarker(lat, lng);

        this.ubicacionMarcadaLat = lat;
        this.ubicacionMarcadaLng = lng;

        if (!this.soloVista) {
          this.ubicacionSeleccionada.emit({
            latitud: lat,
            longitud: lng
          });
        }
      })
      .catch(error => {
        console.error('ERROR BUSQUEDA DIRECCION MAPA:', error);
      });
  }

  volverAValencia(): void {
    this.map.setView([this.latitudValencia, this.longitudValencia], 10);
  }

  volverAUbicacionMarcada(): void {
    if (this.ubicacionMarcadaLat == null || this.ubicacionMarcadaLng == null) {
      return;
    }

    this.map.setView(
      [this.ubicacionMarcadaLat, this.ubicacionMarcadaLng],
      this.soloVista ? 17 : 16
    );
  }

  private colocarMarker(lat: number, lng: number): void {
    if (this.marker) {
      this.marker.remove();
    }

    this.marker = L.marker([lat, lng]).addTo(this.map);
  }
}
