import { NgModule } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  exports: [InputTextModule, ButtonModule, ReactiveFormsModule],
  imports: [InputTextModule, ButtonModule, ReactiveFormsModule],
  providers: [],
})
export class SharedModule {}
