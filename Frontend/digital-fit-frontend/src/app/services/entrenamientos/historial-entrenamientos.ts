import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HistorialEntrenamientosService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/entrenamientos/mi-historial';

  getHistorial(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, {
      withCredentials: true
    });
  }

  getHistorialById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }

  crearRegistro(registro: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, registro, {
      withCredentials: true
    });
  }

  borrarRegistro(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }
}