import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchedulePattern, getSchedulePatternIdentifier } from '../schedule-pattern.model';

export type EntityResponseType = HttpResponse<ISchedulePattern>;
export type EntityArrayResponseType = HttpResponse<ISchedulePattern[]>;

@Injectable({ providedIn: 'root' })
export class SchedulePatternService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/schedule-patterns');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(schedulePattern: ISchedulePattern): Observable<EntityResponseType> {
    return this.http.post<ISchedulePattern>(this.resourceUrl, schedulePattern, { observe: 'response' });
  }

  update(schedulePattern: ISchedulePattern): Observable<EntityResponseType> {
    return this.http.put<ISchedulePattern>(
      `${this.resourceUrl}/${getSchedulePatternIdentifier(schedulePattern) as number}`,
      schedulePattern,
      { observe: 'response' }
    );
  }

  partialUpdate(schedulePattern: ISchedulePattern): Observable<EntityResponseType> {
    return this.http.patch<ISchedulePattern>(
      `${this.resourceUrl}/${getSchedulePatternIdentifier(schedulePattern) as number}`,
      schedulePattern,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISchedulePattern>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISchedulePattern[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSchedulePatternToCollectionIfMissing(
    schedulePatternCollection: ISchedulePattern[],
    ...schedulePatternsToCheck: (ISchedulePattern | null | undefined)[]
  ): ISchedulePattern[] {
    const schedulePatterns: ISchedulePattern[] = schedulePatternsToCheck.filter(isPresent);
    if (schedulePatterns.length > 0) {
      const schedulePatternCollectionIdentifiers = schedulePatternCollection.map(
        schedulePatternItem => getSchedulePatternIdentifier(schedulePatternItem)!
      );
      const schedulePatternsToAdd = schedulePatterns.filter(schedulePatternItem => {
        const schedulePatternIdentifier = getSchedulePatternIdentifier(schedulePatternItem);
        if (schedulePatternIdentifier == null || schedulePatternCollectionIdentifiers.includes(schedulePatternIdentifier)) {
          return false;
        }
        schedulePatternCollectionIdentifiers.push(schedulePatternIdentifier);
        return true;
      });
      return [...schedulePatternsToAdd, ...schedulePatternCollection];
    }
    return schedulePatternCollection;
  }
}
