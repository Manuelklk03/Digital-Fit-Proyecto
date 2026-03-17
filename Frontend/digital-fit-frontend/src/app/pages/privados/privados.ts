import { PrivadosService } from './../../services/centros/privados-service';
import { Component, inject } from '@angular/core';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-privados',
  imports: [HeaderComponent, Footer],
  templateUrl: './privados.html',
  styleUrl: './privados.css'
})
export class PrivadosComponent {

  private privadosService = inject(PrivadosService);
  private cdr = inject(ChangeDetectorRef);

  privados: any[] = [];

  ngOnInit(): void {
    this.privadosService.getPrivados().subscribe({
      next: (data) => {
        console.log('DATOS PRIVADOS:', data);

        this.privados = data;

        // 🔥 CLAVE: forzar refresco de la vista
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR PRIVADOS:', err);
      }
    });
  }
}
