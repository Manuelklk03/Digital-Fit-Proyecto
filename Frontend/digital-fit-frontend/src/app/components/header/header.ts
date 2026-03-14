import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class HeaderComponent {

  private authService = inject(AuthService);
  private router = inject(Router);

  usuarioActual: string | null = null;

  ngOnInit(): void {
    this.cargarUsuario();
  }

  cargarUsuario(): void {
    this.authService.yo().subscribe({
      next: (respuesta) => {
        this.usuarioActual = respuesta;
      },
      error: () => {
        this.usuarioActual = null;
      }
    });
  }

  cerrarSesion(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.usuarioActual = null;
        this.router.navigate(['/login']);
      },
      error: () => {
        this.usuarioActual = null;
        this.router.navigate(['/login']);
      }
    });
  }
}
