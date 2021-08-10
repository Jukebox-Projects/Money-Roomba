import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { createRequestOption } from '../../../core/request/request-util';
import { IWalletBalance } from '../wallet-balance.model';
import { DatePipe } from '@angular/common';

export type EntityResponseType = HttpResponse<IWalletBalance>;
export type EntityArrayResponseType = HttpResponse<IWalletBalance[]>;

@Injectable({
  providedIn: 'root',
})
export class WalletBalanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reports/wallet-balance');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService, protected datePipe: DatePipe) {}

  query(id: number): Observable<EntityArrayResponseType> {
    return this.http.get<IWalletBalance[]>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  queryAll(): Observable<EntityArrayResponseType> {
    var endDate = new Date();
    var startDate = new Date(new Date().setDate(endDate.getDate() - 30));
    const options = createRequestOption({
      startDate: this.datePipe.transform(startDate, 'yyyy-MM-dd'),
      endDate: this.datePipe.transform(endDate, 'yyyy-MM-dd'),
    });

    return this.http.get<IWalletBalance[]>(this.resourceUrl, { observe: 'response' });
  }
}
