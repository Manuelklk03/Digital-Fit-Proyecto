import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EntrenamientosService {

  private http = inject(HttpClient);
  private apiUrl = '/api/entrenamientos';

  getEntrenamientos(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, {
      withCredentials: true
    });
  }

  getEntrenamientosFiltrados(
    categoria?: string,
    nivel?: string,
    duracionEnMinutos?: number,
    nombre?: string
  ): Observable<any[]> {
    let params = new HttpParams();

    if (categoria) {
      params = params.set('categoria', categoria.toUpperCase().replaceAll(' ', '_'));
    }

    if (nivel) {
      params = params.set('nivel', nivel.toUpperCase().replaceAll(' ', '_'));
    }

    if (duracionEnMinutos !== undefined) {
      params = params.set('duracionEnMinutos', duracionEnMinutos.toString());
    }

    if (nombre) {
      params = params.set('nombre', nombre);
    }

    return this.http.get<any[]>(this.apiUrl, {
      params,
      withCredentials: true
    });
  }

  getEntrenamientoById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }
}