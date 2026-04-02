import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MisLugaresService } from '../../../services/publicos/mis-lugares';
import { Footer } from '../../../components/footer/footer';
import { HeaderComponent } from '../../../components/header/header';

@Component({
  selector: 'app-mis-lugares',
  imports: [HeaderComponent, Footer, FormsModule, RouterLink],
  templateUrl: './mis-lugares.html',
  styleUrl: './mis-lugares.css'
})
export class MisLugaresComponent {

  private misLugaresService = inject(MisLugaresService);
  private cdr = inject(ChangeDetectorRef);

  lugares: any[] = [];

  tipoBusqueda = 'nombre';
  valorBusqueda = '';

  mostrarPopup = false;
  textoPopup = '';
  tipoPopup: 'exito' | 'error' = 'exito';
  private popupTimeout: any;

  ngOnInit(): void {
    this.cargarMisLugares();
  }

  cargarMisLugares(): void {
    this.misLugaresService.getMisLugares().subscribe({
      next: (data: any[]) => {
        this.lugares = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR MIS LUGARES:', err);
        this.abrirPopup('No se pudieron cargar tus lugares.', 'error');
      }
    });
  }

  buscarLugares(): void {
    const texto = this.valorBusqueda.trim();

    if (!texto) {
      this.cargarMisLugares();
      return;
    }

    if (this.tipoBusqueda === 'direccion') {
      this.misLugaresService.getMisLugaresFiltrados(undefined, texto, undefined).subscribe({
        next: (data: any[]) => {
          this.lugares = data;
          this.cdr.detectChanges();
        },
        error: (err: any) => console.error('ERROR FILTRO DIRECCION MIS LUGARES:', err)
      });
      return;
    }

    if (this.tipoBusqueda === 'tipo') {
      this.misLugaresService.getMisLugaresFiltrados(undefined, undefined, texto).subscribe({
        next: (data: any[]) => {
          this.lugares = data;
          this.cdr.detectChanges();
        },
        error: (err: any) => console.error('ERROR FILTRO TIPO MIS LUGARES:', err)
      });
      return;
    }

    this.misLugaresService.getMisLugaresFiltrados(texto, undefined, undefined).subscribe({
      next: (data: any[]) => {
        this.lugares = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR FILTRO NOMBRE MIS LUGARES:', err)
    });
  }

  alCambiarBusqueda(): void {
    this.buscarLugares();
  }

  limpiarBusqueda(): void {
    this.tipoBusqueda = 'nombre';
    this.valorBusqueda = '';
    this.cargarMisLugares();
  }

  borrarLugar(id: number): void {
    this.misLugaresService.borrarLugar(id).subscribe({
      next: () => {
        this.abrirPopup('Lugar borrado correctamente.', 'error');
        this.cargarMisLugares();
      },
      error: () => {
        this.abrirPopup('No se pudo borrar el lugar.', 'error');
      }
    });
  }

  mostrarTipoLegible(tipo: string): string {
    if (!tipo) {
      return 'Sin tipo';
    }

    return tipo.replaceAll('_', ' ').toLowerCase()
      .replace(/\b\w/g, letra => letra.toUpperCase());
  }

  formatearUbicacion(latitud: number, longitud: number): string {
    if (latitud == null || longitud == null) {
      return 'Sin ubicación';
    }

    return `${latitud}, ${longitud}`;
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
