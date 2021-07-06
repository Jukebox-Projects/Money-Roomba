import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IScheduledTransaction, getScheduledTransactionIdentifier } from '../scheduled-transaction.model';

export type EntityResponseType = HttpResponse<IScheduledTransaction>;
export type EntityArrayResponseType = HttpResponse<IScheduledTransaction[]>;

@Injectable({ providedIn: 'root' })
export class ScheduledTransactionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/scheduled-transactions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(scheduledTransaction: IScheduledTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scheduledTransaction);
    return this.http
      .post<IScheduledTransaction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(scheduledTransaction: IScheduledTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scheduledTransaction);
    return this.http
      .put<IScheduledTransaction>(`${this.resourceUrl}/${getScheduledTransactionIdentifier(scheduledTransaction) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(scheduledTransaction: IScheduledTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scheduledTransaction);
    return this.http
      .patch<IScheduledTransaction>(`${this.resourceUrl}/${getScheduledTransactionIdentifier(scheduledTransaction) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IScheduledTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IScheduledTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addScheduledTransactionToCollectionIfMissing(
    scheduledTransactionCollection: IScheduledTransaction[],
    ...scheduledTransactionsToCheck: (IScheduledTransaction | null | undefined)[]
  ): IScheduledTransaction[] {
    const scheduledTransactions: IScheduledTransaction[] = scheduledTransactionsToCheck.filter(isPresent);
    if (scheduledTransactions.length > 0) {
      const scheduledTransactionCollectionIdentifiers = scheduledTransactionCollection.map(
        scheduledTransactionItem => getScheduledTransactionIdentifier(scheduledTransactionItem)!
      );
      const scheduledTransactionsToAdd = scheduledTransactions.filter(scheduledTransactionItem => {
        const scheduledTransactionIdentifier = getScheduledTransactionIdentifier(scheduledTransactionItem);
        if (scheduledTransactionIdentifier == null || scheduledTransactionCollectionIdentifiers.includes(scheduledTransactionIdentifier)) {
          return false;
        }
        scheduledTransactionCollectionIdentifiers.push(scheduledTransactionIdentifier);
        return true;
      });
      return [...scheduledTransactionsToAdd, ...scheduledTransactionCollection];
    }
    return scheduledTransactionCollection;
  }

  protected convertDateFromClient(scheduledTransaction: IScheduledTransaction): IScheduledTransaction {
    return Object.assign({}, scheduledTransaction, {
      startDate: scheduledTransaction.startDate?.isValid() ? scheduledTransaction.startDate.format(DATE_FORMAT) : undefined,
      endDate: scheduledTransaction.endDate?.isValid() ? scheduledTransaction.endDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((scheduledTransaction: IScheduledTransaction) => {
        scheduledTransaction.startDate = scheduledTransaction.startDate ? dayjs(scheduledTransaction.startDate) : undefined;
        scheduledTransaction.endDate = scheduledTransaction.endDate ? dayjs(scheduledTransaction.endDate) : undefined;
      });
    }
    return res;
  }
}
