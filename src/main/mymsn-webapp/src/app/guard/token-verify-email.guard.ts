import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
} from '@angular/router';

@Injectable({ providedIn: 'root' })
export class TokenVerifyEmailGuard implements CanActivate {
  constructor(private router: Router, private messageService: MessageService) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    console.log(route.queryParams);
    if (!route.queryParams['token']) {
      this.router.navigate(['/']).then(() => {
        // To display error after DOM has been initialized
        setTimeout(() => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'We cannot verify your account',
          });
        }, 100);
      });
      return false;
    }
    return true;
  }
}
