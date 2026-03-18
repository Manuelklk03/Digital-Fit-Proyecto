import { PrivadosService } from './../../services/centros/privados-service';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';


@Component({
  selector: 'app-detalle-privado',
  imports: [HeaderComponent, Footer],
  templateUrl: './detalle-privado.html',
  styleUrl: './detalle-privado.css'
})
export class DetallePrivadoComponent {

  private route = inject(ActivatedRoute);
  private privadosService = inject(PrivadosService);
  private cdr = inject(ChangeDetectorRef);

  centro: any = null;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.privadosService.getPrivadoById(id).subscribe({
      next: (data) => {
        console.log('DETALLE PRIVADO:', data);
        this.centro = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR DETALLE PRIVADO:', err);
      }
    });
  }
}
