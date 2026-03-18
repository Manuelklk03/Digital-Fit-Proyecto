import { EntrenamientosService } from './../../services/entrenamientos/entrenamientos-service';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';


@Component({
  selector: 'app-entrenamientos',
  imports: [HeaderComponent, Footer, RouterLink],
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
