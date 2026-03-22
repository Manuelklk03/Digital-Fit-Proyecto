import { MisEntrenamientosService } from './../../../services/entrenamientos/mis-entrenamientos-service';
import { Footer } from './../../../components/footer/footer';
import { HeaderComponent } from './../../../components/header/header';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-mis-entrenamientos',
  imports: [HeaderComponent, Footer, FormsModule, RouterLink],
  templateUrl: './mis-entrenamientos.html',
  styleUrl: './mis-entrenamientos.css'
})
export class MisEntrenamientosComponent {

  private misEntrenamientosService = inject(MisEntrenamientosService);
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
    this.cargarMisEntrenamientos();
  }

  cargarMisEntrenamientos(): void {
    this.misEntrenamientosService.getMisEntrenamientos().subscribe({
      next: (data) => {
        console.log('MIS ENTRENAMIENTOS:', data);
        this.entrenamientos = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR MIS ENTRENAMIENTOS:', err);
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
      duracionEnMinutos: this.duracionEnMinutos
    };

    this.misEntrenamientosService.crearEntrenamiento(nuevoEntrenamiento).subscribe({
      next: () => {
        this.mensajeExito = 'Entrenamiento creado correctamente.';
        this.nombre = '';
        this.descripcion = '';
        this.categoria = 'FUERZA_TOTAL';
        this.nivel = 'PRINCIPIANTE';
        this.duracionEnMinutos = 30;
        this.cargarMisEntrenamientos();
      },
      error: () => {
        this.mensajeError = 'No se pudo crear el entrenamiento.';
      }
    });
  }

  borrarEntrenamiento(id: number): void {
    this.misEntrenamientosService.borrarEntrenamiento(id).subscribe({
      next: () => {
        this.cargarMisEntrenamientos();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el entrenamiento.';
      }
    });
  }
}
