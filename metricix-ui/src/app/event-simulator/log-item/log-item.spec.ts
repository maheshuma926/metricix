import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogItem } from './log-item';

describe('LogItem', () => {
  let component: LogItem;
  let fixture: ComponentFixture<LogItem>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LogItem]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LogItem);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
