import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';

@Component({
  selector: 'app-inicio',
  imports: [RouterLink, HeaderComponent, Footer],
  templateUrl: './inicio.html',
  styleUrl: './inicio.css'
})
export class InicioComponent {
}
