import { Component } from '@angular/core';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';

@Component({
  selector: 'app-valoraciones',
  imports: [HeaderComponent, Footer],
  templateUrl: './valoraciones.html',
  styleUrl: './valoraciones.css'
})
export class ValoracionesComponent {
}
