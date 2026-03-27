import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../../components/header/header';
import { Footer } from '../../../components/footer/footer';
import { HistorialEntrenamientosService } from '../../../services/entrenamientos/historial-entrenamientos';
import { EntrenamientosService } from '../../../services/entrenamientos/entrenamientos-service';
import { MisEntrenamientosService } from '../../../services/entrenamientos/mis-entrenamientos-service';
import { MisCentrosService } from '../../../services/centros/mis-centros-service';
import { MisLugaresService } from '../../../services/publicos/mis-lugares';

@Component({
  selector: 'app-historial-entrenamientos',
  imports: [HeaderComponent, Footer, FormsModule, RouterLink],
  templateUrl: './historial-entrenamientos.html',
  styleUrl: './historial-entrenamientos.css'
})
export class HistorialEntrenamientosComponent {

  private historialService = inject(HistorialEntrenamientosService);
  private entrenamientosService = inject(EntrenamientosService);
  private misEntrenamientosService = inject(MisEntrenamientosService);
  private misCentrosService = inject(MisCentrosService);
  private misLugaresService = inject(MisLugaresService);
  private cdr = inject(ChangeDetectorRef);

  historial: any[] = [];
  historialFiltrado: any[] = [];

  entrenamientosBase: any[] = [];
  misEntrenamientos: any[] = [];
  misCentros: any[] = [];
  misLugares: any[] = [];

  tipoEntrenamientoSeleccionado = 'BASE';
  entrenamientoBaseId: number | null = null;
  entrenamientoUsuarioId: number | null = null;
  lugarPublicoId: number | null = null;
  centroPrivadoId: number | null = null;
  fecha = '';
  duracionEnMinutos = 30;
  notas = '';

  busqueda = '';

  mensajeExito = '';
  mensajeError = '';

  ngOnInit(): void {
    this.cargarHistorial();
    this.cargarDatosFormulario();
  }

  cargarHistorial(): void {
    this.historialService.getHistorial().subscribe({
      next: (data: any[]) => {
        this.historial = data;
        this.aplicarFiltroBusqueda();
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR HISTORIAL:', err);
        this.mensajeError = 'No se pudo cargar el historial.';
      }
    });
  }

  cargarDatosFormulario(): void {
    this.entrenamientosService.getEntrenamientos().subscribe({
      next: (data: any[]) => {
        this.entrenamientosBase = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR ENTRENAMIENTOS BASE:', err);
      }
    });

    this.misEntrenamientosService.getMisEntrenamientos().subscribe({
      next: (data: any[]) => {
        this.misEntrenamientos = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR MIS ENTRENAMIENTOS:', err);
      }
    });

    this.misCentrosService.getMisCentros().subscribe({
      next: (data: any[]) => {
        this.misCentros = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR MIS CENTROS:', err);
      }
    });

    this.misLugaresService.getMisLugares().subscribe({
      next: (data: any[]) => {
        this.misLugares = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR MIS LUGARES:', err);
      }
    });
  }

  cambiarTipoEntrenamiento(): void {
    this.entrenamientoBaseId = null;
    this.entrenamientoUsuarioId = null;
  }

  crearRegistro(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    if (this.tipoEntrenamientoSeleccionado === 'BASE' && !this.entrenamientoBaseId) {
      this.mensajeError = 'Debes seleccionar un entrenamiento base.';
      return;
    }

    if (this.tipoEntrenamientoSeleccionado === 'MIS' && !this.entrenamientoUsuarioId) {
      this.mensajeError = 'Debes seleccionar uno de tus entrenamientos.';
      return;
    }

    const nuevoRegistro = {
      entrenamientoBaseId: this.tipoEntrenamientoSeleccionado === 'BASE' ? this.entrenamientoBaseId : null,
      entrenamientoUsuarioId: this.tipoEntrenamientoSeleccionado === 'MIS' ? this.entrenamientoUsuarioId : null,
      lugarPublicoId: this.lugarPublicoId,
      centroPrivadoId: this.centroPrivadoId,
      fecha: this.formatearFechaParaBackend(this.fecha),
      duracionEnMinutos: this.duracionEnMinutos,
      notas: this.notas
    };

    this.historialService.crearRegistro(nuevoRegistro).subscribe({
      next: () => {
        this.mensajeExito = 'Registro guardado correctamente.';
        this.limpiarFormulario();
        this.cargarHistorial();
      },
      error: () => {
        this.mensajeError = 'No se pudo guardar el registro.';
      }
    });
  }

  borrarRegistro(id: number): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    this.historialService.borrarRegistro(id).subscribe({
      next: () => {
        this.mensajeExito = 'Registro borrado correctamente.';
        this.cargarHistorial();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el registro.';
      }
    });
  }

  aplicarFiltroBusqueda(): void {
    const texto = this.busqueda.trim().toLowerCase();

    if (!texto) {
      this.historialFiltrado = [...this.historial];
      return;
    }

    this.historialFiltrado = this.historial.filter((registro: any) =>
      (registro.entrenamiento || '').toLowerCase().includes(texto) ||
      (registro.lugar || '').toLowerCase().includes(texto) ||
      (registro.notas || '').toLowerCase().includes(texto) ||
      (registro.fecha || '').toLowerCase().includes(texto)
    );
  }

  limpiarFormulario(): void {
    this.tipoEntrenamientoSeleccionado = 'BASE';
    this.entrenamientoBaseId = null;
    this.entrenamientoUsuarioId = null;
    this.lugarPublicoId = null;
    this.centroPrivadoId = null;
    this.fecha = '';
    this.duracionEnMinutos = 30;
    this.notas = '';
  }

  formatearFechaParaBackend(fechaLocal: string): string {
    if (!fechaLocal) {
      return '';
    }

    return fechaLocal.replace('T', ':00').length === 16
      ? `${fechaLocal.replace('T', ' ')}:00`
      : fechaLocal.replace('T', ' ');
  }
}