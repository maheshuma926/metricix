import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'   // ✅ available globally
})
export class EventsApiService {

  private http = inject(HttpClient);
  private API_URL = 'http://localhost:8080/api/blocking/track';

  sendEvent(apiKey: string, body: any): Observable<any> {
    const headers = new HttpHeaders({
      'X-API-Key': apiKey,
      'Content-Type': 'application/json'
    });

    return this.http.post(this.API_URL, body, {
      headers,
      observe: 'response'
    });
  }
}
