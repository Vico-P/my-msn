import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
} from '@angular/router';

@Injectable({ providedIn: 'root' })
export class TokenInParamsUrlGuard implements CanActivate {
  constructor(private router: Router, private messageService: MessageService) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    if (!route.queryParams['token']) {
      this.router.navigate(['/']).then(() => {
        // To display error after DOM has been initialized
        setTimeout(() => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'No token provided, unable to continue',
          });
        }, 100);
      });
      return false;
    }
    return true;
  }
}
