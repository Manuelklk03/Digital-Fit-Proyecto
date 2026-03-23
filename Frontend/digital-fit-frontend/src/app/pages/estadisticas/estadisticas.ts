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

  ngOnInit(): void {
    this.cargarEstadisticas();
  }

  cargarEstadisticas(): void {
    this.estadisticasService.getEstadisticas().subscribe({
      next: (data) => {
        console.log('ESTADISTICAS:', data);
        this.estadisticas = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR ESTADISTICAS:', err);
      }
    });
  }
}
