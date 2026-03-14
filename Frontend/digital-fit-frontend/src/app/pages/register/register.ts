import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HeaderComponent } from '../../components/header/header';
import { Footer} from '../../components/footer/footer';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink, HeaderComponent, Footer],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {

  private authService = inject(AuthService);
  private router = inject(Router);

  username = '';
  email = '';
  password = '';
  mensaje = '';
  error = '';

  registrar(): void {
    this.mensaje = '';
    this.error = '';

    if (!this.username.trim() || !this.email.trim() || !this.password.trim()) {
      this.error = 'Debes completar todos los campos.';
      return;
    }

    const nuevoUsuario = {
      username: this.username,
      email: this.email,
      password: this.password,
      rol: 'USER'
    };

    this.authService.registrar(nuevoUsuario).subscribe({
      next: (respuesta) => {
        this.mensaje = respuesta;
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1200);
      },
      error: () => {
        this.error = 'No se pudo registrar el usuario.';
      }
    });
  }
}
