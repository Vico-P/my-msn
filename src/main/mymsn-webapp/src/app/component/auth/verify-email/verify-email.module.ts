import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { VerifyEmailRoutingModule } from './verify-email-routing.module';
import { VerifyEmailComponent } from './verify-email.component';

@NgModule({
  imports: [SharedModule, VerifyEmailRoutingModule],
  declarations: [VerifyEmailComponent],
})
export class VerifyEmailModule {}
