import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { PrivadosService } from '../../services/centros/privados-service';
import { MisCentrosService } from '../../services/centros/mis-centros-service';
import { AdminCentrosPrivadosBaseService } from '../../services/admin/admin-centro-base';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-privados',
  imports: [HeaderComponent, Footer, FormsModule, RouterLink],
  templateUrl: './privados.html',
  styleUrl: './privados.css'
})
export class PrivadosComponent {

  private privadosService = inject(PrivadosService);
  private misCentrosService = inject(MisCentrosService);
  private adminCentrosPrivadosBaseService = inject(AdminCentrosPrivadosBaseService);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  centros: any[] = [];
  usuarioActual: any = null;

  nombre = '';
  direccion = '';
  telefono = '';
  horario = '';
  precioMensual = 30;
  descripcion = '';
  latitud = 0;
  longitud = 0;

  editandoId: number | null = null;

  tipoBusqueda = 'nombre';
  valorBusqueda = '';

  mostrarPopup = false;
  textoPopup = '';
  tipoPopup: 'exito' | 'error' = 'exito';
  private popupTimeout: any;

  ngOnInit(): void {
    this.authService.me().subscribe({
      next: (usuario: any) => {
        this.usuarioActual = usuario;
        this.cargarCentros();
      },
      error: () => {
        this.cargarCentros();
      }
    });
  }

  cargarCentros(): void {
    this.privadosService.getCentrosPrivados().subscribe({
      next: (data: any[]) => {
        this.centros = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR CENTROS PRIVADOS BASE:', err);
        this.abrirPopup('No se pudieron cargar los centros privados base.', 'error');
      }
    });
  }

  esAdmin(): boolean {
    return this.usuarioActual?.rol === 'ADMIN';
  }

  buscarCentros(): void {
    const texto = this.valorBusqueda.trim();

    if (!texto) {
      this.cargarCentros();
      return;
    }

    if (this.tipoBusqueda === 'direccion') {
      this.privadosService.getCentrosPrivadosFiltrados(undefined, texto, undefined).subscribe({
        next: (data: any[]) => {
          this.centros = data;
          this.cdr.detectChanges();
        },
        error: (err: any) => console.error('ERROR FILTRO DIRECCION CENTROS BASE:', err)
      });
      return;
    }

    if (this.tipoBusqueda === 'precioMensual') {
      const precio = Number(texto);

      if (isNaN(precio)) {
        this.centros = [];
        return;
      }

      this.privadosService.getCentrosPrivadosFiltrados(undefined, undefined, precio).subscribe({
        next: (data: any[]) => {
          this.centros = data;
          this.cdr.detectChanges();
        },
        error: (err: any) => console.error('ERROR FILTRO PRECIO CENTROS BASE:', err)
      });
      return;
    }

    this.privadosService.getCentrosPrivadosFiltrados(texto, undefined, undefined).subscribe({
      next: (data: any[]) => {
        this.centros = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('ERROR FILTRO NOMBRE CENTROS BASE:', err)
    });
  }

  alCambiarBusqueda(): void {
    this.buscarCentros();
  }

  limpiarBusqueda(): void {
    this.tipoBusqueda = 'nombre';
    this.valorBusqueda = '';
    this.cargarCentros();
  }

  guardarCentroBase(): void {
    const payload = {
      nombre: this.nombre,
      direccion: this.direccion,
      telefono: this.telefono,
      horario: this.horario,
      precioMensual: this.precioMensual,
      descripcion: this.descripcion,
      latitud: this.latitud,
      longitud: this.longitud
    };

    if (this.editandoId !== null) {
      this.adminCentrosPrivadosBaseService.actualizarCentroPrivadoBase(this.editandoId, payload).subscribe({
        next: () => {
          this.abrirPopup('Centro privado base actualizado correctamente.', 'exito');
          this.limpiarFormulario();
          this.cargarCentros();
        },
        error: () => {
          this.abrirPopup('No se pudo actualizar el centro privado base.', 'error');
        }
      });
    } else {
      this.adminCentrosPrivadosBaseService.crearCentroPrivadoBase(payload).subscribe({
        next: () => {
          this.abrirPopup('Centro privado base creado correctamente.', 'exito');
          this.limpiarFormulario();
          this.cargarCentros();
        },
        error: () => {
          this.abrirPopup('No se pudo crear el centro privado base.', 'error');
        }
      });
    }
  }

  editarCentroBase(centro: any): void {
    this.editandoId = centro.id;
    this.nombre = centro.nombre;
    this.direccion = centro.direccion;
    this.telefono = centro.telefono;
    this.horario = centro.horario;
    this.precioMensual = centro.precioMensual;
    this.descripcion = centro.descripcion;
    this.latitud = centro.latitud ?? 0;
    this.longitud = centro.longitud ?? 0;
  }

  borrarCentroBase(id: number): void {
    this.adminCentrosPrivadosBaseService.borrarCentroPrivadoBase(id).subscribe({
      next: () => {
        this.abrirPopup('Centro privado base borrado correctamente.', 'exito');
        this.cargarCentros();
      },
      error: () => {
        this.abrirPopup('No se pudo borrar el centro privado base.', 'error');
      }
    });
  }

  anadirAMisCentros(id: number): void {
    this.misCentrosService.anadirCentroDesdeApp(id).subscribe({
      next: () => {
        this.abrirPopup('Centro añadido a mis centros.', 'exito');
      },
      error: () => {
        this.abrirPopup('No se pudo añadir a mis centros.', 'error');
      }
    });
  }

  cancelarEdicion(): void {
    this.limpiarFormulario();
  }

  limpiarFormulario(): void {
    this.editandoId = null;
    this.nombre = '';
    this.direccion = '';
    this.telefono = '';
    this.horario = '';
    this.precioMensual = 30;
    this.descripcion = '';
    this.latitud = 0;
    this.longitud = 0;
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