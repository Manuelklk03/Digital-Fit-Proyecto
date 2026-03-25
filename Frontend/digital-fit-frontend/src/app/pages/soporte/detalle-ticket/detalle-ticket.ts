import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HeaderComponent } from '../../../components/header/header';
import { Footer } from '../../../components/footer/footer';
import { SoporteService } from '../../../services/soporte';
import { AuthService } from '../../../services/auth-service';

@Component({
  selector: 'app-detalle-ticket',
  imports: [HeaderComponent, Footer],
  templateUrl: './detalle-ticket.html',
  styleUrl: './detalle-ticket.css'
})
export class DetalleTicketComponent {

  private route = inject(ActivatedRoute);
  private soporteService = inject(SoporteService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  ticket: any = null;

  ngOnInit(): void {
    this.authService.me().subscribe({
      next: (usuario) => {
        if (usuario?.rol === 'ADMIN') {
          this.router.navigate(['/admin/soporte']);
          return;
        }

        const id = Number(this.route.snapshot.paramMap.get('id'));

        this.soporteService.getTicketById(id).subscribe({
          next: (data) => {
            this.ticket = data;
            this.cdr.detectChanges();
          },
          error: (err) => {
            console.error('ERROR DETALLE TICKET:', err);
          }
        });
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });
  }
}
