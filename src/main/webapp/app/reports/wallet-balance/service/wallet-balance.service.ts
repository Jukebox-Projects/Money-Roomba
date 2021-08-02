import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { createRequestOption } from '../../../core/request/request-util';
import { map } from 'rxjs/operators';
import { IWalletBalance } from '../wallet-balance.model';
import { ICurrency } from '../../../entities/currency/currency.model';

export type EntityResponseType = HttpResponse<IWalletBalance>;
export type EntityArrayResponseType = HttpResponse<IWalletBalance[]>;

@Injectable({
  providedIn: 'root',
})
export class WalletBalanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reports/wallet-balance');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  query(id: number): Observable<EntityArrayResponseType> {
    return this.http.get<IWalletBalance[]>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  find(req?: any): Observable<EntityResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWalletBalance>(`${this.resourceUrl}/2`, { params: options, observe: 'response' });
  }
}
