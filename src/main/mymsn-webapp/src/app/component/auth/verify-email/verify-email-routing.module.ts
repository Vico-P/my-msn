import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'; // CLI imports router
import { VerifyEmailComponent } from './verify-email.component';

const routes: Routes = [
  {
    path: '',
    component: VerifyEmailComponent,
  },
];

// configures NgModule imports and exports
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class VerifyEmailRoutingModule {}
