import { HeaderComponent } from './../../../components/header/header';
import { Footer } from './../../../components/footer/footer';
import { AuthService } from './../../../services/auth-service';
import { EntrenamientosComunidadService } from './../../../services/entrenamientos/entrenamiento-comunidad-service';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-entrenamientos-comunidad',
  imports: [HeaderComponent, Footer, FormsModule, RouterLink],
  templateUrl: './entrenamientos-comunidad.html',
  styleUrl: './entrenamientos-comunidad.css'
})
export class EntrenamientosComunidadComponent {

  private entrenamientosComunidadService = inject(EntrenamientosComunidadService);
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
        this.cargarEntrenamientosComunidad();
      },
      error: () => {
        this.cargarEntrenamientosComunidad();
      }
    });
  }

  cargarEntrenamientosComunidad(): void {
    this.entrenamientosComunidadService.getEntrenamientosComunidad().subscribe({
      next: (data) => {
        console.log('ENTRENAMIENTOS COMUNIDAD:', data);
        this.entrenamientos = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR ENTRENAMIENTOS COMUNIDAD:', err);
      }
    });
  }

  esMio(entrenamiento: any): boolean {
    return this.usuarioActual?.username === entrenamiento.usuario;
  }

  guardarEntrenamiento(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    const payload = {
      nombre: this.nombre,
      descripcion: this.descripcion,
      categoria: this.categoria,
      nivel: this.nivel,
      duracionEnMinutos: this.duracionEnMinutos,
      fechaPublicacion: new Date().toISOString().slice(0, 19).replace('T', ' ')
    };

    if (this.editandoId !== null) {
      this.entrenamientosComunidadService.actualizarEntrenamientoComunidad(this.editandoId, payload).subscribe({
        next: () => {
          this.mensajeExito = 'Entrenamiento actualizado correctamente.';
          this.limpiarFormulario();
          this.cargarEntrenamientosComunidad();
        },
        error: () => {
          this.mensajeError = 'No se pudo actualizar el entrenamiento.';
        }
      });
    } else {
      this.entrenamientosComunidadService.crearEntrenamientoComunidad(payload).subscribe({
        next: () => {
          this.mensajeExito = 'Entrenamiento de comunidad creado correctamente.';
          this.limpiarFormulario();
          this.cargarEntrenamientosComunidad();
        },
        error: () => {
          this.mensajeError = 'No se pudo crear el entrenamiento de comunidad.';
        }
      });
    }
  }

  editarEntrenamiento(entrenamiento: any): void {
    this.editandoId = entrenamiento.id;
    this.nombre = entrenamiento.nombre;
    this.descripcion = entrenamiento.descripcion;
    this.categoria = entrenamiento.categoria;
    this.nivel = entrenamiento.nivel;
    this.duracionEnMinutos = entrenamiento.duracionEnMinutos;
  }

  borrarEntrenamiento(id: number): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    this.entrenamientosComunidadService.borrarEntrenamientoComunidad(id).subscribe({
      next: () => {
        this.mensajeExito = 'Entrenamiento borrado correctamente.';
        this.cargarEntrenamientosComunidad();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el entrenamiento.';
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
