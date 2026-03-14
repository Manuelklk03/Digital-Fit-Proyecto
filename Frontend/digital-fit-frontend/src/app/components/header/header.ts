import { AuthService } from './../../services/auth-service';
import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';


@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class HeaderComponent implements OnInit {

  private authService = inject(AuthService);
  private router = inject(Router);

  usuario: string | null = null;

  ngOnInit(): void {
    this.cargarUsuario();
  }

  cargarUsuario(): void {
    this.authService.yo().subscribe({
      next: (respuesta) => {
        this.usuario = respuesta;
      },
      error: () => {
        this.usuario = null;
      }
    });
  }

  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.usuario = null;
        this.router.navigate(['/login']);
      },
      error: () => {
        this.usuario = null;
        this.router.navigate(['/login']);
      }
    });
  }
}
