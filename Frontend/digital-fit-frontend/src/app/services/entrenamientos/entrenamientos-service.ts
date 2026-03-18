import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EntrenamientosService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/entrenamientos';

  getEntrenamientos(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, {
      withCredentials: true
    });
  }
}
