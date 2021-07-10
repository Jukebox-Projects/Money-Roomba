import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchedulePattern } from '../schedule-pattern.model';
import { SchedulePatternService } from '../service/schedule-pattern.service';

@Component({
  templateUrl: './schedule-pattern-delete-dialog.component.html',
})
export class SchedulePatternDeleteDialogComponent {
  schedulePattern?: ISchedulePattern;

  constructor(protected schedulePatternService: SchedulePatternService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.schedulePatternService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
