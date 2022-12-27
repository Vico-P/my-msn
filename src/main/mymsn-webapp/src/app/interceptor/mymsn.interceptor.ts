import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
  HttpEvent,
  HttpInterceptor,
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
    return next.handle(req);
  }
}
