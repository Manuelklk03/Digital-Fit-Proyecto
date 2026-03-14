import { Component } from '@angular/core';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';

@Component({
  selector: 'app-entrenamientos',
  imports: [HeaderComponent, Footer],
  templateUrl: './entrenamientos.html',
  styleUrl: './entrenamientos.css'
})
export class EntrenamientosComponent {
}
