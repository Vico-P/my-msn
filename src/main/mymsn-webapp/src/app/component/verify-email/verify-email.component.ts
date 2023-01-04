import { ThisReceiver } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AccountService } from 'src/app/services/account.services';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService } from 'primeng/api';

@Component({
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.css'],
})
export class VerifyEmailComponent implements OnInit {
  private accountService: AccountService;
  private messageService: MessageService;
  private activatedRoute: ActivatedRoute;
  private token?: string;

  error = false;
  loading = true;

  constructor(
    accountService: AccountService,
    messageService: MessageService,
    activatedRoute: ActivatedRoute
  ) {
    this.accountService = accountService;
    this.messageService = messageService;
    this.activatedRoute = activatedRoute;
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((values) => {
      this.token = values['token'];
      this.verifyEmail();
    });
  }

  verifyEmail(): void {
    if (!this.token) {
      this.loading = false;
      this.error = true;
      return;
    }
    this.accountService
      .verifyEmail(this.token)
      .then(() => {
        this.loading = false;
      })
      .catch(({ error }: HttpErrorResponse) => {
        this.loading = false;
        this.error = true;
        this.messageService.add({
          severity: 'error',
          summary: error.title,
          detail: error.detail,
        });
      });
  }
}
