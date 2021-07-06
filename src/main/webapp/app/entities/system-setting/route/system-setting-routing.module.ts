import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SystemSettingComponent } from '../list/system-setting.component';
import { SystemSettingDetailComponent } from '../detail/system-setting-detail.component';
import { SystemSettingUpdateComponent } from '../update/system-setting-update.component';
import { SystemSettingRoutingResolveService } from './system-setting-routing-resolve.service';

const systemSettingRoute: Routes = [
  {
    path: '',
    component: SystemSettingComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SystemSettingDetailComponent,
    resolve: {
      systemSetting: SystemSettingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SystemSettingUpdateComponent,
    resolve: {
      systemSetting: SystemSettingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SystemSettingUpdateComponent,
    resolve: {
      systemSetting: SystemSettingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(systemSettingRoute)],
  exports: [RouterModule],
})
export class SystemSettingRoutingModule {}
