import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-inicio',
  imports: [HeaderComponent, Footer, RouterLink],
  templateUrl: './inicio.html',
  styleUrl: './inicio.css'
})
export class InicioComponent {

  private authService = inject(AuthService);
  private router = inject(Router);

  usuarioActual: any = null;
  cargando = true;

  ngOnInit(): void {
    this.authService.me().subscribe({
      next: (usuario) => {
        this.usuarioActual = usuario;
        this.cargando = false;
      },
      error: () => {
        this.cargando = false;
        this.router.navigate(['/login']);
      }
    });
  }

  esAdmin(): boolean {
    return this.usuarioActual?.rol === 'ADMIN';
  }

  esUser(): boolean {
    return this.usuarioActual?.rol === 'USER';
  }
}
