import {TestBed} from '@angular/core/testing';

import {EventsApiService} from './EventsApiService';

describe('Api', () => {
  let service: EventsApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EventsApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
