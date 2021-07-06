import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SchedulePatternComponent } from '../list/schedule-pattern.component';
import { SchedulePatternDetailComponent } from '../detail/schedule-pattern-detail.component';
import { SchedulePatternUpdateComponent } from '../update/schedule-pattern-update.component';
import { SchedulePatternRoutingResolveService } from './schedule-pattern-routing-resolve.service';

const schedulePatternRoute: Routes = [
  {
    path: '',
    component: SchedulePatternComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SchedulePatternDetailComponent,
    resolve: {
      schedulePattern: SchedulePatternRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SchedulePatternUpdateComponent,
    resolve: {
      schedulePattern: SchedulePatternRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SchedulePatternUpdateComponent,
    resolve: {
      schedulePattern: SchedulePatternRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(schedulePatternRoute)],
  exports: [RouterModule],
})
export class SchedulePatternRoutingModule {}
