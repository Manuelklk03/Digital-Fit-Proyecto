import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class HeaderComponent implements OnInit {

  usuario: string | null = null;

  private authService = inject(AuthService);

  ngOnInit(): void {

    this.authService.yo().subscribe({
      next: (res) => {
        this.usuario = res;
      },
      error: () => {
        this.usuario = null;
      }
    });

  }

  logout(): void {

    this.authService.logout().subscribe(() => {
      location.reload();
    });

  }

}