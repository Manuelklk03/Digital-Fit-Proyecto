import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminCentrosPrivadosBaseService {

  private http = inject(HttpClient);
  private apiUrl = '/api/admin/centros-privados-base';

  crearCentroPrivadoBase(centro: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, centro, {
      withCredentials: true
    });
  }

  actualizarCentroPrivadoBase(id: number, centro: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, centro, {
      withCredentials: true
    });
  }

  borrarCentroPrivadoBase(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }
}
