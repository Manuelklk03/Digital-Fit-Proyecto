import { AuthService } from './../../../../services/auth-service';
import { AdminSoporteService } from './../../../../services/admin/admin-soporte-service';
import { Footer } from './../../../../components/footer/footer';
import { HeaderComponent } from './../../../../components/header/header';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-soporte',
  imports: [HeaderComponent, Footer, FormsModule],
  templateUrl: './soporte-admin.html',
  styleUrl: './soporte-admin.css'
})
export class AdminSoporteComponent {

  private adminSoporteService = inject(AdminSoporteService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  tickets: any[] = [];
  mensajeError = '';
  mensajeExito = '';

  ngOnInit(): void {
    this.authService.me().subscribe({
      next: (usuario) => {
        if (usuario?.rol !== 'ADMIN') {
          this.router.navigate(['/soporte']);
          return;
        }

        this.cargarTickets();
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });
  }

  cargarTickets(): void {
    this.adminSoporteService.getTicketsAdmin().subscribe({
      next: (data) => {
        console.log('TICKETS ADMIN:', data);
        this.tickets = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR TICKETS ADMIN:', err);
        this.mensajeError = 'No se pudieron cargar los tickets.';
      }
    });
  }

  cambiarEstado(id: number, estado: string): void {
    this.mensajeError = '';
    this.mensajeExito = '';

    this.adminSoporteService.cambiarEstado(id, estado).subscribe({
      next: () => {
        this.mensajeExito = 'Estado actualizado correctamente.';
        this.cargarTickets();
      },
      error: () => {
        this.mensajeError = 'No se pudo actualizar el estado.';
      }
    });
  }

  borrarTicket(id: number): void {
    this.mensajeError = '';
    this.mensajeExito = '';

    this.adminSoporteService.borrarTicketCerrado(id).subscribe({
      next: () => {
        this.mensajeExito = 'Ticket borrado correctamente.';
        this.cargarTickets();
      },
      error: () => {
        this.mensajeError = 'Solo se pueden borrar tickets cerrados.';
      }
    });
  }
}
