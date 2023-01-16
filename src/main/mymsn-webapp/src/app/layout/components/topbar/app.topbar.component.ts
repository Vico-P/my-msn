import { Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { MenuItem, MessageService } from 'primeng/api';
import { AccountService } from 'src/app/services/account.services';
import { LayoutService } from '../../service/app.layout.service';

@Component({
  selector: 'app-topbar',
  templateUrl: './app.topbar.component.html',
})
export class AppTopBarComponent implements OnInit {
  @ViewChild('menubutton') menuButton!: ElementRef;

  @ViewChild('topbarmenubutton') topbarMenuButton!: ElementRef;

  @ViewChild('topbarmenu') menu!: ElementRef;

  public menuItems: MenuItem[] = [];

  constructor(
    public layoutService: LayoutService,
    private accountService: AccountService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {}

  logout() {
    this.accountService.logout();
  }

  editProfileOnDevAlert(): void {
    this.messageService.add({
      severity: 'info',
      summary: 'Information',
      detail: 'Functionnality will be coming soon',
    });
  }
}
