import { MisCentrosService } from './../../../services/centros/mis-centros-service';
import { Footer } from './../../../components/footer/footer';
import { HeaderComponent } from './../../../components/header/header';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-detalle-mi-centro',
  imports: [HeaderComponent, Footer],
  templateUrl: './detalle-mi-centro.html',
  styleUrl: './detalle-mi-centro.css'
})
export class DetalleMiCentroComponent {

  private route = inject(ActivatedRoute);
  private misCentrosService = inject(MisCentrosService);
  private cdr = inject(ChangeDetectorRef);

  centro: any = null;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.misCentrosService.getMiCentroById(id).subscribe({
      next: (data) => {
        console.log('DETALLE MI CENTRO:', data);
        this.centro = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR DETALLE MI CENTRO:', err);
      }
    });
  }
}
