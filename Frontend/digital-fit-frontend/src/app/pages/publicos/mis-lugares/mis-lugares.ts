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

  mensajeExito = '';
  mensajeError = '';

  mostrarToast = false;
  textoToast = '';
  tipoToast: 'exito' | 'error' = 'exito';
  private toastTimeout: any;

  ngOnInit(): void {
    this.cargarMisLugares();
  }

  cargarMisLugares(): void {
    this.misLugaresService.getMisLugares().subscribe({
      next: (data) => {
        this.lugares = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR MIS LUGARES:', err);
        this.mensajeError = 'No se pudieron cargar tus lugares.';
        this.mostrarToastMensaje('No se pudieron cargar tus lugares.', 'error');
      }
    });
  }

  buscarLugares(): void {
    const texto = this.valorBusqueda.trim();

    if (!texto) {
      this.cargarMisLugares();
      return;
    }

    if (this.tipoBusqueda === 'tipo') {
      this.misLugaresService.getMisLugaresFiltrados(undefined, undefined, texto).subscribe({
        next: (data) => {
          this.lugares = data;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('ERROR FILTRO TIPO MIS LUGARES:', err);
        }
      });

      return;
    }

    if (this.tipoBusqueda === 'direccion') {
      this.misLugaresService.getMisLugaresFiltrados(undefined, texto, undefined).subscribe({
        next: (data) => {
          this.lugares = data;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('ERROR FILTRO DIRECCION MIS LUGARES:', err);
        }
      });

      return;
    }

    this.misLugaresService.getMisLugaresFiltrados(texto, undefined, undefined).subscribe({
      next: (data) => {
        this.lugares = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR FILTRO NOMBRE MIS LUGARES:', err);
      }
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
    this.mensajeExito = '';
    this.mensajeError = '';

    this.misLugaresService.borrarLugar(id).subscribe({
      next: () => {
        this.mensajeExito = 'Lugar borrado correctamente.';
        this.mostrarToastMensaje('Lugar borrado correctamente.', 'exito');
        this.cargarMisLugares();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el lugar.';
        this.mostrarToastMensaje('No se pudo borrar el lugar.', 'error');
      }
    });
  }

  hayCoordenadas(lugar: any): boolean {
    return lugar?.latitud !== null && lugar?.latitud !== undefined
      && lugar?.longitud !== null && lugar?.longitud !== undefined;
  }

  mostrarTipoLegible(tipo: string): string {
    if (!tipo) {
      return 'Sin tipo';
    }

    return tipo.replaceAll('_', ' ').toLowerCase()
      .replace(/\b\w/g, (letra) => letra.toUpperCase());
  }

  mostrarToastMensaje(texto: string, tipo: 'exito' | 'error'): void {
    this.textoToast = texto;
    this.tipoToast = tipo;
    this.mostrarToast = true;

    if (this.toastTimeout) {
      clearTimeout(this.toastTimeout);
    }

    this.toastTimeout = setTimeout(() => {
      this.mostrarToast = false;
    }, 3000);
  }

  cerrarToast(): void {
    this.mostrarToast = false;
  }
}
