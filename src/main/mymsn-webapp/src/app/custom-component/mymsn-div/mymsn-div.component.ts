import {
  Component,
  ElementRef,
  ViewChild,
  Input,
  TemplateRef,
} from '@angular/core';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'mymsn-div',
  templateUrl: './mymsn-div.component.html',
})
export class MyMsnDivComponent {
  @Input() public divClass?: string;
  @Input() public gridClass?: string;
  constructor() {}
}
