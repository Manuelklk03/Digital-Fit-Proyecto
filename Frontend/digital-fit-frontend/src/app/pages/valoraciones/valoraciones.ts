import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { ValoracionesService } from '../../services/valoraciones/valoraciones-service';
import { PrivadosService } from '../../services/centros/privados-service';
import { PublicosService } from '../../services/publicos/publicos-service';
import { EntrenamientosService } from '../../services/entrenamientos/entrenamientos-service';
import { EntrenamientosComunidadService } from '../../services/entrenamientos/entrenamiento-comunidad-service';

@Component({
  selector: 'app-valoraciones',
  imports: [HeaderComponent, Footer, FormsModule],
  templateUrl: './valoraciones.html',
  styleUrl: './valoraciones.css'
})
export class ValoracionesComponent {

  private valoracionesService = inject(ValoracionesService);
  private privadosService = inject(PrivadosService);
  private publicosService = inject(PublicosService);
  private entrenamientosService = inject(EntrenamientosService);
  private entrenamientosComunidadService = inject(EntrenamientosComunidadService);
  private cdr = inject(ChangeDetectorRef);

  misValoraciones: any[] = [];
  valoracionesFiltradas: any[] = [];

  centrosPrivados: any[] = [];
  lugaresPublicos: any[] = [];
  entrenamientosBase: any[] = [];
  entrenamientosComunidad: any[] = [];

  tipoContenido = 'CENTRO_PRIVADO';
  contenidoSeleccionadoId: number | null = null;
  puntuacion = 5;
  comentario = '';

  busqueda = '';

  mensajeExito = '';
  mensajeError = '';

  ngOnInit(): void {
    this.cargarMisValoraciones();
    this.cargarOpciones();
  }

  cargarMisValoraciones(): void {
    this.valoracionesService.getMisValoraciones().subscribe({
      next: (data: any[]) => {
        this.misValoraciones = data;
        this.aplicarBusqueda();
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR MIS VALORACIONES:', err);
        this.mensajeError = 'No se pudieron cargar tus valoraciones.';
      }
    });
  }

  cargarOpciones(): void {
    this.privadosService.getCentrosPrivados().subscribe({
      next: (data: any[]) => {
        this.centrosPrivados = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR CENTROS PRIVADOS:', err)
    });

    this.publicosService.getLugaresPublicos().subscribe({
      next: (data: any[]) => {
        this.lugaresPublicos = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR LUGARES PUBLICOS:', err)
    });

    this.entrenamientosService.getEntrenamientos().subscribe({
      next: (data: any[]) => {
        this.entrenamientosBase = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR ENTRENAMIENTOS BASE:', err)
    });

    this.entrenamientosComunidadService.getEntrenamientosComunidad().subscribe({
      next: (data: any[]) => {
        this.entrenamientosComunidad = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR ENTRENAMIENTOS COMUNIDAD:', err)
    });
  }

  cambiarTipoContenido(): void {
    this.contenidoSeleccionadoId = null;
  }

  guardarValoracion(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    if (!this.contenidoSeleccionadoId) {
      this.mensajeError = 'Debes seleccionar un contenido.';
      return;
    }

    const nuevaValoracion = {
      puntuacion: this.puntuacion,
      comentario: this.comentario
    };

    this.valoracionesService
      .crearOActualizarValoracion(this.tipoContenido, this.contenidoSeleccionadoId, nuevaValoracion)
      .subscribe({
        next: () => {
          this.mensajeExito = 'Valoración guardada correctamente.';
          this.puntuacion = 5;
          this.comentario = '';
          this.contenidoSeleccionadoId = null;
          this.cargarMisValoraciones();
        },
        error: () => {
          this.mensajeError = 'No se pudo guardar la valoración.';
        }
      });
  }

  borrarValoracion(tipo: string, contenidoId: number): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    this.valoracionesService.borrarValoracion(tipo, contenidoId).subscribe({
      next: () => {
        this.mensajeExito = 'Valoración borrada correctamente.';
        this.cargarMisValoraciones();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar la valoración.';
      }
    });
  }

  aplicarBusqueda(): void {
    const texto = this.busqueda.trim().toLowerCase();

    if (!texto) {
      this.valoracionesFiltradas = [...this.misValoraciones];
      return;
    }

    this.valoracionesFiltradas = this.misValoraciones.filter((valoracion: any) =>
      this.mostrarTipoContenido(valoracion.tipoDeValoracion).toLowerCase().includes(texto) ||
      String(valoracion.puntuacion).includes(texto) ||
      (valoracion.comentario || '').toLowerCase().includes(texto) ||
      (valoracion.fecha || '').toLowerCase().includes(texto)
    );
  }

  getOpcionesContenidoActual(): any[] {
    switch (this.tipoContenido) {
      case 'CENTRO_PRIVADO':
        return this.centrosPrivados;
      case 'LUGAR_PUBLICO':
        return this.lugaresPublicos;
      case 'ENTRENAMIENTO_BASE':
        return this.entrenamientosBase;
      case 'ENTRENAMIENTO_COMUNIDAD':
        return this.entrenamientosComunidad;
      default:
        return [];
    }
  }

  getTextoOpcionContenido(item: any): string {
    switch (this.tipoContenido) {
      case 'CENTRO_PRIVADO':
        return `${item.nombre} - ${item.direccion}`;
      case 'LUGAR_PUBLICO':
        return `${item.nombre} - ${item.tipo}`;
      case 'ENTRENAMIENTO_BASE':
        return `${item.nombre} - ${item.nivel} - ${item.duracionEnMinutos} min`;
      case 'ENTRENAMIENTO_COMUNIDAD':
        return `${item.nombre} - ${item.usuario} - ${item.duracionEnMinutos} min`;
      default:
        return item.nombre || 'Contenido';
    }
  }

  mostrarTipoContenido(tipo: string): string {
    switch (tipo) {
      case 'CENTRO_PRIVADO':
        return 'Centro privado';
      case 'LUGAR_PUBLICO':
        return 'Lugar público';
      case 'ENTRENAMIENTO_BASE':
        return 'Entrenamiento base';
      case 'ENTRENAMIENTO_COMUNIDAD':
        return 'Entrenamiento comunidad';
      default:
        return tipo;
    }
  }
}
