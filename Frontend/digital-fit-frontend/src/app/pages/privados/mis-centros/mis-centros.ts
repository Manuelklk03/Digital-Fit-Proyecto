import { MisCentrosService } from './../../../services/centros/mis-centros-service';
import { Footer } from './../../../components/footer/footer';
import { HeaderComponent } from './../../../components/header/header';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-mis-centros',
  imports: [HeaderComponent, Footer, FormsModule, RouterLink],
  templateUrl: './mis-centros.html',
  styleUrl: './mis-centros.css'
})
export class MisCentrosComponent {

  private misCentrosService = inject(MisCentrosService);
  private cdr = inject(ChangeDetectorRef);

  centros: any[] = [];

  tipoBusqueda = 'nombre';
  valorBusqueda = '';

  mensajeExito = '';
  mensajeError = '';

  mostrarToast = false;
  textoToast = '';
  tipoToast: 'exito' | 'error' = 'exito';
  private toastTimeout: any;

  ngOnInit(): void {
    this.cargarMisCentros();
  }

  cargarMisCentros(): void {
    this.misCentrosService.getMisCentros().subscribe({
      next: (data) => {
        this.centros = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR MIS CENTROS:', err);
        this.mensajeError = 'No se pudieron cargar tus centros.';
        this.mostrarToastMensaje('No se pudieron cargar tus centros.', 'error');
      }
    });
  }

  buscarCentros(): void {
    const texto = this.valorBusqueda.trim();

    if (!texto) {
      this.cargarMisCentros();
      return;
    }

    if (this.tipoBusqueda === 'precioMensual') {
      const precio = Number(texto);

      if (isNaN(precio)) {
        this.centros = [];
        return;
      }

      this.misCentrosService.getMisCentrosFiltrados(undefined, undefined, precio).subscribe({
        next: (data) => {
          this.centros = data;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('ERROR FILTRO PRECIO MIS CENTROS:', err);
        }
      });

      return;
    }

    if (this.tipoBusqueda === 'direccion') {
      this.misCentrosService.getMisCentrosFiltrados(undefined, texto, undefined).subscribe({
        next: (data) => {
          this.centros = data;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('ERROR FILTRO DIRECCION MIS CENTROS:', err);
        }
      });

      return;
    }

    this.misCentrosService.getMisCentrosFiltrados(texto, undefined, undefined).subscribe({
      next: (data) => {
        this.centros = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR FILTRO NOMBRE MIS CENTROS:', err);
      }
    });
  }

  alCambiarBusqueda(): void {
    this.buscarCentros();
  }

  limpiarBusqueda(): void {
    this.tipoBusqueda = 'nombre';
    this.valorBusqueda = '';
    this.cargarMisCentros();
  }

  borrarCentro(id: number): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    this.misCentrosService.borrarCentro(id).subscribe({
      next: () => {
        this.mensajeExito = 'Centro borrado correctamente.';
        this.mostrarToastMensaje('Centro borrado correctamente.', 'exito');
        this.cargarMisCentros();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el centro.';
        this.mostrarToastMensaje('No se pudo borrar el centro.', 'error');
      }
    });
  }

  hayCoordenadas(centro: any): boolean {
    return centro?.latitud !== null && centro?.latitud !== undefined
      && centro?.longitud !== null && centro?.longitud !== undefined;
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
