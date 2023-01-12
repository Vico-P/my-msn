import { NgModule } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ReactiveFormsModule } from '@angular/forms';
import { PasswordModule } from 'primeng/password';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { CheckboxModule } from 'primeng/checkbox';
import { CommonModule } from '@angular/common';
import { CardModule } from '../custom-component/card/card.module';

@NgModule({
  exports: [
    InputTextModule,
    ButtonModule,
    ReactiveFormsModule,
    PasswordModule,
    ProgressSpinnerModule,
    ToastModule,
    CheckboxModule,
    CommonModule,
    CardModule,
  ],
  imports: [
    InputTextModule,
    ButtonModule,
    ReactiveFormsModule,
    PasswordModule,
    ProgressSpinnerModule,
    ToastModule,
    CheckboxModule,
    CommonModule,
    CardModule,
  ],
  providers: [MessageService],
})
export class SharedModule {}
