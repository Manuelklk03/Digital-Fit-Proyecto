import { EntrenamientosService } from './../../services/entrenamientos/entrenamientos-service';
import { Component, inject } from '@angular/core';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-entrenamientos',
  imports: [HeaderComponent, Footer],
  templateUrl: './entrenamientos.html',
  styleUrl: './entrenamientos.css'
})
export class EntrenamientosComponent {

  private entrenamientosService = inject(EntrenamientosService);
  private cdr = inject(ChangeDetectorRef);

  entrenamientos: any[] = [];

  ngOnInit(): void {
    this.entrenamientosService.getEntrenamientos().subscribe({
      next: (data) => {
        console.log('DATOS ENTRENAMIENTOS:', data);

        this.entrenamientos = data;

        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR ENTRENAMIENTOS:', err);
      }
    });
  }
}
