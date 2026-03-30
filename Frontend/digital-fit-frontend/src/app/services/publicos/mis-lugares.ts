import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MisLugaresService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/mis-lugares-publicos';

  getMisLugares(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, {
      withCredentials: true
    });
  }

  getMisLugaresFiltrados(
    nombre?: string,
    direccion?: string,
    tipo?: string
  ): Observable<any[]> {
    let params = new HttpParams();

    if (nombre) {
      params = params.set('nombre', nombre);
    }

    if (direccion) {
      params = params.set('direccion', direccion);
    }

    if (tipo) {
      params = params.set('tipo', tipo.toUpperCase().replaceAll(' ', '_'));
    }

    return this.http.get<any[]>(this.apiUrl, {
      params,
      withCredentials: true
    });
  }

  getMiLugarById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }

  crearLugarDesdeMaps(lugar: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, lugar, {
      withCredentials: true
    });
  }

  anadirLugarDesdeBase(idBase: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/base/${idBase}`, {}, {
      withCredentials: true
    });
  }

  borrarLugar(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }
}