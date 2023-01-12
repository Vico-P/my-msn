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
  templateUrl: './reset-password-init.component.html',
  styleUrls: ['./reset-password-init.component.css'],
})
export class ResetPasswordInitComponent implements OnInit {
  private accountService: AccountService;
  private router: Router;
  private messageService: MessageService;
  loading = false;
  sentResetRequest = false;
  resetPasswordForm: FormGroup = new FormGroup({
    login: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
  });

  constructor(
    accountService: AccountService,
    router: Router,
    messageService: MessageService
  ) {
    this.accountService = accountService;
    this.router = router;
    this.messageService = messageService;
  }

  resetPasswordInit() {
    this.loading = true;
    this.accountService
      .resetPasswordInit(
        this.resetPasswordForm.get('login')?.value,
        this.resetPasswordForm.get('email')?.value
      )
      .then(() => {
        this.loading = false;
        this.sentResetRequest = true;
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

  ngOnInit() {}
}
