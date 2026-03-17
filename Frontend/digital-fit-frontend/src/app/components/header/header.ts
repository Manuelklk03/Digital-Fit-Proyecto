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

    // 🔥 escucha cambios de login en tiempo real
    this.authService.usuario$.subscribe(usuario => {
      this.usuarioActual = usuario;
    });

    // 🔥 al cargar la app intenta recuperar sesión
    this.authService.yo().subscribe({
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
}
