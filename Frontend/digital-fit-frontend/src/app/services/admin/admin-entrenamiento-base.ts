import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminEntrenamientosBaseService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/admin/entrenamientos-base';

  crearEntrenamientoBase(entrenamiento: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, entrenamiento, {
      withCredentials: true
    });
  }

  actualizarEntrenamientoBase(id: number, entrenamiento: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, entrenamiento, {
      withCredentials: true
    });
  }

  borrarEntrenamientoBase(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }
}