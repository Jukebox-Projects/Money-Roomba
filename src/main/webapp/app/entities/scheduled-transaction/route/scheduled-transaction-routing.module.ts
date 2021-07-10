import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ScheduledTransactionComponent } from '../list/scheduled-transaction.component';
import { ScheduledTransactionDetailComponent } from '../detail/scheduled-transaction-detail.component';
import { ScheduledTransactionUpdateComponent } from '../update/scheduled-transaction-update.component';
import { ScheduledTransactionRoutingResolveService } from './scheduled-transaction-routing-resolve.service';

const scheduledTransactionRoute: Routes = [
  {
    path: '',
    component: ScheduledTransactionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ScheduledTransactionDetailComponent,
    resolve: {
      scheduledTransaction: ScheduledTransactionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ScheduledTransactionUpdateComponent,
    resolve: {
      scheduledTransaction: ScheduledTransactionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ScheduledTransactionUpdateComponent,
    resolve: {
      scheduledTransaction: ScheduledTransactionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(scheduledTransactionRoute)],
  exports: [RouterModule],
})
export class ScheduledTransactionRoutingModule {}
