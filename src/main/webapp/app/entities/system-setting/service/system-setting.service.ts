import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISystemSetting, getSystemSettingIdentifier } from '../system-setting.model';

export type EntityResponseType = HttpResponse<ISystemSetting>;
export type EntityArrayResponseType = HttpResponse<ISystemSetting[]>;

@Injectable({ providedIn: 'root' })
export class SystemSettingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/system-settings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(systemSetting: ISystemSetting): Observable<EntityResponseType> {
    return this.http.post<ISystemSetting>(this.resourceUrl, systemSetting, { observe: 'response' });
  }

  update(systemSetting: ISystemSetting): Observable<EntityResponseType> {
    return this.http.put<ISystemSetting>(`${this.resourceUrl}/${getSystemSettingIdentifier(systemSetting) as number}`, systemSetting, {
      observe: 'response',
    });
  }

  partialUpdate(systemSetting: ISystemSetting): Observable<EntityResponseType> {
    return this.http.patch<ISystemSetting>(`${this.resourceUrl}/${getSystemSettingIdentifier(systemSetting) as number}`, systemSetting, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISystemSetting>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISystemSetting[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSystemSettingToCollectionIfMissing(
    systemSettingCollection: ISystemSetting[],
    ...systemSettingsToCheck: (ISystemSetting | null | undefined)[]
  ): ISystemSetting[] {
    const systemSettings: ISystemSetting[] = systemSettingsToCheck.filter(isPresent);
    if (systemSettings.length > 0) {
      const systemSettingCollectionIdentifiers = systemSettingCollection.map(
        systemSettingItem => getSystemSettingIdentifier(systemSettingItem)!
      );
      const systemSettingsToAdd = systemSettings.filter(systemSettingItem => {
        const systemSettingIdentifier = getSystemSettingIdentifier(systemSettingItem);
        if (systemSettingIdentifier == null || systemSettingCollectionIdentifiers.includes(systemSettingIdentifier)) {
          return false;
        }
        systemSettingCollectionIdentifiers.push(systemSettingIdentifier);
        return true;
      });
      return [...systemSettingsToAdd, ...systemSettingCollection];
    }
    return systemSettingCollection;
  }
}
