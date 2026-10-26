import { EntrenamientosService } from './../../../services/entrenamientos/entrenamientos-service';
import { Footer } from './../../../components/footer/footer';
import { HeaderComponent } from './../../../components/header/header';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-detalle-entrenamiento',
  imports: [HeaderComponent, Footer],
  templateUrl: './detalle-entrenamiento-base.html',
  styleUrl: './detalle-entrenamiento-base.css'
})
export class DetalleEntrenamientoComponent {

  private route = inject(ActivatedRoute);
  private entrenamientosService = inject(EntrenamientosService);
  private cdr = inject(ChangeDetectorRef);

  entrenamiento: any = null;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.entrenamientosService.getEntrenamientoById(id).subscribe({
      next: (data) => {
        console.log('DETALLE ENTRENAMIENTO:', data);
        this.entrenamiento = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR DETALLE ENTRENAMIENTO:', err);
      }
    });
  }
}
