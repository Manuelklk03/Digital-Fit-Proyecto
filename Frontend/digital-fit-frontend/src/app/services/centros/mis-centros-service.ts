import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MisCentrosService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/mis-centros-privados';

  getMisCentros(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, {
      withCredentials: true
    });
  }

  getMiCentroById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }

  crearCentroDesdeMaps(centro: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/centros-maps`, centro, {
      withCredentials: true
    });
  }

  anadirCentroDesdeApp(id: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/centros-app/${id}`, {}, {
      withCredentials: true
    });
  }

  borrarCentro(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }
}
