import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  Output,
  ViewChild
} from '@angular/core';
import * as L from 'leaflet';
import 'leaflet-control-geocoder';

@Component({
  selector: 'app-mapa-selector',
  standalone: true,
  templateUrl: './mapa-selector.html',
  styleUrl: './mapa-selector.css'
})
export class MapaSelectorComponent implements AfterViewInit {

  @ViewChild('mapContainer', { static: true }) mapContainer!: ElementRef<HTMLDivElement>;

  @Input() latitud: number | null = null;
  @Input() longitud: number | null = null;
  @Input() soloVista = false;

  @Input() tipoSeleccion: 'centro' | 'lugar' = 'lugar';
  @Input() textoAyuda = '';

  @Output() ubicacionSeleccionada = new EventEmitter<{ latitud: number, longitud: number }>();

  private map!: L.Map;
  private marker: L.Marker | null = null;

  // Centro inicial: Valencia
  private readonly latitudValencia = 39.4699;
  private readonly longitudValencia = -0.3763;

  ngAfterViewInit(): void {
    this.inicializarMapa();
  }

  private inicializarMapa(): void {
    const lat = this.latitud ?? this.latitudValencia;
    const lng = this.longitud ?? this.longitudValencia;
    const zoom = (this.latitud !== null && this.longitud !== null) ? 16 : 13;

    this.map = L.map(this.mapContainer.nativeElement).setView([lat, lng], zoom);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

    this.configurarBuscador();

    if (this.latitud !== null && this.longitud !== null) {
      this.colocarMarker(this.latitud, this.longitud);
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

    this.crearBotonVolverValencia();
    this.crearBotonVolverMarcador();

    setTimeout(() => {
      this.map.invalidateSize();
    }, 200);
  }

  private configurarBuscador(): void {
    const geocoderConstructor = (L.Control as any).Geocoder;

    if (!geocoderConstructor) {
      return;
    }

    const geocoder = geocoderConstructor.nominatim();

    const control = geocoderConstructor.geocoder({
      defaultMarkGeocode: false,
      placeholder: this.tipoSeleccion === 'centro'
        ? 'Buscar dirección de centro en Valencia...'
        : 'Buscar dirección de lugar en Valencia...',
      errorMessage: 'Dirección no encontrada',
      geocoder
    }).addTo(this.map);

    control.on('markgeocode', (e: any) => {
      const center = e.geocode.center;
      const nuevaLat = Number(center.lat.toFixed(6));
      const nuevaLng = Number(center.lng.toFixed(6));

      this.map.setView([nuevaLat, nuevaLng], 16);
      this.colocarMarker(nuevaLat, nuevaLng);

      if (!this.soloVista) {
        this.ubicacionSeleccionada.emit({
          latitud: nuevaLat,
          longitud: nuevaLng
        });
      }
    });
  }

  private crearBotonVolverValencia(): void {
    const BotonValencia = L.Control.extend({
      options: { position: 'topleft' },

      onAdd: () => {
        const container = L.DomUtil.create('div', 'leaflet-bar leaflet-control');
        const button = L.DomUtil.create('a', 'boton-mapa-control', container);

        button.innerHTML = 'VLC';
        button.href = '#';
        button.title = 'Volver a Valencia';

        L.DomEvent.disableClickPropagation(container);

        L.DomEvent.on(button, 'click', L.DomEvent.stop);
        L.DomEvent.on(button, 'click', () => {
          this.map.setView([this.latitudValencia, this.longitudValencia], 13);
        });

        return container;
      }
    });

    this.map.addControl(new BotonValencia());
  }

  private crearBotonVolverMarcador(): void {
    const BotonMarcador = L.Control.extend({
      options: { position: 'topleft' },

      onAdd: () => {
        const container = L.DomUtil.create('div', 'leaflet-bar leaflet-control');
        const button = L.DomUtil.create('a', 'boton-mapa-control', container);

        button.innerHTML = '📍';
        button.href = '#';
        button.title = 'Volver a la ubicación marcada';

        L.DomEvent.disableClickPropagation(container);

        L.DomEvent.on(button, 'click', L.DomEvent.stop);
        L.DomEvent.on(button, 'click', () => {
          const lat = this.marker ? this.marker.getLatLng().lat : this.latitud;
          const lng = this.marker ? this.marker.getLatLng().lng : this.longitud;

          if (lat != null && lng != null) {
            this.map.setView([lat, lng], 16);
          }
        });

        return container;
      }
    });

    this.map.addControl(new BotonMarcador());
  }

  private colocarMarker(lat: number, lng: number): void {
    if (this.marker) {
      this.marker.remove();
    }

    this.marker = L.marker([lat, lng]).addTo(this.map);
  }
}
