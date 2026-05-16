import {Component, inject} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {LiveConsoleComponent} from './console/live-console/live-console';
import {SimulatorFormComponent} from './form/simulator-form/simulator-form';
import {EventsApiService} from '../services/api';


@Component({
  selector: 'event-simulator',
  standalone: true,
  imports: [
    LiveConsoleComponent,
    SimulatorFormComponent
  ],
  templateUrl: './event-simulator.html'
})
export class EventSimulatorComponent {

  logs: any[] = [];
  API_URL = 'http://localhost:8080/api/blocking/track';
  // ✅ Modern injection
  private http = inject(HttpClient);
  private apiService = inject(EventsApiService);

  handleEventSent(data: any) {

    data.events.forEach((ev: any) => {

      const body = {
        event_type: ev.eventType,
        payload: ev.payload
      };

      // ✅ Request log
      this.logs.push({
        type: 'request',
        method: 'POST',                // ✅ FIX
        message: `${this.API_URL}`,    // optional
        apiUrl: this.API_URL,
        payload: body,
        timestamp: this.getTime()
      });

      // ✅ Call service
      this.apiService.sendEvent(data.apiKey, body)
        .subscribe({
          next: (res) => {

            this.logs.push({
              type: 'response',
              message: `${res.status} Accepted`,
              apiUrl: this.API_URL,
              payload: res.body,
              timestamp: this.getTime()
            });

          },
          error: (err) => {

            this.logs.push({
              type: 'error',
              message: 'Request Failed',
              payload: err.error || err,
              timestamp: this.getTime()
            });

          }
        });

    });
  }

  getTime() {
    return new Date().toLocaleTimeString();
  }

  clearLogs() {
    this.logs = [];
  }
}
