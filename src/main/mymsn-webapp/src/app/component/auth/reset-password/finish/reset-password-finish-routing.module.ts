import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'; // CLI imports router
import { ResetPasswordFinishComponent } from './reset-password-finish.component';

const routes: Routes = [
  {
    path: '',
    component: ResetPasswordFinishComponent,
  },
];

// configures NgModule imports and exports
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ResetPasswordFinishRoutingModule {}
