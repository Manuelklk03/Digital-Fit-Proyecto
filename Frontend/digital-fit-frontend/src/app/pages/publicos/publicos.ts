import { MisLugaresService } from './../../services/publicos/mis-lugares';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { PublicosService } from '../../services/publicos/publicos-service';
import { AdminLugaresPublicosBaseService } from '../../services/admin/admin-lugarespublicos-base';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-publicos',
  imports: [HeaderComponent, Footer, FormsModule, RouterLink],
  templateUrl: './publicos.html',
  styleUrl: './publicos.css'
})
export class PublicosComponent {

  private publicosService = inject(PublicosService);
  private misLugaresService = inject(MisLugaresService);
  private adminLugaresPublicosBaseService = inject(AdminLugaresPublicosBaseService);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  lugares: any[] = [];
  usuarioActual: any = null;

  nombre = '';
  direccion = '';
  descripcion = '';
  telefono = '';
  horario = '';
  latitud = 0;
  longitud = 0;
  tipo = 'PARQUE_PUBLICO';

  editandoId: number | null = null;

  tipoBusqueda = 'nombre';
  valorBusqueda = '';

  mensajeExito = '';
  mensajeError = '';

  mostrarToast = false;
  textoToast = '';
  tipoToast: 'exito' | 'error' = 'exito';
  private toastTimeout: any;

  ngOnInit(): void {
    this.authService.me().subscribe({
      next: (usuario) => {
        this.usuarioActual = usuario;
        this.cargarLugares();
      },
      error: () => {
        this.cargarLugares();
      }
    });
  }

  cargarLugares(): void {
    this.publicosService.getLugaresPublicos().subscribe({
      next: (data) => {
        this.lugares = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR LUGARES PUBLICOS BASE:', err);
        this.mensajeError = 'No se pudieron cargar los lugares públicos.';
        this.mostrarToastMensaje('No se pudieron cargar los lugares públicos.', 'error');
      }
    });
  }

  buscarLugares(): void {
    const texto = this.valorBusqueda.trim();

    if (!texto) {
      this.cargarLugares();
      return;
    }

    if (this.tipoBusqueda === 'tipo') {
      this.publicosService.getLugaresPublicosFiltrados(undefined, undefined, texto).subscribe({
        next: (data) => {
          this.lugares = data;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('ERROR FILTRO TIPO LUGARES:', err);
        }
      });

      return;
    }

    if (this.tipoBusqueda === 'direccion') {
      this.publicosService.getLugaresPublicosFiltrados(undefined, texto, undefined).subscribe({
        next: (data) => {
          this.lugares = data;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('ERROR FILTRO DIRECCION LUGARES:', err);
        }
      });

      return;
    }

    this.publicosService.getLugaresPublicosFiltrados(texto, undefined, undefined).subscribe({
      next: (data) => {
        this.lugares = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR FILTRO NOMBRE LUGARES:', err);
      }
    });
  }

  alCambiarBusqueda(): void {
    this.buscarLugares();
  }

  limpiarBusqueda(): void {
    this.tipoBusqueda = 'nombre';
    this.valorBusqueda = '';
    this.cargarLugares();
  }

  esAdmin(): boolean {
    return this.usuarioActual?.rol === 'ADMIN';
  }

  guardarLugarBase(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    const payload = {
      nombre: this.nombre,
      direccion: this.direccion,
      descripcion: this.descripcion,
      telefono: this.telefono,
      horario: this.horario,
      latitud: this.latitud,
      longitud: this.longitud,
      tipo: this.tipo
    };

    if (this.editandoId !== null) {
      this.adminLugaresPublicosBaseService.actualizarLugarPublicoBase(this.editandoId, payload).subscribe({
        next: () => {
          this.mensajeExito = 'Lugar público base actualizado correctamente.';
          this.mostrarToastMensaje('Lugar público base actualizado correctamente.', 'exito');
          this.limpiarFormulario();
          this.cargarLugares();
        },
        error: () => {
          this.mensajeError = 'No se pudo actualizar el lugar público base.';
          this.mostrarToastMensaje('No se pudo actualizar el lugar público base.', 'error');
        }
      });
    } else {
      this.adminLugaresPublicosBaseService.crearLugarPublicoBase(payload).subscribe({
        next: () => {
          this.mensajeExito = 'Lugar público base creado correctamente.';
          this.mostrarToastMensaje('Lugar público base creado correctamente.', 'exito');
          this.limpiarFormulario();
          this.cargarLugares();
        },
        error: () => {
          this.mensajeError = 'No se pudo crear el lugar público base.';
          this.mostrarToastMensaje('No se pudo crear el lugar público base.', 'error');
        }
      });
    }
  }

  editarLugarBase(lugar: any): void {
    this.editandoId = lugar.id;
    this.nombre = lugar.nombre;
    this.direccion = lugar.direccion;
    this.descripcion = lugar.descripcion;
    this.telefono = lugar.telefono;
    this.horario = lugar.horario;
    this.latitud = lugar.latitud ?? 0;
    this.longitud = lugar.longitud ?? 0;
    this.tipo = lugar.tipo;

    this.mensajeExito = '';
    this.mensajeError = '';
  }

  borrarLugarBase(id: number): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    this.adminLugaresPublicosBaseService.borrarLugarPublicoBase(id).subscribe({
      next: () => {
        this.mensajeExito = 'Lugar público base borrado correctamente.';
        this.mostrarToastMensaje('Lugar público base borrado correctamente.', 'exito');
        this.cargarLugares();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el lugar público base.';
        this.mostrarToastMensaje('No se pudo borrar el lugar público base.', 'error');
      }
    });
  }

  anadirAMisLugares(id: number): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    this.misLugaresService.anadirLugarDesdeBase(id).subscribe({
      next: () => {
        this.mensajeExito = 'Lugar añadido a mis lugares.';
        this.mostrarToastMensaje('Lugar añadido a mis lugares.', 'exito');
      },
      error: () => {
        this.mensajeError = 'No se pudo añadir a mis lugares.';
        this.mostrarToastMensaje('No se pudo añadir a mis lugares.', 'error');
      }
    });
  }

  cancelarEdicion(): void {
    this.limpiarFormulario();
  }

  limpiarFormulario(): void {
    this.editandoId = null;
    this.nombre = '';
    this.direccion = '';
    this.descripcion = '';
    this.telefono = '';
    this.horario = '';
    this.latitud = 0;
    this.longitud = 0;
    this.tipo = 'PARQUE_PUBLICO';
  }

  hayCoordenadas(lugar: any): boolean {
    return lugar?.latitud !== null && lugar?.latitud !== undefined
      && lugar?.longitud !== null && lugar?.longitud !== undefined;
  }

  mostrarTipoLegible(tipo: string): string {
    if (!tipo) {
      return 'Sin tipo';
    }

    return tipo.replaceAll('_', ' ').toLowerCase()
      .replace(/\b\w/g, (letra) => letra.toUpperCase());
  }

  mostrarToastMensaje(texto: string, tipo: 'exito' | 'error'): void {
    this.textoToast = texto;
    this.tipoToast = tipo;
    this.mostrarToast = true;

    if (this.toastTimeout) {
      clearTimeout(this.toastTimeout);
    }

    this.toastTimeout = setTimeout(() => {
      this.mostrarToast = false;
    }, 3000);
  }

  cerrarToast(): void {
    this.mostrarToast = false;
  }
}
