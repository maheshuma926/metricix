import {Component, inject, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {LiveConsoleComponent} from './console/live-console/live-console';
import {SimulatorFormComponent} from './form/simulator-form/simulator-form';
import {EventsApiService} from '../services/EventsApiService';


@Component({
  selector: 'event-simulator',
  standalone: true,
  imports: [
    LiveConsoleComponent,
    SimulatorFormComponent
  ],
  templateUrl: './event-simulator.html',
  styleUrls: ['./event-simulator.css']
})
export class EventSimulatorComponent {


  logs = signal<any[]>([]);

  private http = inject(HttpClient);
  private apiService = inject(EventsApiService);
  API_URL = this.apiService.getApiUrl();

  handleEventSent(data: any) {
    data.events.forEach((ev: any) => {
      const body = this.buildRequestBody(ev);
      this.logRequest(ev, body);
      this.callEventApi(data.apiKey, body);
    });
  }

  getTime() {
    return new Date().toLocaleTimeString();
  }

  clearLogs() {
    this.logs.set([]);
  }

  private buildRequestBody(ev: any) {
    return {
      event_type: ev.eventType,
      payload: ev.payload
    };
  }

  private logRequest(ev: any, body: any) {
    this.logs.update(currentLogs => [
      ...currentLogs,
      {
        type: 'request',
        method: 'POST',
        apiUrl: this.API_URL,
        message: `(${ev.eventType})`,
        payload: body,
        timestamp: this.getTime()
      }
    ]);
  }

  private callEventApi(apiKey: string, body: any) {
    this.apiService.sendEvent(apiKey, body)
      .subscribe({
        next: (res) => this.handleSuccess(res),
        error: (err) => this.handleError(err)
      });

  }

  private handleSuccess(res: any) {

    const {status, body} = res;

    this.logs.update(currentLogs => [
      ...currentLogs,
      {
        type: 'response',
        method: 'POST',
        apiUrl: this.API_URL,
        status: status,
        payload: body,
        timestamp: this.getTime()
      }
    ]);
  }

  private handleError(err: any) {

    this.logs.update(currentLogs => [
      ...currentLogs,
      {
        type: 'error',
        method: 'POST',
        apiUrl: this.API_URL,
        message: err.message,
        payload: err.error || {message: err.message},
        timestamp: this.getTime()
      }
    ]);

  }

}
