import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISchedulePattern } from '../schedule-pattern.model';

@Component({
  selector: 'jhi-schedule-pattern-detail',
  templateUrl: './schedule-pattern-detail.component.html',
})
export class SchedulePatternDetailComponent implements OnInit {
  schedulePattern: ISchedulePattern | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schedulePattern }) => {
      this.schedulePattern = schedulePattern;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
