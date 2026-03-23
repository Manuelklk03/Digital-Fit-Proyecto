import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../../components/header/header';
import { Footer } from '../../../components/footer/footer';
import { EntrenamientosComunidadService } from '../../../services/entrenamientos/entrenamiento-comunidad-service';

@Component({
  selector: 'app-entrenamientos-comunidad',
  imports: [HeaderComponent, Footer, FormsModule, RouterLink],
  templateUrl: './entrenamientos-comunidad.html',
  styleUrl: './entrenamientos-comunidad.css'
})
export class EntrenamientosComunidadComponent {

  private entrenamientosComunidadService = inject(EntrenamientosComunidadService);
  private cdr = inject(ChangeDetectorRef);

  entrenamientos: any[] = [];

  nombre = '';
  descripcion = '';
  categoria = 'FUERZA_TOTAL';
  nivel = 'PRINCIPIANTE';
  duracionEnMinutos = 30;

  mensajeExito = '';
  mensajeError = '';

  ngOnInit(): void {
    this.cargarEntrenamientosComunidad();
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

  crearEntrenamiento(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    const nuevoEntrenamiento = {
      nombre: this.nombre,
      descripcion: this.descripcion,
      categoria: this.categoria,
      nivel: this.nivel,
      duracionEnMinutos: this.duracionEnMinutos,
      fechaPublicacion: new Date().toISOString().slice(0, 19).replace('T', ' ')
    };

    this.entrenamientosComunidadService.crearEntrenamientoComunidad(nuevoEntrenamiento).subscribe({
      next: () => {
        this.mensajeExito = 'Entrenamiento de comunidad creado correctamente.';
        this.nombre = '';
        this.descripcion = '';
        this.categoria = 'FUERZA_TOTAL';
        this.nivel = 'PRINCIPIANTE';
        this.duracionEnMinutos = 30;
        this.cargarEntrenamientosComunidad();
      },
      error: () => {
        this.mensajeError = 'No se pudo crear el entrenamiento de comunidad.';
      }
    });
  }
}