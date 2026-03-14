import { Component } from '@angular/core';
import { HeaderComponent } from '../../components/header/header';
import { Footer} from '../../components/footer/footer';

@Component({
  selector: 'app-publicos',
  imports: [HeaderComponent, Footer],
  templateUrl: './publicos.html',
  styleUrl: './publicos.css'
})
export class PublicosComponent {
}
