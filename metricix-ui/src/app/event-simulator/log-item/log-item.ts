import {Component, Input} from '@angular/core';
import {JsonPipe} from '@angular/common';

@Component({
  selector: 'log-item',
  standalone: true,
  imports: [
    JsonPipe
  ],
  templateUrl: './log-item.html'
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
