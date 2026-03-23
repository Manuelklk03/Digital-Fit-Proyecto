import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../../components/header/header';
import { Footer } from '../../../components/footer/footer';
import { HistorialEntrenamientosService } from '../../../services/entrenamientos/historial-entrenamientos';

@Component({
  selector: 'app-historial-entrenamientos',
  imports: [HeaderComponent, Footer, FormsModule, RouterLink],
  templateUrl: './historial-entrenamientos.html',
  styleUrl: './historial-entrenamientos.css'
})
export class HistorialEntrenamientosComponent {

  private historialService = inject(HistorialEntrenamientosService);
  private cdr = inject(ChangeDetectorRef);

  historial: any[] = [];

  entrenamientoBaseId: number | null = null;
  entrenamientoUsuarioId: number | null = null;
  lugarPublicoId: number | null = null;
  centroPrivadoId: number | null = null;
  fecha = '';
  duracionEnMinutos = 30;
  notas = '';

  mensajeExito = '';
  mensajeError = '';

  ngOnInit(): void {
    this.cargarHistorial();
  }

  cargarHistorial(): void {
    this.historialService.getHistorial().subscribe({
      next: (data) => {
        console.log('HISTORIAL:', data);
        this.historial = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR HISTORIAL:', err);
      }
    });
  }

  crearRegistro(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    const nuevoRegistro = {
      entrenamientoBaseId: this.entrenamientoBaseId,
      entrenamientoUsuarioId: this.entrenamientoUsuarioId,
      lugarPublicoId: this.lugarPublicoId,
      centroPrivadoId: this.centroPrivadoId,
      fecha: this.fecha,
      duracionEnMinutos: this.duracionEnMinutos,
      notas: this.notas
    };

    this.historialService.crearRegistro(nuevoRegistro).subscribe({
      next: () => {
        this.mensajeExito = 'Registro guardado correctamente.';
        this.entrenamientoBaseId = null;
        this.entrenamientoUsuarioId = null;
        this.lugarPublicoId = null;
        this.centroPrivadoId = null;
        this.fecha = '';
        this.duracionEnMinutos = 30;
        this.notas = '';
        this.cargarHistorial();
      },
      error: () => {
        this.mensajeError = 'No se pudo guardar el registro.';
      }
    });
  }

  borrarRegistro(id: number): void {
    this.historialService.borrarRegistro(id).subscribe({
      next: () => {
        this.cargarHistorial();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el registro.';
      }
    });
  }
}