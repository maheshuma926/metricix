import {Component, EventEmitter, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'simulator-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './simulator-form.html'
})
export class SimulatorFormComponent {

  @Output() sendEvent = new EventEmitter<any>();

  apiKey = 'mtx_pub_test_123';
  eventType = 'button_click';

  payloadText = `{
  "button_id": "signup_hero",
  "path": "/home"
}`;

  submit() {
    let payload;
    try {
      payload = JSON.parse(this.payloadText);
    } catch {
      alert('Invalid JSON');
      return;
    }

    this.sendEvent.emit({
      apiKey: this.apiKey,
      events: [
        {
          eventType: this.eventType,
          payload
        }
      ]
    });
  }

  formatJson() {
    try {
      this.payloadText = JSON.stringify(JSON.parse(this.payloadText), null, 2);
    } catch {
      alert('Invalid JSON');
    }
  }
}
