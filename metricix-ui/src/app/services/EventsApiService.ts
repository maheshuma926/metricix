import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EventsApiService {

  private http = inject(HttpClient);
  private API_URL = `${environment.apiBaseUrl}/api/blocking/track`;

  //private API_URL = `${environment.apiBaseUrl}/api/v1/track`; //non-blocking API

  sendEvent(apiKey: string, body: any): Observable<any> {
    const headers = new HttpHeaders({
      'X-API-Key': apiKey,
      'Content-Type': 'application/json'
    });

    return this.http.post(this.API_URL,
      body,
      {headers, observe: 'response'}
    );
  }

  getApiUrl() {
    return this.API_URL;
  }

}
