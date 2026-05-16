import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LiveConsole } from './live-console';

describe('LiveConsole', () => {
  let component: LiveConsole;
  let fixture: ComponentFixture<LiveConsole>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LiveConsole]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LiveConsole);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
