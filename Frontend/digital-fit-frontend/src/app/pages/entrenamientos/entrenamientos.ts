import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { EntrenamientosService } from '../../services/entrenamientos/entrenamientos-service';
import { MisEntrenamientosService } from '../../services/entrenamientos/mis-entrenamientos-service';
import { AdminEntrenamientosBaseService } from '../../services/admin/admin-entrenamiento-base';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-entrenamientos',
  imports: [HeaderComponent, Footer, FormsModule, RouterLink],
  templateUrl: './entrenamientos.html',
  styleUrl: './entrenamientos.css'
})
export class EntrenamientosComponent {

  private entrenamientosService = inject(EntrenamientosService);
  private misEntrenamientosService = inject(MisEntrenamientosService);
  private adminEntrenamientosBaseService = inject(AdminEntrenamientosBaseService);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  entrenamientos: any[] = [];
  usuarioActual: any = null;

  nombre = '';
  descripcion = '';
  categoria = 'FUERZA_TOTAL';
  nivel = 'PRINCIPIANTE';
  duracionEnMinutos = 30;

  editandoId: number | null = null;

  mensajeExito = '';
  mensajeError = '';

  ngOnInit(): void {
    this.authService.me().subscribe({
      next: (usuario) => {
        this.usuarioActual = usuario;
        this.cargarEntrenamientos();
      },
      error: () => {
        this.cargarEntrenamientos();
      }
    });
  }

  cargarEntrenamientos(): void {
    this.entrenamientosService.getEntrenamientos().subscribe({
      next: (data) => {
        console.log('ENTRENAMIENTOS BASE:', data);
        this.entrenamientos = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR ENTRENAMIENTOS BASE:', err);
      }
    });
  }

  esAdmin(): boolean {
    return this.usuarioActual?.rol === 'ADMIN';
  }

  guardarEntrenamientoBase(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    const payload = {
      nombre: this.nombre,
      descripcion: this.descripcion,
      categoria: this.categoria,
      nivel: this.nivel,
      duracionEnMinutos: this.duracionEnMinutos
    };

    if (this.editandoId !== null) {
      this.adminEntrenamientosBaseService.actualizarEntrenamientoBase(this.editandoId, payload).subscribe({
        next: () => {
          this.mensajeExito = 'Entrenamiento base actualizado correctamente.';
          this.limpiarFormulario();
          this.cargarEntrenamientos();
        },
        error: () => {
          this.mensajeError = 'No se pudo actualizar el entrenamiento base.';
        }
      });
    } else {
      this.adminEntrenamientosBaseService.crearEntrenamientoBase(payload).subscribe({
        next: () => {
          this.mensajeExito = 'Entrenamiento base creado correctamente.';
          this.limpiarFormulario();
          this.cargarEntrenamientos();
        },
        error: () => {
          this.mensajeError = 'No se pudo crear el entrenamiento base.';
        }
      });
    }
  }

  editarEntrenamientoBase(entrenamiento: any): void {
    this.editandoId = entrenamiento.id;
    this.nombre = entrenamiento.nombre;
    this.descripcion = entrenamiento.descripcion;
    this.categoria = entrenamiento.categoria;
    this.nivel = entrenamiento.nivel;
    this.duracionEnMinutos = entrenamiento.duracionEnMinutos;
  }

  borrarEntrenamientoBase(id: number): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    this.adminEntrenamientosBaseService.borrarEntrenamientoBase(id).subscribe({
      next: () => {
        this.mensajeExito = 'Entrenamiento base borrado correctamente.';
        this.cargarEntrenamientos();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el entrenamiento base.';
      }
    });
  }

  anadirAMisEntrenamientos(id: number): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    this.misEntrenamientosService.anadirDesdeBase(id).subscribe({
      next: () => {
        this.mensajeExito = 'Entrenamiento añadido a mis entrenamientos.';
      },
      error: () => {
        this.mensajeError = 'No se pudo añadir a mis entrenamientos.';
      }
    });
  }

  cancelarEdicion(): void {
    this.limpiarFormulario();
  }

  limpiarFormulario(): void {
    this.editandoId = null;
    this.nombre = '';
    this.descripcion = '';
    this.categoria = 'FUERZA_TOTAL';
    this.nivel = 'PRINCIPIANTE';
    this.duracionEnMinutos = 30;
  }
}