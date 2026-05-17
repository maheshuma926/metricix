import {Component, Input} from '@angular/core';
import {JsonPipe, NgClass} from '@angular/common';

@Component({
  selector: 'log-item',
  standalone: true,
  imports: [
    JsonPipe,
    NgClass
  ],
  templateUrl: './log-item.html',
  styleUrls: ['./log-item.css']
})
export class LogItemComponent {

  @Input() log: any;

  isRequest() {
    return this.log.type === 'request';
  }

  isResponse() {
    return this.log.type === 'response';
  }

  isError() {
    return this.log.type === 'error';
  }
}
