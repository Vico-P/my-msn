import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { RegisterRoutingModule } from './register-routing.module';
import { RegisterComponent } from './register.component';

@NgModule({
  imports: [SharedModule, RegisterRoutingModule],
  declarations: [RegisterComponent],
})
export class RegisterModule {}
