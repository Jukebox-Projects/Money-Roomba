import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IScheduledTransaction, ScheduledTransaction } from '../scheduled-transaction.model';
import { ScheduledTransactionService } from '../service/scheduled-transaction.service';

@Injectable({ providedIn: 'root' })
export class ScheduledTransactionRoutingResolveService implements Resolve<IScheduledTransaction> {
  constructor(protected service: ScheduledTransactionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IScheduledTransaction> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((scheduledTransaction: HttpResponse<ScheduledTransaction>) => {
          if (scheduledTransaction.body) {
            return of(scheduledTransaction.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ScheduledTransaction());
  }
}
