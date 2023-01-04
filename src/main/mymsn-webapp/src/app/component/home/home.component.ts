import { Component, OnInit } from '@angular/core';
import { AccountService } from 'src/app/services/account.services';
import { UserService } from 'src/app/services/user.services';

@Component({
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  private userService: UserService;
  private accountService: AccountService;
  constructor(userService: UserService, accountService: AccountService) {
    this.userService = userService;
    this.accountService = accountService;
  }

  ngOnInit() {
    this.userService.getUserByLogin('test_login').subscribe((result) => {
      console.log('COMMUNICATING WITH BACKEND : ', result);
    });
  }

  logout() {
    this.accountService.logout();
  }
}
