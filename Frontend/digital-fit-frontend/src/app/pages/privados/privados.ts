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

  mensajeExito = '';
  mensajeError = '';

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
        console.log('CENTROS PRIVADOS BASE:', data);
        this.centros = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR CENTROS PRIVADOS BASE:', err);
      }
    });
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
          this.limpiarFormulario();
          this.cargarCentros();
        },
        error: () => {
          this.mensajeError = 'No se pudo actualizar el centro privado base.';
        }
      });
    } else {
      this.adminCentrosPrivadosBaseService.crearCentroPrivadoBase(payload).subscribe({
        next: () => {
          this.mensajeExito = 'Centro privado base creado correctamente.';
          this.limpiarFormulario();
          this.cargarCentros();
        },
        error: () => {
          this.mensajeError = 'No se pudo crear el centro privado base.';
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
  }

  borrarCentroBase(id: number): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    this.adminCentrosPrivadosBaseService.borrarCentroPrivadoBase(id).subscribe({
      next: () => {
        this.mensajeExito = 'Centro privado base borrado correctamente.';
        this.cargarCentros();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el centro privado base.';
      }
    });
  }

  anadirAMisCentros(id: number): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    this.misCentrosService.anadirCentroDesdeApp(id).subscribe({
      next: () => {
        this.mensajeExito = 'Centro añadido a mis centros.';
      },
      error: () => {
        this.mensajeError = 'No se pudo añadir a mis centros.';
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
}