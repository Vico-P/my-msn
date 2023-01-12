import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'; // CLI imports router
import { TokenInParamsUrlGuard } from 'src/app/guard/token-in-params-url.guard';

const routes: Routes = [
  {
    path: 'init',
    loadChildren: () =>
      import('./init/reset-password-init.module').then(
        (m) => m.ResetPasswordInitModule
      ),
  },
  {
    path: 'finish',
    loadChildren: () =>
      import('./finish/reset-password-finish.module').then(
        (m) => m.ResetPasswordFinishModule
      ),
    canActivate: [TokenInParamsUrlGuard],
  },
];

// configures NgModule imports and exports
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ResetPasswordRoutingModule {}
