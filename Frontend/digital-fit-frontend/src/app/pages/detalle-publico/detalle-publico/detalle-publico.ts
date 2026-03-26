import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HeaderComponent } from '../../../components/header/header';
import { Footer } from '../../../components/footer/footer';
import { PublicosService } from '../../../services/publicos/publicos-service';
import { MisLugaresService } from '../../../services/publicos/mis-lugares';
import { AdminLugaresPublicosBaseService } from '../../../services/admin/admin-lugarespublicos-base';
import { AuthService } from '../../../services/auth-service';

@Component({
  selector: 'app-detalle-publico',
  imports: [HeaderComponent, Footer, RouterLink],
  templateUrl: './detalle-publico.html',
  styleUrl: './detalle-publico.css'
})
export class DetallePublicoComponent {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  private publicosService = inject(PublicosService);
  private misLugaresService = inject(MisLugaresService);
  private adminLugaresPublicosBaseService = inject(AdminLugaresPublicosBaseService);
  private authService = inject(AuthService);

  lugar: any = null;
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

    this.publicosService.getLugarPublicoById(id).subscribe({
      next: (data: any) => {
        this.lugar = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('ERROR DETALLE LUGAR PUBLICO:', err);
        this.mensajeError = 'No se pudo cargar el detalle del lugar.';
      }
    });
  }

  esAdmin(): boolean {
    return this.usuarioActual?.rol === 'ADMIN';
  }

  anadirAMisLugares(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    if (!this.lugar?.id) {
      return;
    }

    this.misLugaresService.anadirLugarDesdeBase(this.lugar.id).subscribe({
      next: () => {
        this.mensajeExito = 'Lugar añadido a mis lugares.';
      },
      error: () => {
        this.mensajeError = 'No se pudo añadir a mis lugares.';
      }
    });
  }

  borrarLugarBase(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    if (!this.lugar?.id) {
      return;
    }

    this.adminLugaresPublicosBaseService.borrarLugarPublicoBase(this.lugar.id).subscribe({
      next: () => {
        this.router.navigate(['/publicos']);
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el lugar público base.';
      }
    });
  }
}
