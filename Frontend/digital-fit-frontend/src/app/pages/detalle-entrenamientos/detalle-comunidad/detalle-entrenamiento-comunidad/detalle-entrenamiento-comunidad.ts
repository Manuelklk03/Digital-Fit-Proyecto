import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EntrenamientosComunidadService } from '../../../../services/entrenamientos/entrenamiento-comunidad-service';
import { HeaderComponent } from '../../../../components/header/header';
import { Footer } from '../../../../components/footer/footer';

@Component({
  selector: 'app-detalle-entrenamiento-comunidad',
  imports: [HeaderComponent, Footer],
  templateUrl: './detalle-entrenamiento-comunidad.html',
  styleUrl: './detalle-entrenamiento-comunidad.css'
})
export class DetalleEntrenamientoComunidadComponent {

  private route = inject(ActivatedRoute);
  private entrenamientosComunidadService = inject(EntrenamientosComunidadService);
  private cdr = inject(ChangeDetectorRef);

  entrenamiento: any = null;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.entrenamientosComunidadService.getEntrenamientoComunidadById(id).subscribe({
      next: (data) => {
        console.log('DETALLE ENTRENAMIENTO COMUNIDAD:', data);
        this.entrenamiento = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR DETALLE ENTRENAMIENTO COMUNIDAD:', err);
      }
    });
  }
}