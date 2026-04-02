import { MisCentrosService } from './../../../services/centros/mis-centros-service';
import { Footer } from './../../../components/footer/footer';
import { HeaderComponent } from './../../../components/header/header';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MapaSelectorComponent } from '../../../components/mapa-selector/mapa-selector';

@Component({
  selector: 'app-mis-centros',
  imports: [HeaderComponent, Footer, FormsModule, RouterLink, MapaSelectorComponent],
  templateUrl: './mis-centros.html',
  styleUrl: './mis-centros.css'
})
export class MisCentrosComponent {

  private misCentrosService = inject(MisCentrosService);
  private cdr = inject(ChangeDetectorRef);

  centros: any[] = [];

  nombre = '';
  direccion = '';
  telefono = '';
  horario = '';
  precioMensual = 30;
  descripcion = '';
  latitud: number | null = null;
  longitud: number | null = null;

  tipoBusqueda = 'nombre';
  valorBusqueda = '';

  mostrarPopup = false;
  textoPopup = '';
  tipoPopup: 'exito' | 'error' = 'exito';
  private popupTimeout: any;

  ngOnInit(): void {
    this.cargarMisCentros();
  }

  cargarMisCentros(): void {
    this.misCentrosService.getMisCentros().subscribe({
      next: (data: any[]) => {
        this.centros = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR MIS CENTROS:', err);
        this.abrirPopup('No se pudieron cargar tus centros.', 'error');
      }
    });
  }

  buscarCentros(): void {
    const texto = this.valorBusqueda.trim();

    if (!texto) {
      this.cargarMisCentros();
      return;
    }

    if (this.tipoBusqueda === 'direccion') {
      this.misCentrosService.getMisCentrosFiltrados(undefined, texto, undefined).subscribe({
        next: (data: any[]) => {
          this.centros = data;
          this.cdr.detectChanges();
        },
        error: (err: any) => console.error('ERROR FILTRO DIRECCION MIS CENTROS:', err)
      });
      return;
    }

    if (this.tipoBusqueda === 'precioMensual') {
      const precio = Number(texto);

      if (isNaN(precio)) {
        this.centros = [];
        return;
      }

      this.misCentrosService.getMisCentrosFiltrados(undefined, undefined, precio).subscribe({
        next: (data: any[]) => {
          this.centros = data;
          this.cdr.detectChanges();
        },
        error: (err: any) => console.error('ERROR FILTRO PRECIO MIS CENTROS:', err)
      });
      return;
    }

    this.misCentrosService.getMisCentrosFiltrados(texto, undefined, undefined).subscribe({
      next: (data: any[]) => {
        this.centros = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR FILTRO NOMBRE MIS CENTROS:', err)
    });
  }

  alCambiarBusqueda(): void {
    this.buscarCentros();
  }

  limpiarBusqueda(): void {
    this.tipoBusqueda = 'nombre';
    this.valorBusqueda = '';
    this.cargarMisCentros();
  }

  actualizarUbicacionMapa(evento: { latitud: number; longitud: number }): void {
    this.latitud = evento.latitud;
    this.longitud = evento.longitud;
  }

  crearCentroDesdeMapa(): void {
    if (!this.nombre.trim() || !this.direccion.trim()) {
      this.abrirPopup('Debes completar al menos nombre y dirección.', 'error');
      return;
    }

    if (this.latitud == null || this.longitud == null) {
      this.abrirPopup('Debes seleccionar la ubicación en el mapa.', 'error');
      return;
    }

    const payload = {
      nombre: this.nombre,
      direccion: this.direccion,
      telefono: this.telefono,
      horario: this.horario,
      precioMensual: this.precioMensual,
      descripcion: this.descripcion,
      latitud: this.latitud,
      longitud: this.longitud
    };

    this.misCentrosService.crearCentroDesdeMaps(payload).subscribe({
      next: () => {
        this.abrirPopup('Centro guardado correctamente desde el mapa.', 'exito');
        this.limpiarFormulario();
        this.cargarMisCentros();
      },
      error: () => {
        this.abrirPopup('No se pudo guardar el centro desde el mapa.', 'error');
      }
    });
  }

  borrarCentro(id: number): void {
    this.misCentrosService.borrarCentro(id).subscribe({
      next: () => {
        this.abrirPopup('Centro borrado correctamente.', 'exito');
        this.cargarMisCentros();
      },
      error: () => {
        this.abrirPopup('No se pudo borrar el centro.', 'error');
      }
    });
  }

  limpiarFormulario(): void {
    this.nombre = '';
    this.direccion = '';
    this.telefono = '';
    this.horario = '';
    this.precioMensual = 30;
    this.descripcion = '';
    this.latitud = null;
    this.longitud = null;
  }

  formatearUbicacion(latitud: number, longitud: number): string {
    if (latitud == null || longitud == null) {
      return 'Sin ubicación';
    }

    return `${latitud}, ${longitud}`;
  }

  abrirPopup(texto: string, tipo: 'exito' | 'error'): void {
    this.textoPopup = texto;
    this.tipoPopup = tipo;
    this.mostrarPopup = true;
    this.cdr.detectChanges();

    if (this.popupTimeout) {
      clearTimeout(this.popupTimeout);
    }

    this.popupTimeout = setTimeout(() => {
      this.mostrarPopup = false;
      this.cdr.detectChanges();
    }, 3000);
  }

  cerrarPopup(): void {
    this.mostrarPopup = false;
    this.cdr.detectChanges();
  }
}
