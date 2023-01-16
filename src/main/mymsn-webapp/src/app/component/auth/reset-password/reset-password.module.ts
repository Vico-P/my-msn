import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { ResetPasswordRoutingModule } from './reset-password-routing.module';

@NgModule({
  imports: [SharedModule, ResetPasswordRoutingModule],
})
export class ResetPasswordModule {}
