import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ValoracionesService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/valoraciones';

  getMisValoraciones(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/mis-valoraciones`, {
      withCredentials: true
    });
  }

  getValoracionesPorContenido(tipoContenido: string, idContenido: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${tipoContenido}/${idContenido}`, {
      withCredentials: true
    });
  }

  crearOActualizarValoracion(tipoContenido: string, idContenido: number, valoracion: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${tipoContenido}/${idContenido}`, valoracion, {
      withCredentials: true
    });
  }

  borrarValoracion(tipoContenido: string, idContenido: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${tipoContenido}/${idContenido}`, {
      withCredentials: true
    });
  }
}
