import { Injectable, OnInit } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICurrency, getCurrencyIdentifier } from '../../currency/currency.model';

export type EntityResponseType = HttpResponse<ICurrency>;
export type EntityArrayResponseType = HttpResponse<ICurrency[]>;

@Injectable({
  providedIn: 'root',
})
export class CurrencyConverterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/currencies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICurrency[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  addCurrencyToCollectionIfMissing(currencyCollection: ICurrency[], ...currenciesToCheck: (ICurrency | null | undefined)[]): ICurrency[] {
    const currencies: ICurrency[] = currenciesToCheck.filter(isPresent);
    if (currencies.length > 0) {
      const currencyCollectionIdentifiers = currencyCollection.map(currencyItem => getCurrencyIdentifier(currencyItem)!);
      const currenciesToAdd = currencies.filter(currencyItem => {
        const currencyIdentifier = getCurrencyIdentifier(currencyItem);
        if (currencyIdentifier == null || currencyCollectionIdentifiers.includes(currencyIdentifier)) {
          return false;
        }
        currencyCollectionIdentifiers.push(currencyIdentifier);
        return true;
      });
      return [...currenciesToAdd, ...currencyCollection];
    }
    return currencyCollection;
  }
}
