import { PublicosService } from './../../services/publicos/publicos-service';
import { Component, inject } from '@angular/core';
import { HeaderComponent } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-publicos',
  imports: [HeaderComponent, Footer],
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

        // 🔥 igual que privados
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('ERROR PUBLICOS:', err);
      }
    });
  }
}
