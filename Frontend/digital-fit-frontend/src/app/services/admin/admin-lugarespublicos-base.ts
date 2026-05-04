import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminLugaresPublicosBaseService {

  private http = inject(HttpClient);
  private apiUrl = '/api/admin/lugares-publicos-base';

  crearLugarPublicoBase(lugar: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, lugar, {
      withCredentials: true
    });
  }

  actualizarLugarPublicoBase(id: number, lugar: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, lugar, {
      withCredentials: true
    });
  }

  borrarLugarPublicoBase(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }
}
