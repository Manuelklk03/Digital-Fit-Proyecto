import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HeaderComponent } from '../../../../components/header/header';
import { Footer } from '../../../../components/footer/footer';
import { MisLugaresService } from '../../../../services/publicos/mis-lugares';

@Component({
  selector: 'app-detalle-mi-lugar',
  imports: [HeaderComponent, Footer],
  templateUrl: './detalle-mi-lugar.html',
  styleUrl: './detalle-mi-lugar.css'
})
export class DetalleMiLugarComponent {

  private route = inject(ActivatedRoute);
  private misLugaresService = inject(MisLugaresService);
  private cdr = inject(ChangeDetectorRef);

  lugar: any = null;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.misLugaresService.getMiLugarById(id).subscribe({
      next: (data) => {
        console.log('DETALLE MI LUGAR:', data);
        this.lugar = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR DETALLE MI LUGAR:', err);
      }
    });
  }
}