import { Component, OnInit } from '@angular/core';
import { UserService } from './services/user.services';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  private userService: UserService;
  title = 'my-msn';

  constructor(userService: UserService) {
    this.userService = userService;
  }

  ngOnInit() {
    this.userService.getUserByLogin('test_login').subscribe((result) => {
      console.log('COMMUNICATING WITH BACKEND : ', result);
    });
  }
}
