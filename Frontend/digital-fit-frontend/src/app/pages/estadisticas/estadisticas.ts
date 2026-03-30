import { EstadisticasService } from './../../services/estadisticas/estadisticas-service';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';

@Component({
  selector: 'app-estadisticas',
  imports: [HeaderComponent, Footer],
  templateUrl: './estadisticas.html',
  styleUrl: './estadisticas.css'
})
export class EstadisticasComponent {

  private estadisticasService = inject(EstadisticasService);
  private cdr = inject(ChangeDetectorRef);

  estadisticas: any = null;
  cargando = true;
  mensajeError = '';

  ngOnInit(): void {
    this.cargarEstadisticas();
  }

  cargarEstadisticas(): void {
    this.cargando = true;
    this.mensajeError = '';

    this.estadisticasService.getEstadisticas().subscribe({
      next: (data: any) => {
        this.estadisticas = data;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR ESTADISTICAS:', err);
        this.mensajeError = 'No se pudieron cargar las estadísticas.';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  getPromedioFormateado(): string {
    if (!this.estadisticas || this.estadisticas.promedioMinutosEntrenamiento == null) {
      return '0';
    }

    return Number(this.estadisticas.promedioMinutosEntrenamiento).toFixed(1);
  }

  getEntrenamientoMasRealizado(): string {
    if (!this.estadisticas?.entrenamientoMasRealizado || this.estadisticas.entrenamientoMasRealizado === 'N/A') {
      return 'Todavía no hay suficiente información';
    }

    return this.estadisticas.entrenamientoMasRealizado;
  }
}
