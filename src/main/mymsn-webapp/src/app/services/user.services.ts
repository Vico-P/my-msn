import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environment/environment';

@Injectable({ providedIn: 'root' })
export class UserService {
  private http: HttpClient;
  constructor(http: HttpClient) {
    this.http = http;
  }

  public getUserByLogin(login: string): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/users/${login}`);
  }
}
