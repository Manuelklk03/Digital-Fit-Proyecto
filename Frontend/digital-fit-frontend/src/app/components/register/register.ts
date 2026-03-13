import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {

  username: string = '';
  email: string = '';
  password: string = '';
  mensaje: string = '';

  private authService = inject(AuthService);
  private router = inject(Router);

  registrar(): void {

    const usuario = {
      username: this.username,
      email: this.email,
      password: this.password,
      rol: 'USER'
    };

    this.authService.registrar(usuario).subscribe({
      next: (res) => {
        this.mensaje = res;
        this.router.navigate(['/']);
      },
      error: () => {
        this.mensaje = 'Error al registrar usuario';
      }
    });

  }

}