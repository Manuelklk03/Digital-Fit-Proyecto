import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EntrenamientosComunidadService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/entrenamientos-comunidad';

  getEntrenamientosComunidad(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, {
      withCredentials: true
    });
  }

  getEntrenamientoComunidadById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }

  crearEntrenamientoComunidad(entrenamiento: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, entrenamiento, {
      withCredentials: true
    });
  }
}