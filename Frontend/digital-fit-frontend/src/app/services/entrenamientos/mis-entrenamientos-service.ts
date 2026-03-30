import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MisEntrenamientosService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/mis-entrenamientos';

  getMisEntrenamientos(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, {
      withCredentials: true
    });
  }

  getMisEntrenamientosFiltrados(
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

  getMiEntrenamientoById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }

  crearEntrenamiento(entrenamiento: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, entrenamiento, {
      withCredentials: true
    });
  }

  anadirDesdeBase(idBase: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/desde-base/${idBase}`, {}, {
      withCredentials: true
    });
  }

  anadirDesdeComunidad(idComunidad: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/desde-comunidad/${idComunidad}`, {}, {
      withCredentials: true
    });
  }

  borrarEntrenamiento(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }
}