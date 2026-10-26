import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SoporteService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/soporte';

  crearTicket(ticket: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, ticket, {
      withCredentials: true
    });
  }

  getMisTickets(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/mis-tickets`, {
      withCredentials: true
    });
  }

  getTicketById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/mis-tickets/${id}`, {
      withCredentials: true
    });
  }

  getMensajesTicket(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/mis-tickets/${id}/mensajes`, {
      withCredentials: true
    });
  }

  enviarMensajeTicket(id: number, contenido: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/mis-tickets/${id}/mensajes`, { contenido }, {
      withCredentials: true
    });
  }

  borrarTicket(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      withCredentials: true
    });
  }
}