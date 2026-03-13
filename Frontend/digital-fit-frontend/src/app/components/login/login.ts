import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent implements OnInit {

  username: string = '';
  password: string = '';
  mensaje: string = '';

  private authService = inject(AuthService);
  private router = inject(Router);

  ngOnInit(): void {

    this.authService.yo().subscribe({
      next: () => {
        this.router.navigate(['/inicio']);
      },
      error: () => {
        // no hay sesión, seguimos en login
      }
    });

  }

  login(): void {

    this.mensaje = '';

    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.router.navigate(['/inicio']);
      },
      error: () => {
        this.mensaje = 'Usuario o contraseña incorrectos';
      }
    });

  }

}