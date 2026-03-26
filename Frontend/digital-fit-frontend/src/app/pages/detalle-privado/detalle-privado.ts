import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { PrivadosService } from '../../services/centros/privados-service';

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

    this.privadosService.getCentroPrivadoById(id).subscribe({
      next: (data: any) => {
        this.centro = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR DETALLE CENTRO PRIVADO:', err);
      }
    });
  }
}