import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EstadisticasService {

  private http = inject(HttpClient);
  private apiUrl = '/api/estadisticas';

  getEstadisticas(): Observable<any> {
    return this.http.get<any>(this.apiUrl, {
      withCredentials: true
    });
  }
}