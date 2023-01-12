import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'; // CLI imports router
import { ResetPasswordInitComponent } from './reset-password-init.component';

const routes: Routes = [
  {
    path: '',
    component: ResetPasswordInitComponent,
  },
];

// configures NgModule imports and exports
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ResetPasswordInitRoutingModule {}
