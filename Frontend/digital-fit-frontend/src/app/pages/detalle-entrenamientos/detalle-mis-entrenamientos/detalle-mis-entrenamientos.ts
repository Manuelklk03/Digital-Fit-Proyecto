import { HeaderComponent } from './../../../components/header/header';
import { Footer } from './../../../components/footer/footer';
import { MisEntrenamientosService } from './../../../services/entrenamientos/mis-entrenamientos-service';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-detalle-mi-entrenamiento',
  imports: [HeaderComponent, Footer, RouterLink],
  templateUrl: './detalle-mis-entrenamientos.html',
  styleUrl: './detalle-mis-entrenamientos.css'
})
export class DetalleMiEntrenamientoComponent {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private misEntrenamientosService = inject(MisEntrenamientosService);
  private cdr = inject(ChangeDetectorRef);

  entrenamiento: any = null;

  mostrarPopup = false;
  textoPopup = '';
  tipoPopup: 'exito' | 'error' = 'exito';
  private popupTimeout: any;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.misEntrenamientosService.getMiEntrenamientoById(id).subscribe({
      next: (data: any) => {
        this.entrenamiento = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR DETALLE MI ENTRENAMIENTO:', err);
        this.abrirPopup('No se pudo cargar el detalle del entrenamiento.', 'error');
      }
    });
  }

  borrarEntrenamiento(): void {
    if (!this.entrenamiento?.id) {
      return;
    }

    this.misEntrenamientosService.borrarEntrenamiento(this.entrenamiento.id).subscribe({
      next: () => {
        this.router.navigate(['/mis-entrenamientos']);
      },
      error: () => {
        this.abrirPopup('No se pudo borrar el entrenamiento.', 'error');
      }
    });
  }

  mostrarCategoriaLegible(categoria: string): string {
    if (!categoria) {
      return 'Sin categoría';
    }

    return categoria.replaceAll('_', ' ').toLowerCase()
      .replace(/\b\w/g, letra => letra.toUpperCase());
  }

  mostrarNivelLegible(nivel: string): string {
    if (!nivel) {
      return 'Sin nivel';
    }

    return nivel.replaceAll('_', ' ').toLowerCase()
      .replace(/\b\w/g, letra => letra.toUpperCase());
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