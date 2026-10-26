import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EntrenamientosComunidadService {

  private http = inject(HttpClient);
  private apiUrl = '/api/entrenamientos-comunidad';

  getEntrenamientosComunidad(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, {
      withCredentials: true
    });
  }

  getEntrenamientosComunidadFiltrados(
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

  actualizarEntrenamientoComunidad(id: number, entrenamiento: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, entrenamiento, {
      withCredentials: true
    });
  }

  borrarEntrenamientoComunidad(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }
}