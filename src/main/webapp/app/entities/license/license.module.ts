import { LicenseCreateDialogComponent } from './create/license-create-dialog.component';
import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LicenseComponent } from './list/license.component';
import { LicenseDetailComponent } from './detail/license-detail.component';
import { LicenseUpdateComponent } from './update/license-update.component';
import { LicenseDeleteDialogComponent } from './delete/license-delete-dialog.component';
import { LicenseRoutingModule } from './route/license-routing.module';
import { LicenseViewComponent } from './license-view/license-view.component';

@NgModule({
  imports: [SharedModule, LicenseRoutingModule],
  declarations: [
    LicenseComponent,
    LicenseDetailComponent,
    LicenseUpdateComponent,
    LicenseDeleteDialogComponent,
    LicenseCreateDialogComponent,
  ],
  entryComponents: [LicenseDeleteDialogComponent],
})
export class LicenseModule {}
