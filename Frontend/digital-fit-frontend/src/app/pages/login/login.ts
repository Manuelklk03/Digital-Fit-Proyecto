import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink, HeaderComponent, Footer],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {

  private authService = inject(AuthService);
  private router = inject(Router);

  username = '';
  password = '';
  mensajeError = '';

  iniciarSesion(): void {
    this.mensajeError = '';

    if (!this.username.trim() || !this.password.trim()) {
      this.mensajeError = 'Debes completar todos los campos.';
      return;
    }

    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.router.navigate(['/inicio']);
      },
      error: () => {
        this.mensajeError = 'Usuario o contraseña incorrectos.';
      }
    });
  }
}
