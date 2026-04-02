import { AfterViewInit, Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import * as L from 'leaflet';

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

  @Output() ubicacionSeleccionada = new EventEmitter<{ latitud: number, longitud: number }>();

  private map!: L.Map;
  private marker: L.Marker | null = null;

  ngAfterViewInit(): void {
    this.inicializarMapa();
  }

  private inicializarMapa(): void {
    const lat = this.latitud ?? 36.834;
    const lng = this.longitud ?? -2.463;
    const zoom = (this.latitud !== null && this.longitud !== null) ? 15 : 13;

    this.map = L.map(this.mapContainer.nativeElement).setView([lat, lng], zoom);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

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

    setTimeout(() => {
      this.map.invalidateSize();
    }, 200);
  }

  private colocarMarker(lat: number, lng: number): void {
    if (this.marker) {
      this.marker.remove();
    }

    this.marker = L.marker([lat, lng]).addTo(this.map);
  }
}
