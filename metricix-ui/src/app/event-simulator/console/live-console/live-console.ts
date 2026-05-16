import {Component, Input} from '@angular/core';
import {LogItemComponent} from '../../log-item/log-item';
import {LogEntry} from '../../log-item/model/log.model';

@Component({
  selector: 'live-console',
  standalone: true,
  imports: [LogItemComponent],
  templateUrl: './live-console.html',
  styleUrls: ['./live-console.css']
})
export class LiveConsoleComponent {

  @Input() logs: LogEntry[] = [];

}
