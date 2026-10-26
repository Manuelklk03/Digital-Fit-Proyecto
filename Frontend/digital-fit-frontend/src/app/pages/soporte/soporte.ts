import { Component } from '@angular/core';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';

@Component({
  selector: 'app-soporte',
  imports: [HeaderComponent, Footer],
  templateUrl: './soporte.html',
  styleUrl: './soporte.css'
})
export class SoporteComponent {
}
