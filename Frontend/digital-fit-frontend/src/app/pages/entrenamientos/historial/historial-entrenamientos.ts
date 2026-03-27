import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../../components/header/header';
import { Footer } from '../../../components/footer/footer';
import { HistorialEntrenamientosService } from '../../../services/entrenamientos/historial-entrenamientos';
import { EntrenamientosService } from '../../../services/entrenamientos/entrenamientos-service';
import { MisEntrenamientosService } from '../../../services/entrenamientos/mis-entrenamientos-service';
import { PrivadosService } from '../../../services/centros/privados-service';
import { MisCentrosService } from '../../../services/centros/mis-centros-service';
import { PublicosService } from '../../../services/publicos/publicos-service';
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
  private privadosService = inject(PrivadosService);
  private misCentrosService = inject(MisCentrosService);
  private publicosService = inject(PublicosService);
  private misLugaresService = inject(MisLugaresService);
  private cdr = inject(ChangeDetectorRef);

  historial: any[] = [];
  historialFiltrado: any[] = [];

  entrenamientosBase: any[] = [];
  misEntrenamientos: any[] = [];
  centrosBase: any[] = [];
  misCentros: any[] = [];
  lugaresBase: any[] = [];
  misLugares: any[] = [];

  entrenamientoSeleccionado = '';
  ubicacionSeleccionada = '';

  fecha = '';
  duracionEnMinutos = 30;
  notas = '';

  busqueda = '';

  mensajeExito = '';
  mensajeError = '';

  ngOnInit(): void {
    this.cargarHistorial();
    this.cargarOpcionesFormulario();
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

  cargarOpcionesFormulario(): void {
    this.entrenamientosService.getEntrenamientos().subscribe({
      next: (data: any[]) => {
        this.entrenamientosBase = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR ENTRENAMIENTOS BASE:', err)
    });

    this.misEntrenamientosService.getMisEntrenamientos().subscribe({
      next: (data: any[]) => {
        this.misEntrenamientos = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR MIS ENTRENAMIENTOS:', err)
    });

    this.privadosService.getCentrosPrivados().subscribe({
      next: (data: any[]) => {
        this.centrosBase = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR CENTROS BASE:', err)
    });

    this.misCentrosService.getMisCentros().subscribe({
      next: (data: any[]) => {
        this.misCentros = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR MIS CENTROS:', err)
    });

    this.publicosService.getLugaresPublicos().subscribe({
      next: (data: any[]) => {
        this.lugaresBase = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR LUGARES BASE:', err)
    });

    this.misLugaresService.getMisLugares().subscribe({
      next: (data: any[]) => {
        this.misLugares = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR MIS LUGARES:', err)
    });
  }

  crearRegistro(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    if (!this.entrenamientoSeleccionado) {
      this.mensajeError = 'Debes seleccionar un entrenamiento.';
      return;
    }

    const payload: any = {
      entrenamientoBaseId: null,
      entrenamientoUsuarioId: null,
      lugarPublicoBaseId: null,
      lugarPublicoUsuarioId: null,
      centroPrivadoBaseId: null,
      centroPrivadoUsuarioId: null,
      fecha: this.formatearFechaParaBackend(this.fecha),
      duracionEnMinutos: this.duracionEnMinutos,
      notas: this.notas
    };

    const [tipoEntrenamiento, idEntrenamiento] = this.entrenamientoSeleccionado.split('-');

    if (tipoEntrenamiento === 'BASE') {
      payload.entrenamientoBaseId = Number(idEntrenamiento);
    } else if (tipoEntrenamiento === 'MIS') {
      payload.entrenamientoUsuarioId = Number(idEntrenamiento);
    }

    if (this.ubicacionSeleccionada) {
      const [tipoUbicacion, idUbicacion] = this.ubicacionSeleccionada.split('-');

      if (tipoUbicacion === 'LPB') {
        payload.lugarPublicoBaseId = Number(idUbicacion);
      } else if (tipoUbicacion === 'LPU') {
        payload.lugarPublicoUsuarioId = Number(idUbicacion);
      } else if (tipoUbicacion === 'CPB') {
        payload.centroPrivadoBaseId = Number(idUbicacion);
      } else if (tipoUbicacion === 'CPU') {
        payload.centroPrivadoUsuarioId = Number(idUbicacion);
      }
    }

    this.historialService.crearRegistro(payload).subscribe({
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
      (registro.ubicacion || '').toLowerCase().includes(texto) ||
      (registro.tipoEntrenamiento || '').toLowerCase().includes(texto) ||
      (registro.tipoUbicacion || '').toLowerCase().includes(texto) ||
      (registro.notas || '').toLowerCase().includes(texto) ||
      (registro.fecha || '').toLowerCase().includes(texto)
    );
  }

  limpiarFormulario(): void {
    this.entrenamientoSeleccionado = '';
    this.ubicacionSeleccionada = '';
    this.fecha = '';
    this.duracionEnMinutos = 30;
    this.notas = '';
  }

  formatearFechaParaBackend(fechaLocal: string): string {
    if (!fechaLocal) {
      return '';
    }

    return `${fechaLocal.replace('T', ' ')}:00`;
  }

  mostrarTipoEntrenamiento(tipo: string): string {
    switch (tipo) {
      case 'ENTRENAMIENTO_BASE':
        return 'Entrenamiento base';
      case 'MI_ENTRENAMIENTO':
        return 'Mi entrenamiento';
      default:
        return 'No indicado';
    }
  }

  mostrarTipoUbicacion(tipo: string): string {
    switch (tipo) {
      case 'LUGAR_PUBLICO_BASE':
        return 'Lugar público base';
      case 'MI_LUGAR_PUBLICO':
        return 'Mi lugar público';
      case 'CENTRO_PRIVADO_BASE':
        return 'Centro privado base';
      case 'MI_CENTRO_PRIVADO':
        return 'Mi centro privado';
      default:
        return 'Sin ubicación';
    }
  }
}