import { HeaderComponent } from './../../../components/header/header';
import { Footer } from './../../../components/footer/footer';
import { PublicosService } from './../../../services/publicos/publicos-service';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';




@Component({
  selector: 'app-detalle-publico',
  imports: [HeaderComponent, Footer],
  templateUrl: './detalle-publico.html',
  styleUrl: './detalle-publico.css'
})
export class DetallePublicoComponent {

  private route = inject(ActivatedRoute);
  private publicosService = inject(PublicosService);
  private cdr = inject(ChangeDetectorRef);

  lugar: any = null;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.publicosService.getPublicoById(id).subscribe({
      next: (data) => {
        console.log('DETALLE PUBLICO:', data);
        this.lugar = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR DETALLE PUBLICO:', err);
      }
    });
  }
}
