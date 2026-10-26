import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HeaderComponent } from '../../../../components/header/header';
import { Footer } from '../../../../components/footer/footer';
import { AdminSoporteService } from '../../../../services/admin/admin-soporte-service';
import { AuthService } from '../../../../services/auth-service';

@Component({
  selector: 'app-admin-detalle-ticket',
  imports: [HeaderComponent, Footer],
  templateUrl: './admin-detalle-ticket.html',
  styleUrl: './admin-detalle-ticket.css'
})
export class AdminDetalleTicketComponent {

  private route = inject(ActivatedRoute);
  private adminSoporteService = inject(AdminSoporteService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  ticket: any = null;

  mostrarPopup = false;
  textoPopup = '';
  tipoPopup: 'exito' | 'error' = 'exito';
  private popupTimeout: any;

  ngOnInit(): void {
    this.authService.me().subscribe({
      next: (usuario: any) => {
        if (usuario?.rol !== 'ADMIN') {
          this.router.navigate(['/inicio']);
          return;
        }

        const id = Number(this.route.snapshot.paramMap.get('id'));

        this.adminSoporteService.getTicketAdminById(id).subscribe({
          next: (data: any) => {
            this.ticket = data;
            this.cdr.detectChanges();
          },
          error: (err: any) => {
            console.error('ERROR DETALLE TICKET ADMIN:', err);
            this.abrirPopup('No se pudo cargar el ticket.', 'error');
          }
        });
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });
  }

  cambiarEstado(estado: string): void {
    if (!this.ticket) {
      return;
    }

    this.adminSoporteService.cambiarEstado(this.ticket.id, estado).subscribe({
      next: (data: any) => {
        this.ticket = data;
        this.abrirPopup('Estado actualizado correctamente.', 'exito');
        this.cdr.detectChanges();
      },
      error: () => {
        this.abrirPopup('No se pudo actualizar el estado.', 'error');
      }
    });
  }

  borrarTicket(): void {
    if (!this.ticket) {
      return;
    }

    this.adminSoporteService.borrarTicketCerrado(this.ticket.id).subscribe({
      next: () => {
        this.abrirPopup('Ticket borrado correctamente.', 'error');
        setTimeout(() => {
          this.router.navigate(['/admin/soporte']);
        }, 900);
      },
      error: () => {
        this.abrirPopup('Solo se pueden borrar tickets cerrados.', 'error');
      }
    });
  }

  mostrarEstado(estado: string): string {
    switch (estado) {
      case 'ABIERTO':
        return 'Abierto';
      case 'EN_PROCESO':
        return 'En proceso';
      case 'CERRADO':
        return 'Cerrado';
      default:
        return estado;
    }
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
