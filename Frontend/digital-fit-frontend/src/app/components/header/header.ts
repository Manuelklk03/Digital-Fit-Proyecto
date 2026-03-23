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

  usuarioActual: any = null;

  ngOnInit(): void {
    this.authService.usuario$.subscribe(usuario => {
      this.usuarioActual = usuario;
    });

    this.authService.me().subscribe({
      error: () => {
        this.usuarioActual = null;
      }
    });
  }

  cerrarSesion(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      }
    });
  }

  esAdmin(): boolean {
    return this.usuarioActual?.rol === 'ADMIN';
  }

  iconoUsuario(): string {
    return this.esAdmin() ? '🛡️' : '👤';
  }
}
