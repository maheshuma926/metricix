import {Routes} from '@angular/router';
import {EventSimulatorComponent} from './event-simulator/event-simulator';
import {EventDashboard} from './event-dashboard/event-dashboard';

export const routes: Routes = [
  {path: '', component: EventSimulatorComponent},
  {path: 'dashboard', component: EventDashboard}
];
