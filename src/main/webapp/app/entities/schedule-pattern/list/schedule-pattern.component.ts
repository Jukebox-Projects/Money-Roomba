import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISchedulePattern } from '../schedule-pattern.model';
import { SchedulePatternService } from '../service/schedule-pattern.service';
import { SchedulePatternDeleteDialogComponent } from '../delete/schedule-pattern-delete-dialog.component';

@Component({
  selector: 'jhi-schedule-pattern',
  templateUrl: './schedule-pattern.component.html',
})
export class SchedulePatternComponent implements OnInit {
  schedulePatterns?: ISchedulePattern[];
  isLoading = false;

  constructor(protected schedulePatternService: SchedulePatternService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.schedulePatternService.query().subscribe(
      (res: HttpResponse<ISchedulePattern[]>) => {
        this.isLoading = false;
        this.schedulePatterns = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISchedulePattern): number {
    return item.id!;
  }

  delete(schedulePattern: ISchedulePattern): void {
    const modalRef = this.modalService.open(SchedulePatternDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.schedulePattern = schedulePattern;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
