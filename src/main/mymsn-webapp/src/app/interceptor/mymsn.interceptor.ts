import { Injectable } from '@angular/core';
import { Observable, map, catchError, of, throwError } from 'rxjs';
import {
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
  HttpHandler,
  HttpRequest,
} from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class MyMsnInterceptor implements HttpInterceptor {
  private router: Router;

  constructor(router: Router) {
    this.router = router;
  }

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (!req.url.includes('/login')) {
      // We add token to header to make request on endpoint with authentication required
      req = req.clone({
        headers: req.headers.set(
          'Authorization',
          localStorage.getItem('token')
            ? `Bearer ${localStorage.getItem('token')}`
            : ''
        ),
      });
    }
    return next.handle(req).pipe(
      // If we have a 401 error it means the token has expired or is invalid
      // so we clear the local storage and redirect to login page
      // We use pipe to handle error and return a copy of the initial error normally
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          localStorage.clear();
          this.router.navigate(['']);
        }
        return new Promise<any>((_, reject) => {
          reject(error);
        });
      })
    );
  }
}
