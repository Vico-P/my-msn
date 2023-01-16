import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'; // CLI imports router
import { NotLoggedGuard } from 'src/app/guard/not-logged.guard';
import { TokenInParamsUrlGuard } from 'src/app/guard/token-in-params-url.guard';

const routes: Routes = [
  {
    path: '',
    loadChildren: () =>
      import('./login/login.module').then((m) => m.LoginModule),
    canActivate: [NotLoggedGuard],
  },
  {
    path: 'register',
    loadChildren: () =>
      import('./register/register.module').then((m) => m.RegisterModule),
    canActivate: [NotLoggedGuard],
  },
  {
    path: 'verify-email',
    loadChildren: () =>
      import('./verify-email/verify-email.module').then(
        (m) => m.VerifyEmailModule
      ),
    canActivate: [NotLoggedGuard, TokenInParamsUrlGuard],
    title: 'Verify your email',
  },
  {
    path: 'reset-password',
    loadChildren: () =>
      import('./reset-password/reset-password.module').then(
        (m) => m.ResetPasswordModule
      ),
    canActivate: [NotLoggedGuard],
  },
  {
    path: '**',
    redirectTo: '/notfound',
  },
];

// configures NgModule imports and exports
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AuthRoutingModule {}
