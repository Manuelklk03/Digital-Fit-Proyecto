import { Component } from '@angular/core';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';

@Component({
  selector: 'app-privados',
  imports: [HeaderComponent, Footer],
  templateUrl: './privados.html',
  styleUrl: './privados.css'
})
export class PrivadosComponent {
}
