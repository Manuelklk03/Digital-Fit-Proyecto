import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MisLugaresService } from '../../../services/publicos/mis-lugares';
import { Footer } from '../../../components/footer/footer';
import { HeaderComponent } from '../../../components/header/header';

@Component({
  selector: 'app-mis-lugares',
  imports: [HeaderComponent, Footer, FormsModule, RouterLink],
  templateUrl: './mis-lugares.html',
  styleUrl: './mis-lugares.css'
})
export class MisLugaresComponent {

  private misLugaresService = inject(MisLugaresService);
  private cdr = inject(ChangeDetectorRef);

  lugares: any[] = [];

  nombre = '';
  direccion = '';
  descripcion = '';
  telefono = '';
  horario = '';
  latitud = 0;
  longitud = 0;
  tipo = 'PARQUE_PUBLICO';

  mensajeExito = '';
  mensajeError = '';

  ngOnInit(): void {
    this.cargarMisLugares();
  }

  cargarMisLugares(): void {
    this.misLugaresService.getMisLugares().subscribe({
      next: (data) => {
        console.log('MIS LUGARES:', data);
        this.lugares = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR MIS LUGARES:', err);
      }
    });
  }

  crearLugar(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    const nuevoLugar = {
      nombre: this.nombre,
      direccion: this.direccion,
      descripcion: this.descripcion,
      telefono: this.telefono,
      horario: this.horario,
      latitud: this.latitud,
      longitud: this.longitud,
      tipo: this.tipo
    };

    this.misLugaresService.crearLugarDesdeMaps(nuevoLugar).subscribe({
      next: () => {
        this.mensajeExito = 'Lugar guardado correctamente.';
        this.nombre = '';
        this.direccion = '';
        this.descripcion = '';
        this.telefono = '';
        this.horario = '';
        this.latitud = 0;
        this.longitud = 0;
        this.tipo = 'PARQUE_PUBLICO';
        this.cargarMisLugares();
      },
      error: () => {
        this.mensajeError = 'No se pudo guardar el lugar.';
      }
    });
  }

  borrarLugar(id: number): void {
    this.misLugaresService.borrarLugar(id).subscribe({
      next: () => {
        this.cargarMisLugares();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el lugar.';
      }
    });
  }
}