import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SchedulePatternComponent } from './list/schedule-pattern.component';
import { SchedulePatternDetailComponent } from './detail/schedule-pattern-detail.component';
import { SchedulePatternUpdateComponent } from './update/schedule-pattern-update.component';
import { SchedulePatternDeleteDialogComponent } from './delete/schedule-pattern-delete-dialog.component';
import { SchedulePatternRoutingModule } from './route/schedule-pattern-routing.module';

@NgModule({
  imports: [SharedModule, SchedulePatternRoutingModule],
  declarations: [
    SchedulePatternComponent,
    SchedulePatternDetailComponent,
    SchedulePatternUpdateComponent,
    SchedulePatternDeleteDialogComponent,
  ],
  entryComponents: [SchedulePatternDeleteDialogComponent],
})
export class SchedulePatternModule {}
