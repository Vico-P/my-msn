import { ThisReceiver } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  Validators,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from 'src/app/services/account.services';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService } from 'primeng/api';

@Component({
  templateUrl: './reset-password-finish.component.html',
  styleUrls: ['./reset-password-finish.component.css'],
})
export class ResetPasswordFinishComponent implements OnInit {
  private accountService: AccountService;
  private messageService: MessageService;
  private activatedRoute: ActivatedRoute;
  loading = false;
  success = false;
  token?: string;
  resetPasswordForm: FormGroup = new FormGroup(
    {
      password: new FormControl('', [Validators.required]),
      confirmPassword: new FormControl('', [Validators.required]),
    },
    { validators: MatchValidator }
  );

  constructor(
    accountService: AccountService,
    router: Router,
    messageService: MessageService,
    activatedRoute: ActivatedRoute
  ) {
    this.accountService = accountService;
    this.activatedRoute = activatedRoute;
    this.messageService = messageService;
  }

  resetPasswordFinish() {
    // Normally token will never be empty or null because our canActivate guard forbid access without a token in url params
    if (this.token) {
      this.loading = true;
      this.accountService
        .resetPasswordFinish({
          password: this.resetPasswordForm.get('password')?.value,
          confirmPassword: this.resetPasswordForm.get('confirmPassword')?.value,
          token: this.token,
        })
        .then(() => {
          this.loading = false;
          this.success = true;
        })
        .catch(({ error }: HttpErrorResponse) => {
          this.loading = false;
          this.messageService.add({
            severity: 'error',
            summary: error.title,
            detail: error.detail,
          });
        });
    }
  }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe((values) => {
      this.token = values['token'];
    });
  }
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
