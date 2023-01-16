import { HttpErrorResponse } from '@angular/common/http';
import { ThisReceiver } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/services/account.services';
import { MessageService } from 'primeng/api';

@Component({
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  private accountService: AccountService;
  private router: Router;
  private messageService: MessageService;
  loading: boolean = false;
  loginForm: FormGroup = new FormGroup({
    login: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
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

  login() {
    this.loading = true;
    this.accountService
      .login(this.loginForm.value.login, this.loginForm.value.password)
      .then(() => {
        this.loading = false;
        this.router.navigate(['/home']);
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
