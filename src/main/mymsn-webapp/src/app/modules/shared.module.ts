import { NgModule } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ReactiveFormsModule } from '@angular/forms';
import { PasswordModule } from 'primeng/password';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@NgModule({
  exports: [
    InputTextModule,
    ButtonModule,
    ReactiveFormsModule,
    PasswordModule,
    ProgressSpinnerModule,
    ToastModule,
  ],
  imports: [
    InputTextModule,
    ButtonModule,
    ReactiveFormsModule,
    PasswordModule,
    ProgressSpinnerModule,
    ToastModule,
  ],
  providers: [MessageService],
})
export class SharedModule {}
