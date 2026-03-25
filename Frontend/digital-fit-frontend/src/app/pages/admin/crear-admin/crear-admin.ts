import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HeaderComponent } from '../../../components/header/header';
import { Footer } from '../../../components/footer/footer';
import { AuthService } from '../../../services/auth-service';
import { AdminService } from '../../../services/admin/admin-service';

@Component({
  selector: 'app-crear-admin',
  imports: [HeaderComponent, Footer, FormsModule],
  templateUrl: './crear-admin.html',
  styleUrl: './crear-admin.css'
})
export class CrearAdminComponent {

  private authService = inject(AuthService);
  private adminService = inject(AdminService);
  private router = inject(Router);

  username = '';
  email = '';
  password = '';

  mensajeExito = '';
  mensajeError = '';

  ngOnInit(): void {
    this.authService.me().subscribe({
      next: (usuario) => {
        if (usuario?.rol !== 'ADMIN') {
          this.router.navigate(['/inicio']);
        }
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });
  }

  crearAdmin(): void {
    this.mensajeExito = '';
    this.mensajeError = '';

    if (!this.username.trim() || !this.email.trim() || !this.password.trim()) {
      this.mensajeError = 'Debes completar todos los campos.';
      return;
    }

    const nuevoAdmin = {
      username: this.username,
      email: this.email,
      password: this.password
    };

    this.adminService.crearAdmin(nuevoAdmin).subscribe({
      next: (respuesta) => {
        this.mensajeExito = respuesta;
        this.username = '';
        this.email = '';
        this.password = '';
      },
      error: () => {
        this.mensajeError = 'No se pudo crear el admin.';
      }
    });
  }
}
