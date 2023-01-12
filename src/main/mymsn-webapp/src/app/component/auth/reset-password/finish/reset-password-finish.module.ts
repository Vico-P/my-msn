import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { ResetPasswordFinishRoutingModule } from './reset-password-finish-routing.module';
import { ResetPasswordFinishComponent } from './reset-password-finish.component';

@NgModule({
  imports: [SharedModule, ResetPasswordFinishRoutingModule],
  declarations: [ResetPasswordFinishComponent],
})
export class ResetPasswordFinishModule {}
