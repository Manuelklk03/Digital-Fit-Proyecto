import { SoporteService } from './../../services/soporte';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-soporte',
  imports: [HeaderComponent, Footer, FormsModule],
  templateUrl: './soporte.html',
  styleUrl: './soporte.css'
})
export class SoporteComponent {

  private soporteService = inject(SoporteService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  asunto = '';
  mensaje = '';
  mensajeExito = '';
  mensajeError = '';

  tickets: any[] = [];
  usuarioActual: any = null;

  ngOnInit(): void {
    this.authService.me().subscribe({
      next: (usuario) => {
        this.usuarioActual = usuario;

        if (usuario?.rol === 'ADMIN') {
          this.router.navigate(['/admin/soporte']);
          return;
        }

        this.cargarTickets();
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });
  }

  enviarFormulario(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    if (!this.asunto.trim() || !this.mensaje.trim()) {
      this.mensajeError = 'Debes completar todos los campos.';
      return;
    }

    const nuevoTicket = {
      asunto: this.asunto,
      mensaje: this.mensaje
    };

    this.soporteService.crearTicket(nuevoTicket).subscribe({
      next: () => {
        this.mensajeExito = 'Ticket enviado correctamente.';
        this.asunto = '';
        this.mensaje = '';
        this.cargarTickets();
      },
      error: () => {
        this.mensajeError = 'No se pudo enviar el ticket.';
      }
    });
  }

  cargarTickets(): void {
    this.soporteService.getMisTickets().subscribe({
      next: (data) => {
        console.log('MIS TICKETS:', data);
        this.tickets = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR TICKETS:', err);
      }
    });
  }

  borrarTicket(id: number): void {
    this.soporteService.borrarTicket(id).subscribe({
      next: () => {
        this.cargarTickets();
      },
      error: () => {
        this.mensajeError = 'No se pudo borrar el ticket.';
      }
    });
  }
}
