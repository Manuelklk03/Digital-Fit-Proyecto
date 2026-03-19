import { ValoracionesService } from './../../services/valoraciones/valoraciones-service';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';

@Component({
  selector: 'app-valoraciones',
  imports: [HeaderComponent, Footer, FormsModule],
  templateUrl: './valoraciones.html',
  styleUrl: './valoraciones.css'
})
export class ValoracionesComponent {

  private valoracionesService = inject(ValoracionesService);
  private cdr = inject(ChangeDetectorRef);

  misValoraciones: any[] = [];

  tipoContenido = 'CENTRO_PRIVADO';
  idContenido = 1;
  puntuacion = 5;
  comentario = '';

  mensajeExito = '';
  mensajeError = '';

  ngOnInit(): void {
    this.cargarMisValoraciones();
  }

  cargarMisValoraciones(): void {
    this.valoracionesService.getMisValoraciones().subscribe({
      next: (data) => {
        console.log('MIS VALORACIONES:', data);
        this.misValoraciones = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR MIS VALORACIONES:', err);
      }
    });
  }

  guardarValoracion(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    const nuevaValoracion = {
      puntuacion: this.puntuacion,
      comentario: this.comentario
    };

    this.valoracionesService
      .crearOActualizarValoracion(this.tipoContenido, this.idContenido, nuevaValoracion)
      .subscribe({
        next: () => {
          this.mensajeExito = 'Valoración guardada correctamente.';
          this.comentario = '';
          this.cargarMisValoraciones();
        },
        error: () => {
          this.mensajeError = 'No se pudo guardar la valoración.';
        }
      });
  }

  borrarValoracion(tipo: string, contenidoId: number): void {
    this.valoracionesService.borrarValoracion(tipo, contenidoId).subscribe({
      next: () => {
        this.cargarMisValoraciones();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar la valoración.';
      }
    });
  }
}
