import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PublicosService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/lugares-publicos';

  getLugaresPublicos(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, {
      withCredentials: true
    });
  }

  getLugaresPublicosFiltrados(
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

  getLugarPublicoById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }
}