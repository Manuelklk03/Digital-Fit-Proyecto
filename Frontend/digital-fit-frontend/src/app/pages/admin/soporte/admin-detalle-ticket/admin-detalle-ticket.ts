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
  mensajeError = '';
  mensajeExito = '';

  ngOnInit(): void {
    this.authService.me().subscribe({
      next: (usuario) => {
        if (usuario?.rol !== 'ADMIN') {
          this.router.navigate(['/soporte']);
          return;
        }

        const id = Number(this.route.snapshot.paramMap.get('id'));

        this.adminSoporteService.getTicketsAdmin(id).subscribe({
          next: (data) => {
            this.ticket = data;
            this.cdr.detectChanges();
          },
          error: (err) => {
            console.error('ERROR DETALLE TICKET ADMIN:', err);
          }
        });
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });
  }

  cambiarEstado(estado: string): void {
    if (!this.ticket) return;

    this.mensajeError = '';
    this.mensajeExito = '';

    this.adminSoporteService.cambiarEstado(this.ticket.id, estado).subscribe({
      next: (data) => {
        this.ticket = data;
        this.mensajeExito = 'Estado actualizado correctamente.';
        this.cdr.detectChanges();
      },
      error: () => {
        this.mensajeError = 'No se pudo actualizar el estado.';
      }
    });
  }

  borrarTicket(): void {
    if (!this.ticket) return;

    this.mensajeError = '';
    this.mensajeExito = '';

    this.adminSoporteService.borrarTicketCerrado(this.ticket.id).subscribe({
      next: () => {
        this.router.navigate(['/admin/soporte']);
      },
      error: () => {
        this.mensajeError = 'Solo se pueden borrar tickets cerrados.';
      }
    });
  }
}
