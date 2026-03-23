import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HeaderComponent } from '../../../components/header/header';
import { Footer } from '../../../components/footer/footer';
import { HistorialEntrenamientosService } from '../../../services/entrenamientos/historial-entrenamientos';

@Component({
  selector: 'app-detalle-historial-entrenamiento',
  imports: [HeaderComponent, Footer],
  templateUrl: './detalle-historial.html',
  styleUrl: './detalle-historial.css'
})
export class DetalleHistorialEntrenamientoComponent {

  private route = inject(ActivatedRoute);
  private historialService = inject(HistorialEntrenamientosService);
  private cdr = inject(ChangeDetectorRef);

  registro: any = null;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.historialService.getHistorialById(id).subscribe({
      next: (data) => {
        console.log('DETALLE HISTORIAL:', data);
        this.registro = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR DETALLE HISTORIAL:', err);
      }
    });
  }
}
