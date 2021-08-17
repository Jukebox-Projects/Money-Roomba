import { Injectable } from '@angular/core';
import { DatePipe } from '@angular/common';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ITransactionCount } from '../transaction-count.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { Observable } from 'rxjs';
import { createRequestOption } from '../../../core/request/request-util';
import { IWalletBalance } from '../../wallet-balance/wallet-balance.model';

export type EntityResponseType = HttpResponse<ITransactionCount>;
export type EntityArrayResponseType = HttpResponse<ITransactionCount[]>;

@Injectable({
  providedIn: 'root',
})
export class TransactionCountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reports/transaction-count');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService, protected datePipe: DatePipe) {}

  queryAll(): Observable<EntityArrayResponseType> {
    var endDate = new Date();
    var startDate = new Date(new Date().setDate(endDate.getDate() - 30));
    const options = createRequestOption({
      startDate: this.datePipe.transform(startDate, 'yyyy-MM-dd'),
      endDate: this.datePipe.transform(endDate, 'yyyy-MM-dd'),
    });

    return this.http.get<ITransactionCount[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}
