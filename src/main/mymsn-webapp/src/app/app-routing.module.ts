import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'; // CLI imports router
import { HomeComponent } from './component/home/home.component';
import { LoginComponent } from './component/login/login.component';
import { NotFoundComponent } from './component/not-found/not-found.component';
import { RegisterComponent } from './component/register/register.component';
import { ResetPasswordFinishComponent } from './component/reset-password/finish/reset-password-finish.component';
import { ResetPasswordInitComponent } from './component/reset-password/init/reset-password-init.component';
import { VerifyEmailComponent } from './component/verify-email/verify-email.component';
import { NotLoggedGuard } from './guard/not-logged.guard';
import { TokenInParamsUrlGuard } from './guard/token-in-params-url.guard';
import { TokenGuard } from './guard/token.guard';

const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
    title: 'MyMsn',
    canActivate: [NotLoggedGuard],
  },
  {
    path: 'register',
    component: RegisterComponent,
    title: 'Register',
    canActivate: [NotLoggedGuard],
  },
  {
    path: 'forgot-password',
    component: HomeComponent,
    title: 'Forgot password',
    canActivate: [NotLoggedGuard],
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [TokenGuard],
    title: 'Home',
  },
  {
    path: 'verify-email',
    component: VerifyEmailComponent,
    canActivate: [NotLoggedGuard, TokenInParamsUrlGuard],
    title: 'Verify your email',
  },
  {
    path: 'reset-password',
    children: [
      {
        path: 'init',
        component: ResetPasswordInitComponent,
        canActivate: [NotLoggedGuard],
        title: 'Password forgotten',
      },
      {
        path: 'finish',
        component: ResetPasswordFinishComponent,
        canActivate: [NotLoggedGuard, TokenInParamsUrlGuard],
        title: 'Set new password',
      },
    ],
  },
  { path: '**', component: NotFoundComponent },
];

// configures NgModule imports and exports
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
