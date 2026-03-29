import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { PrivadosService } from '../../services/centros/privados-service';
import { MisCentrosService } from '../../services/centros/mis-centros-service';
import { AdminCentrosPrivadosBaseService } from '../../services/admin/admin-centro-base';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-privados',
  imports: [HeaderComponent, Footer, FormsModule, RouterLink],
  templateUrl: './privados.html',
  styleUrl: './privados.css'
})
export class PrivadosComponent {

  private privadosService = inject(PrivadosService);
  private misCentrosService = inject(MisCentrosService);
  private adminCentrosPrivadosBaseService = inject(AdminCentrosPrivadosBaseService);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  centros: any[] = [];
  usuarioActual: any = null;

  nombre = '';
  direccion = '';
  telefono = '';
  horario = '';
  precioMensual = 30;
  descripcion = '';
  latitud = 0;
  longitud = 0;

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
        this.cargarCentros();
      },
      error: () => {
        this.cargarCentros();
      }
    });
  }

  cargarCentros(): void {
    this.privadosService.getCentrosPrivados().subscribe({
      next: (data) => {
        this.centros = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR CENTROS PRIVADOS BASE:', err);
        this.mensajeError = 'No se pudieron cargar los centros privados.';
        this.mostrarToastMensaje('No se pudieron cargar los centros privados.', 'error');
      }
    });
  }

  buscarCentros(): void {
    const texto = this.valorBusqueda.trim();

    if (!texto) {
      this.cargarCentros();
      return;
    }

    if (this.tipoBusqueda === 'precioMensual') {
      const precio = Number(texto);

      if (isNaN(precio)) {
        this.centros = [];
        return;
      }

      this.privadosService.getCentrosPrivadosFiltrados(undefined, undefined, precio).subscribe({
        next: (data) => {
          this.centros = data;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('ERROR FILTRO PRECIO CENTROS:', err);
        }
      });

      return;
    }

    if (this.tipoBusqueda === 'direccion') {
      this.privadosService.getCentrosPrivadosFiltrados(undefined, texto, undefined).subscribe({
        next: (data) => {
          this.centros = data;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('ERROR FILTRO DIRECCION CENTROS:', err);
        }
      });

      return;
    }

    this.privadosService.getCentrosPrivadosFiltrados(texto, undefined, undefined).subscribe({
      next: (data) => {
        this.centros = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR FILTRO NOMBRE CENTROS:', err);
      }
    });
  }

  alCambiarBusqueda(): void {
    this.buscarCentros();
  }

  limpiarBusqueda(): void {
    this.tipoBusqueda = 'nombre';
    this.valorBusqueda = '';
    this.cargarCentros();
  }

  esAdmin(): boolean {
    return this.usuarioActual?.rol === 'ADMIN';
  }

  guardarCentroBase(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

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

    if (this.editandoId !== null) {
      this.adminCentrosPrivadosBaseService.actualizarCentroPrivadoBase(this.editandoId, payload).subscribe({
        next: () => {
          this.mensajeExito = 'Centro privado base actualizado correctamente.';
          this.mostrarToastMensaje('Centro privado base actualizado correctamente.', 'exito');
          this.limpiarFormulario();
          this.cargarCentros();
        },
        error: () => {
          this.mensajeError = 'No se pudo actualizar el centro privado base.';
          this.mostrarToastMensaje('No se pudo actualizar el centro privado base.', 'error');
        }
      });
    } else {
      this.adminCentrosPrivadosBaseService.crearCentroPrivadoBase(payload).subscribe({
        next: () => {
          this.mensajeExito = 'Centro privado base creado correctamente.';
          this.mostrarToastMensaje('Centro privado base creado correctamente.', 'exito');
          this.limpiarFormulario();
          this.cargarCentros();
        },
        error: () => {
          this.mensajeError = 'No se pudo crear el centro privado base.';
          this.mostrarToastMensaje('No se pudo crear el centro privado base.', 'error');
        }
      });
    }
  }

  editarCentroBase(centro: any): void {
    this.editandoId = centro.id;
    this.nombre = centro.nombre;
    this.direccion = centro.direccion;
    this.telefono = centro.telefono;
    this.horario = centro.horario;
    this.precioMensual = centro.precioMensual;
    this.descripcion = centro.descripcion;
    this.latitud = centro.latitud ?? 0;
    this.longitud = centro.longitud ?? 0;

    this.mensajeExito = '';
    this.mensajeError = '';
  }

  borrarCentroBase(id: number): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    this.adminCentrosPrivadosBaseService.borrarCentroPrivadoBase(id).subscribe({
      next: () => {
        this.mensajeExito = 'Centro privado base borrado correctamente.';
        this.mostrarToastMensaje('Centro privado base borrado correctamente.', 'exito');
        this.cargarCentros();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el centro privado base.';
        this.mostrarToastMensaje('No se pudo borrar el centro privado base.', 'error');
      }
    });
  }

  anadirAMisCentros(id: number): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    this.misCentrosService.anadirCentroDesdeApp(id).subscribe({
      next: () => {
        this.mensajeExito = 'Centro añadido a mis centros.';
        this.mostrarToastMensaje('Centro añadido a mis centros.', 'exito');
      },
      error: () => {
        this.mensajeError = 'No se pudo añadir a mis centros.';
        this.mostrarToastMensaje('No se pudo añadir a mis centros.', 'error');
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
    this.telefono = '';
    this.horario = '';
    this.precioMensual = 30;
    this.descripcion = '';
    this.latitud = 0;
    this.longitud = 0;
  }

  hayCoordenadas(centro: any): boolean {
    return centro?.latitud !== null && centro?.latitud !== undefined
      && centro?.longitud !== null && centro?.longitud !== undefined;
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
