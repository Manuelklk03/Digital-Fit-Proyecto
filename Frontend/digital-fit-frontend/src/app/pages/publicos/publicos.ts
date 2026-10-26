import { PublicosService } from './../../services/publicos/publicos-service';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';


@Component({
  selector: 'app-publicos',
  imports: [HeaderComponent, Footer, RouterLink],
  templateUrl: './publicos.html',
  styleUrl: './publicos.css'
})
export class PublicosComponent {

  private publicosService = inject(PublicosService);
  private cdr = inject(ChangeDetectorRef);

  publicos: any[] = [];

  ngOnInit(): void {
    this.publicosService.getPublicos().subscribe({
      next: (data) => {
        console.log('DATOS PUBLICOS:', data);
        this.publicos = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR PUBLICOS:', err);
      }
    });
  }
}
