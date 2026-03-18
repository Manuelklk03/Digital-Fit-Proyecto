import { PrivadosService } from './../../services/centros/privados-service';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';


@Component({
  selector: 'app-privados',
  imports: [HeaderComponent, Footer, RouterLink],
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
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR PRIVADOS:', err);
      }
    });
  }
}
