import { inject, Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/auth';

  login(username: string, password: string): Observable<string> {
    const body = new URLSearchParams();
    body.set('username', username);
    body.set('password', password);

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded'
    });

    return this.http.post(
      `${this.apiUrl}/login`,
      body.toString(),
      {
        headers,
        withCredentials: true,
        responseType: 'text'
      }
    );
  }

  registrar(usuario: any): Observable<string> {
    return this.http.post(
      `${this.apiUrl}/registro`,
      usuario,
      {
        withCredentials: true,
        responseType: 'text'
      }
    );
  }

  yo(): Observable<string> {
    return this.http.get(
      `${this.apiUrl}/yo`,
      {
        withCredentials: true,
        responseType: 'text'
      }
    );
  }

  logout(): Observable<string> {
    return this.http.post(
      `${this.apiUrl}/logout`,
      {},
      {
        withCredentials: true,
        responseType: 'text'
      }
    );
  }

}
