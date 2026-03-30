import { MisCentrosService } from './../../../services/centros/mis-centros-service';
import { Footer } from './../../../components/footer/footer';
import { HeaderComponent } from './../../../components/header/header';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-detalle-mi-centro',
  imports: [HeaderComponent, Footer, RouterLink],
  templateUrl: './detalle-mi-centro.html',
  styleUrl: './detalle-mi-centro.css'
})
export class DetalleMiCentroComponent {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private misCentrosService = inject(MisCentrosService);
  private cdr = inject(ChangeDetectorRef);

  centro: any = null;

  mostrarPopup = false;
  textoPopup = '';
  tipoPopup: 'exito' | 'error' = 'exito';
  private popupTimeout: any;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.misCentrosService.getMiCentroById(id).subscribe({
      next: (data: any) => {
        this.centro = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR DETALLE MI CENTRO:', err);
        this.abrirPopup('No se pudo cargar el detalle del centro.', 'error');
      }
    });
  }

  borrarCentro(): void {
    if (!this.centro?.id) {
      return;
    }

    this.misCentrosService.borrarCentro(this.centro.id).subscribe({
      next: () => {
        this.router.navigate(['/mis-centros']);
      },
      error: () => {
        this.abrirPopup('No se pudo borrar el centro.', 'error');
      }
    });
  }

  abrirPopup(texto: string, tipo: 'exito' | 'error'): void {
    this.textoPopup = texto;
    this.tipoPopup = tipo;
    this.mostrarPopup = true;
    this.cdr.detectChanges();

    if (this.popupTimeout) {
      clearTimeout(this.popupTimeout);
    }

    this.popupTimeout = setTimeout(() => {
      this.mostrarPopup = false;
      this.cdr.detectChanges();
    }, 3000);
  }

  cerrarPopup(): void {
    this.mostrarPopup = false;
    this.cdr.detectChanges();
  }
}