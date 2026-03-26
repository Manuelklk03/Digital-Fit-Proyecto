import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminSoporteService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/admin/soporte';

  getTicketsAdmin(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, {
      withCredentials: true
    });
  }

  getTicketAdminById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }

  cambiarEstado(id: number, estado: string): Observable<any> {
    return this.http.patch<any>(`${this.apiUrl}/${id}/estado`, { estado }, {
      withCredentials: true
    });
  }

  borrarTicketCerrado(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }
}