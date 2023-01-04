import { ThisReceiver } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  Validators,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/services/account.services';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService } from 'primeng/api';

@Component({
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  private accountService: AccountService;
  private router: Router;
  private messageService: MessageService;
  loading = false;
  sentCreationRequest = false;
  registerForm: FormGroup = new FormGroup(
    {
      login: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),
      confirmPassword: new FormControl('', [Validators.required]),
    },
    { validators: MatchValidator }
  );

  constructor(
    accountService: AccountService,
    router: Router,
    messageService: MessageService
  ) {
    this.accountService = accountService;
    this.router = router;
    this.messageService = messageService;
  }

  register() {
    this.loading = true;
    this.accountService
      .register({
        username: this.registerForm.value.login,
        ...this.registerForm.value,
      })
      .then(() => {
        this.sentCreationRequest = true;
        this.loading = false;
      })
      .catch(({ error }: HttpErrorResponse) => {
        this.messageService.add({
          severity: 'error',
          summary: error.title,
          detail: error.detail,
        });
        this.loading = false;
      });
  }

  ngOnInit() {}
}

function MatchValidator(control: AbstractControl): ValidationErrors | null {
  const password: string = control.get('password')?.value; // get password from our password form control
  const confirmPassword: string = control.get('confirmPassword')?.value; // get password from our confirmPassword form control

  // if the confirmPassword value is null or empty, don't return an error.
  if (!confirmPassword?.length || !password?.length) {
    return null;
  }

  // compare the passwords and see if they match.
  if (password !== confirmPassword) {
    control.get('confirmPassword')?.setErrors({ mismatch: true });
    return { mismatching: true };
  }

  return null;
}
