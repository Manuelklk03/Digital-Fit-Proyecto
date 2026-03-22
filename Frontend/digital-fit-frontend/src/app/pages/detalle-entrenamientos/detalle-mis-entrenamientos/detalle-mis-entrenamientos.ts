import { HeaderComponent } from './../../../components/header/header';
import { Footer } from './../../../components/footer/footer';
import { MisEntrenamientosService } from './../../../services/entrenamientos/mis-entrenamientos-service';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-detalle-mi-entrenamiento',
  imports: [HeaderComponent, Footer],
  templateUrl: './detalle-mis-entrenamientos.html',
  styleUrl: './detalle-mis-entrenamientos.css'
})
export class DetalleMiEntrenamientoComponent {

  private route = inject(ActivatedRoute);
  private misEntrenamientosService = inject(MisEntrenamientosService);
  private cdr = inject(ChangeDetectorRef);

  entrenamiento: any = null;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.misEntrenamientosService.getMiEntrenamientoById(id).subscribe({
      next: (data) => {
        console.log('DETALLE MI ENTRENAMIENTO:', data);
        this.entrenamiento = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR DETALLE MI ENTRENAMIENTO:', err);
      }
    });
  }
}
