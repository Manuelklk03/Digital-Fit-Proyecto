import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  Output,
  ViewChild,
  OnChanges,
  SimpleChanges,
  OnDestroy
} from '@angular/core';
import * as L from 'leaflet';

@Component({
  selector: 'app-mapa-selector',
  standalone: true,
  templateUrl: './mapa-selector.html',
  styleUrl: './mapa-selector.css'
})
export class MapaSelectorComponent implements AfterViewInit, OnChanges, OnDestroy {

  @ViewChild('mapContainer', { static: true }) mapContainer!: ElementRef<HTMLDivElement>;

  @Input() latitud: number | null = null;
  @Input() longitud: number | null = null;
  @Input() soloVista = false;

  // libre = cualquiera
  // centro = para centros privados
  // lugar = para lugares públicos
  @Input() tipoSeleccion: 'libre' | 'centro' | 'lugar' = 'libre';

  @Input() textoAyuda = 'Pulsa sobre el mapa para seleccionar una ubicación.';

  @Output() ubicacionSeleccionada = new EventEmitter<{ latitud: number, longitud: number }>();

  private map!: L.Map;
  private marker: L.CircleMarker | null = null;
  private mapaInicializado = false;

  ngAfterViewInit(): void {
    this.inicializarMapa();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!this.mapaInicializado) {
      return;
    }

    if (changes['latitud'] || changes['longitud']) {
      this.actualizarVistaDesdeInputs();
    }

    if (changes['soloVista']) {
      this.actualizarModoInteraccion();
    }
  }

  ngOnDestroy(): void {
    if (this.map) {
      this.map.remove();
    }
  }

  private inicializarMapa(): void {
    const lat = this.latitud ?? 36.834;
    const lng = this.longitud ?? -2.463;
    const zoom = (this.latitud !== null && this.longitud !== null) ? 15 : 13;

    this.map = L.map(this.mapContainer.nativeElement, {
      center: [lat, lng],
      zoom: zoom
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors',
      maxZoom: 19
    }).addTo(this.map);

    if (this.latitud !== null && this.longitud !== null) {
      this.colocarMarker(this.latitud, this.longitud);
    }

    this.actualizarModoInteraccion();

    this.map.on('click', (e: L.LeafletMouseEvent) => {
      if (this.soloVista) {
        return;
      }

      const nuevaLat = Number(e.latlng.lat.toFixed(6));
      const nuevaLng = Number(e.latlng.lng.toFixed(6));

      this.colocarMarker(nuevaLat, nuevaLng);

      this.ubicacionSeleccionada.emit({
        latitud: nuevaLat,
        longitud: nuevaLng
      });
    });

    this.mapaInicializado = true;

    setTimeout(() => {
      this.map.invalidateSize();
    }, 200);
  }

  private actualizarVistaDesdeInputs(): void {
    if (this.latitud == null || this.longitud == null) {
      return;
    }

    this.colocarMarker(this.latitud, this.longitud);
    this.map.setView([this.latitud, this.longitud], 15);

    setTimeout(() => {
      this.map.invalidateSize();
    }, 100);
  }

  private actualizarModoInteraccion(): void {
    if (!this.map) {
      return;
    }

    if (this.soloVista) {
      this.map.dragging.enable();
      this.map.touchZoom.enable();
      this.map.doubleClickZoom.enable();
      this.map.scrollWheelZoom.enable();
      this.map.boxZoom.enable();
      this.map.keyboard.enable();
    }
  }

  private colocarMarker(lat: number, lng: number): void {
    if (this.marker) {
      this.marker.remove();
    }

    this.marker = L.circleMarker([lat, lng], {
      radius: 10,
      weight: 3,
      color: '#1a4f87',
      fillColor: '#3C91E6',
      fillOpacity: 0.9
    }).addTo(this.map);

    const textoPopup = this.obtenerTextoPopup();
    this.marker.bindPopup(`${textoPopup}<br>${lat}, ${lng}`);
  }

  private obtenerTextoPopup(): string {
    if (this.tipoSeleccion === 'centro') {
      return 'Ubicación seleccionada para centro privado';
    }

    if (this.tipoSeleccion === 'lugar') {
      return 'Ubicación seleccionada para lugar público';
    }

    return 'Ubicación seleccionada';
  }
}
