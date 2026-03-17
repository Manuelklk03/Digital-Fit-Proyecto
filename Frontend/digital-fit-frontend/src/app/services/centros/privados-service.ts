import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PrivadosService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/centros-privados';

  getPrivados(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, {
      withCredentials: true
    });
  }
}
