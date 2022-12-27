import { ThisReceiver } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/services/account.services';

@Component({
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  title = 'MyMSN';
  private accountService: AccountService;
  private router: Router;
  loginForm: FormGroup = new FormGroup({
    login: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });

  constructor(accountService: AccountService, router: Router) {
    this.accountService = accountService;
    this.router = router;
  }

  login() {
    this.accountService
      .login(this.loginForm.value.login, this.loginForm.value.password)
      .then(() => {
        this.router.navigate(['/home']);
      })
      .catch((error: Error) => {
        console.log(error);
      });
  }

  ngOnInit() {}
}
