import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { ValoracionesService } from '../../services/valoraciones/valoraciones-service';
import { PrivadosService } from '../../services/centros/privados-service';
import { PublicosService } from '../../services/publicos/publicos-service';
import { EntrenamientosService } from '../../services/entrenamientos/entrenamientos-service';
import { EntrenamientosComunidadService } from '../../services/entrenamientos/entrenamiento-comunidad-service';
import { MisEntrenamientosService } from '../../services/entrenamientos/mis-entrenamientos-service';
import { MisCentrosService } from '../../services/centros/mis-centros-service';
import { MisLugaresService } from '../../services/publicos/mis-lugares';
import { HistorialEntrenamientosService } from '../../services/entrenamientos/historial-entrenamientos';

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
  private misEntrenamientosService = inject(MisEntrenamientosService);
  private misCentrosService = inject(MisCentrosService);
  private misLugaresService = inject(MisLugaresService);
  private historialEntrenamientosService = inject(HistorialEntrenamientosService);
  private cdr = inject(ChangeDetectorRef);

  misValoraciones: any[] = [];
  valoracionesFiltradas: any[] = [];

  entrenamientosBase: any[] = [];
  misEntrenamientos: any[] = [];
  historialEntrenamientos: any[] = [];
  entrenamientosComunidad: any[] = [];

  centrosBase: any[] = [];
  misCentros: any[] = [];

  lugaresBase: any[] = [];
  misLugares: any[] = [];

  categoriaContenido = 'ENTRENAMIENTOS';
  tipoContenidoReal = 'ENTRENAMIENTO_BASE';
  contenidoSeleccionadoId: number | null = null;

  puntuacion = 5;
  comentario = '';
  busqueda = '';

  mensajeExito = '';
  mensajeError = '';

  ngOnInit(): void {
    this.cargarMisValoraciones();
    this.cargarOpciones();
    this.sincronizarTipoReal();
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

    this.historialEntrenamientosService.getHistorial().subscribe({
      next: (data: any[]) => {
        this.historialEntrenamientos = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR HISTORIAL:', err)
    });

    this.entrenamientosComunidadService.getEntrenamientosComunidad().subscribe({
      next: (data: any[]) => {
        this.entrenamientosComunidad = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR ENTRENAMIENTOS COMUNIDAD:', err)
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

  cambiarCategoriaContenido(): void {
    this.contenidoSeleccionadoId = null;
    this.sincronizarTipoReal();
  }

  sincronizarTipoReal(): void {
    if (this.categoriaContenido === 'ENTRENAMIENTOS') {
      this.tipoContenidoReal = 'ENTRENAMIENTO_BASE';
    } else if (this.categoriaContenido === 'CENTROS_PRIVADOS') {
      this.tipoContenidoReal = 'CENTRO_PRIVADO_BASE';
    } else {
      this.tipoContenidoReal = 'LUGAR_PUBLICO_BASE';
    }
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
      .crearOActualizarValoracion(this.tipoContenidoReal, this.contenidoSeleccionadoId, nuevaValoracion)
      .subscribe({
        next: () => {
          this.mensajeExito = 'Valoración guardada correctamente.';
          this.contenidoSeleccionadoId = null;
          this.puntuacion = 5;
          this.comentario = '';
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
      (valoracion.contenidoNombre || '').toLowerCase().includes(texto) ||
      String(valoracion.puntuacion).includes(texto) ||
      (valoracion.comentario || '').toLowerCase().includes(texto) ||
      (valoracion.fecha || '').toLowerCase().includes(texto)
    );
  }

  mostrarTipoContenido(tipo: string): string {
    switch (tipo) {
      case 'ENTRENAMIENTO_BASE':
        return 'Entrenamiento base';
      case 'ENTRENAMIENTO_USUARIO':
        return 'Mi entrenamiento';
      case 'HISTORIAL_ENTRENAMIENTO':
        return 'Entrenamiento del historial';
      case 'ENTRENAMIENTO_COMUNIDAD':
        return 'Entrenamiento comunidad';
      case 'CENTRO_PRIVADO_BASE':
        return 'Centro privado base';
      case 'CENTRO_PRIVADO_USUARIO':
        return 'Mi centro';
      case 'LUGAR_PUBLICO_BASE':
        return 'Lugar público base';
      case 'LUGAR_PUBLICO_USUARIO':
        return 'Mi lugar';
      default:
        return tipo;
    }
  }

  getOpcionesActuales(): { grupo: string, tipoReal: string, items: any[] }[] {
    if (this.categoriaContenido === 'ENTRENAMIENTOS') {
      return [
        { grupo: 'Entrenamientos base', tipoReal: 'ENTRENAMIENTO_BASE', items: this.entrenamientosBase },
        { grupo: 'Mis entrenamientos', tipoReal: 'ENTRENAMIENTO_USUARIO', items: this.misEntrenamientos },
        { grupo: 'Entrenamientos del historial', tipoReal: 'HISTORIAL_ENTRENAMIENTO', items: this.historialEntrenamientos },
        { grupo: 'Entrenamientos comunidad', tipoReal: 'ENTRENAMIENTO_COMUNIDAD', items: this.entrenamientosComunidad }
      ];
    }

    if (this.categoriaContenido === 'CENTROS_PRIVADOS') {
      return [
        { grupo: 'Centros base', tipoReal: 'CENTRO_PRIVADO_BASE', items: this.centrosBase },
        { grupo: 'Mis centros', tipoReal: 'CENTRO_PRIVADO_USUARIO', items: this.misCentros }
      ];
    }

    return [
      { grupo: 'Lugares públicos base', tipoReal: 'LUGAR_PUBLICO_BASE', items: this.lugaresBase },
      { grupo: 'Mis lugares', tipoReal: 'LUGAR_PUBLICO_USUARIO', items: this.misLugares }
    ];
  }

  seleccionarContenido(valor: string): void {
    if (!valor) {
      this.sincronizarTipoReal();
      this.contenidoSeleccionadoId = null;
      return;
    }

    const separador = valor.lastIndexOf('-');
    const tipo = valor.substring(0, separador);
    const id = valor.substring(separador + 1);

    this.tipoContenidoReal = tipo;
    this.contenidoSeleccionadoId = Number(id);
  }

  getValorSeleccionadoContenido(): string {
    if (!this.contenidoSeleccionadoId) {
      return '';
    }

    return `${this.tipoContenidoReal}-${this.contenidoSeleccionadoId}`;
  }

  getTextoOpcion(item: any, tipoReal: string): string {
    switch (tipoReal) {
      case 'ENTRENAMIENTO_BASE':
        return `${item.nombre} - ${item.nivel} - ${item.duracionEnMinutos} min`;
      case 'ENTRENAMIENTO_USUARIO':
        return `${item.nombre} - ${item.nivel} - ${item.duracionEnMinutos} min`;
      case 'HISTORIAL_ENTRENAMIENTO':
        return `${item.entrenamiento || 'Entrenamiento'} - ${item.fecha || 'Sin fecha'}`;
      case 'ENTRENAMIENTO_COMUNIDAD':
        return `${item.nombre} - ${item.usuario || 'Comunidad'} - ${item.duracionEnMinutos} min`;
      case 'CENTRO_PRIVADO_BASE':
        return `${item.nombre} - ${item.direccion}`;
      case 'CENTRO_PRIVADO_USUARIO':
        return `${item.nombre} - ${item.direccion}`;
      case 'LUGAR_PUBLICO_BASE':
        return `${item.nombre} - ${item.tipo}`;
      case 'LUGAR_PUBLICO_USUARIO':
        return `${item.nombre} - ${item.tipo}`;
      default:
        return item.nombre || 'Contenido';
    }
  }
}
