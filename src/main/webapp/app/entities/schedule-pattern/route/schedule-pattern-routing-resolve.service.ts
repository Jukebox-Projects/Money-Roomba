import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISchedulePattern, SchedulePattern } from '../schedule-pattern.model';
import { SchedulePatternService } from '../service/schedule-pattern.service';

@Injectable({ providedIn: 'root' })
export class SchedulePatternRoutingResolveService implements Resolve<ISchedulePattern> {
  constructor(protected service: SchedulePatternService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISchedulePattern> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((schedulePattern: HttpResponse<SchedulePattern>) => {
          if (schedulePattern.body) {
            return of(schedulePattern.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SchedulePattern());
  }
}
