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

  mostrarPopup = false;
  textoPopup = '';
  tipoPopup: 'exito' | 'error' = 'exito';
  private popupTimeout: any;

  ngOnInit(): void {
    this.cargarMisCentros();
  }

  cargarMisCentros(): void {
    this.misCentrosService.getMisCentros().subscribe({
      next: (data: any[]) => {
        this.centros = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR MIS CENTROS:', err);
        this.abrirPopup('No se pudieron cargar tus centros.', 'error');
      }
    });
  }

  buscarCentros(): void {
    const texto = this.valorBusqueda.trim();

    if (!texto) {
      this.cargarMisCentros();
      return;
    }

    if (this.tipoBusqueda === 'direccion') {
      this.misCentrosService.getMisCentrosFiltrados(undefined, texto, undefined).subscribe({
        next: (data: any[]) => {
          this.centros = data;
          this.cdr.detectChanges();
        },
        error: (err: any) => console.error('ERROR FILTRO DIRECCION MIS CENTROS:', err)
      });
      return;
    }

    if (this.tipoBusqueda === 'precioMensual') {
      const precio = Number(texto);

      if (isNaN(precio)) {
        this.centros = [];
        return;
      }

      this.misCentrosService.getMisCentrosFiltrados(undefined, undefined, precio).subscribe({
        next: (data: any[]) => {
          this.centros = data;
          this.cdr.detectChanges();
        },
        error: (err: any) => console.error('ERROR FILTRO PRECIO MIS CENTROS:', err)
      });
      return;
    }

    this.misCentrosService.getMisCentrosFiltrados(texto, undefined, undefined).subscribe({
      next: (data: any[]) => {
        this.centros = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR FILTRO NOMBRE MIS CENTROS:', err)
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
    this.misCentrosService.borrarCentro(id).subscribe({
      next: () => {
        this.abrirPopup('Centro borrado correctamente.', 'error');
        this.cargarMisCentros();
      },
      error: () => {
        this.abrirPopup('No se pudo borrar el centro.', 'error');
      }
    });
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
