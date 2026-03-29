import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { HeaderComponent } from '../../../../components/header/header';
import { Footer } from '../../../../components/footer/footer';
import { MisLugaresService } from '../../../../services/publicos/mis-lugares';

@Component({
  selector: 'app-detalle-mi-lugar',
  imports: [HeaderComponent, Footer, RouterLink],
  templateUrl: './detalle-mi-lugar.html',
  styleUrl: './detalle-mi-lugar.css'
})
export class DetalleMiLugarComponent {

  private route = inject(ActivatedRoute);
  private misLugaresService = inject(MisLugaresService);
  private cdr = inject(ChangeDetectorRef);

  lugar: any = null;
  mensajeError = '';

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.misLugaresService.getMiLugarById(id).subscribe({
      next: (data) => {
        this.lugar = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR DETALLE MI LUGAR:', err);
        this.mensajeError = 'No se pudo cargar el detalle del lugar.';
      }
    });
  }

  mostrarTipoLegible(tipo: string): string {
    if (!tipo) {
      return 'Sin tipo';
    }

    return tipo.replaceAll('_', ' ').toLowerCase()
      .replace(/\b\w/g, (letra) => letra.toUpperCase());
  }

  hayCoordenadas(): boolean {
    return this.lugar?.latitud !== null && this.lugar?.latitud !== undefined
      && this.lugar?.longitud !== null && this.lugar?.longitud !== undefined;
  }
}
