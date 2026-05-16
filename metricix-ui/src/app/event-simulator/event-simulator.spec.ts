import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventSimulator } from './event-simulator';

describe('EventSimulator', () => {
  let component: EventSimulator;
  let fixture: ComponentFixture<EventSimulator>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventSimulator]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventSimulator);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
