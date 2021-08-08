import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { createRequestOption } from '../../../core/request/request-util';
import { ITotalBalance } from '../total-balance.model';
import { DatePipe } from '@angular/common';
import { TotalBalanceModel } from '../../total-balance/total-balance.model';

export type EntityResponseType = HttpResponse<ITotalBalance>;
export type EntityArrayResponseType = HttpResponse<ITotalBalance[]>;

@Injectable({
  providedIn: 'root',
})
export class TotalBalanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reports/wallet-balance/totalBalance');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService, protected datePipe: DatePipe) {}

  queryAll(): Observable<EntityArrayResponseType> {
    return this.http.get<TotalBalanceModel[]>(this.resourceUrl, { observe: 'response' });
  }
}
