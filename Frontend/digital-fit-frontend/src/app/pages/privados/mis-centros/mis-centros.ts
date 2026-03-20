import { MisCentrosService } from './../../../services/centros/mis-centros-service';
import { Footer } from './../../../components/footer/footer';
import { HeaderComponent } from './../../../components/header/header';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-mis-centros',
  imports: [HeaderComponent, Footer, FormsModule, RouterLink],
  templateUrl: './mis-centros.html',
  styleUrl: './mis-centros.css'
})
export class MisCentrosComponent {

  private misCentrosService = inject(MisCentrosService);
  private cdr = inject(ChangeDetectorRef);

  centros: any[] = [];

  nombre = '';
  direccion = '';
  telefono = '';
  horario = '';
  precioMensual = 30;
  descripcion = '';
  latitud = 0;
  longitud = 0;

  mensajeExito = '';
  mensajeError = '';

  ngOnInit(): void {
    this.cargarMisCentros();
  }

  cargarMisCentros(): void {
    this.misCentrosService.getMisCentros().subscribe({
      next: (data) => {
        console.log('MIS CENTROS:', data);
        this.centros = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR MIS CENTROS:', err);
      }
    });
  }

  crearCentro(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    const nuevoCentro = {
      nombre: this.nombre,
      direccion: this.direccion,
      telefono: this.telefono,
      horario: this.horario,
      precioMensual: this.precioMensual,
      descripcion: this.descripcion,
      latitud: this.latitud,
      longitud: this.longitud
    };

    this.misCentrosService.crearCentroDesdeMaps(nuevoCentro).subscribe({
      next: () => {
        this.mensajeExito = 'Centro guardado correctamente.';
        this.nombre = '';
        this.direccion = '';
        this.telefono = '';
        this.horario = '';
        this.precioMensual = 30;
        this.descripcion = '';
        this.latitud = 0;
        this.longitud = 0;
        this.cargarMisCentros();
      },
      error: () => {
        this.mensajeError = 'No se pudo guardar el centro.';
      }
    });
  }

  borrarCentro(id: number): void {
    this.misCentrosService.borrarCentro(id).subscribe({
      next: () => {
        this.cargarMisCentros();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el centro.';
      }
    });
  }
}
