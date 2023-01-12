import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { ResetPasswordInitRoutingModule } from './reset-password-init-routing.module';
import { ResetPasswordInitComponent } from './reset-password-init.component';

@NgModule({
  imports: [SharedModule, ResetPasswordInitRoutingModule],
  declarations: [ResetPasswordInitComponent],
})
export class ResetPasswordInitModule {}
