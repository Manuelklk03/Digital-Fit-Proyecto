import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HeaderComponent } from '../../../components/header/header';
import { Footer } from '../../../components/footer/footer';
import { EntrenamientosService } from '../../../services/entrenamientos/entrenamientos-service';
import { MisEntrenamientosService } from '../../../services/entrenamientos/mis-entrenamientos-service';
import { AdminEntrenamientosBaseService } from '../../../services/admin/admin-entrenamiento-base';
import { AuthService } from '../../../services/auth-service';

@Component({
  selector: 'app-detalle-entrenamiento-base',
  imports: [HeaderComponent, Footer, RouterLink],
  templateUrl: './detalle-entrenamiento-base.html',
  styleUrl: './detalle-entrenamiento-base.css'
})
export class DetalleEntrenamientoComponent {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  private entrenamientosService = inject(EntrenamientosService);
  private misEntrenamientosService = inject(MisEntrenamientosService);
  private adminEntrenamientosBaseService = inject(AdminEntrenamientosBaseService);
  private authService = inject(AuthService);

  entrenamiento: any = null;
  usuarioActual: any = null;

  mensajeExito = '';
  mensajeError = '';

  ngOnInit(): void {
    this.authService.me().subscribe({
      next: (usuario: any) => {
        this.usuarioActual = usuario;
        this.cargarDetalle();
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });
  }

  cargarDetalle(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.entrenamientosService.getEntrenamientoById(id).subscribe({
      next: (data: any) => {
        this.entrenamiento = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR DETALLE ENTRENAMIENTO BASE:', err);
        this.mensajeError = 'No se pudo cargar el detalle del entrenamiento.';
      }
    });
  }

  esAdmin(): boolean {
    return this.usuarioActual?.rol === 'ADMIN';
  }

  anadirAMisEntrenamientos(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    if (!this.entrenamiento?.id) {
      return;
    }

    this.misEntrenamientosService.anadirDesdeBase(this.entrenamiento.id).subscribe({
      next: () => {
        this.mensajeExito = 'Entrenamiento añadido a mis entrenamientos.';
      },
      error: () => {
        this.mensajeError = 'No se pudo añadir a mis entrenamientos.';
      }
    });
  }

  borrarEntrenamientoBase(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    if (!this.entrenamiento?.id) {
      return;
    }

    this.adminEntrenamientosBaseService.borrarEntrenamientoBase(this.entrenamiento.id).subscribe({
      next: () => {
        this.router.navigate(['/entrenamientos']);
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el entrenamiento base.';
      }
    });
  }
}
