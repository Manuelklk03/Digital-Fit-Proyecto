import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/admin';

  crearAdmin(admin: any): Observable<string> {
    return this.http.post(
      `${this.apiUrl}/crear-admin`,
      admin,
      {
        withCredentials: true,
        responseType: 'text'
      }
    );
  }
}
