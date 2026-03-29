import { MisCentrosService } from './../../../services/centros/mis-centros-service';
import { Footer } from './../../../components/footer/footer';
import { HeaderComponent } from './../../../components/header/header';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-detalle-mi-centro',
  imports: [HeaderComponent, Footer, RouterLink],
  templateUrl: './detalle-mi-centro.html',
  styleUrl: './detalle-mi-centro.css'
})
export class DetalleMiCentroComponent {

  private route = inject(ActivatedRoute);
  private misCentrosService = inject(MisCentrosService);
  private cdr = inject(ChangeDetectorRef);

  centro: any = null;
  mensajeError = '';

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.misCentrosService.getMiCentroById(id).subscribe({
      next: (data) => {
        this.centro = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR DETALLE MI CENTRO:', err);
        this.mensajeError = 'No se pudo cargar el detalle del centro.';
      }
    });
  }

  hayCoordenadas(): boolean {
    return this.centro?.latitud !== null && this.centro?.latitud !== undefined
      && this.centro?.longitud !== null && this.centro?.longitud !== undefined;
  }
}
