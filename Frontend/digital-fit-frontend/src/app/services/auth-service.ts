import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private http = inject(HttpClient);
  private api = 'http://localhost:8080/api/auth';

  login(username: string, password: string): Observable<any> {

    const body = new URLSearchParams();
    body.set('username', username);
    body.set('password', password);

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded'
    });

    return this.http.post(
      `${this.api}/login`,
      body.toString(),
      {
        headers,
        withCredentials: true,
        responseType: 'text'
      }
    );
  }

  registrar(usuario: any): Observable<any> {

    return this.http.post(
      `${this.api}/registro`,
      usuario,
      {
        withCredentials: true,
        responseType: 'text'
      }
    );

  }

  yo(): Observable<any> {
    return this.http.get(`${this.api}/yo`, {
      withCredentials: true,
      responseType: 'text'
    });
  }

  logout(): Observable<any> {
    return this.http.post(`${this.api}/logout`, {}, {
      withCredentials: true,
      responseType: 'text'
    });
  }
}