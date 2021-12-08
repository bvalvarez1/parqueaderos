import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISystemParameters, getSystemParametersIdentifier } from '../system-parameters.model';

export type EntityResponseType = HttpResponse<ISystemParameters>;
export type EntityArrayResponseType = HttpResponse<ISystemParameters[]>;

@Injectable({ providedIn: 'root' })
export class SystemParametersService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/system-parameters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(systemParameters: ISystemParameters): Observable<EntityResponseType> {
    return this.http.post<ISystemParameters>(this.resourceUrl, systemParameters, { observe: 'response' });
  }

  update(systemParameters: ISystemParameters): Observable<EntityResponseType> {
    return this.http.put<ISystemParameters>(
      `${this.resourceUrl}/${getSystemParametersIdentifier(systemParameters) as number}`,
      systemParameters,
      { observe: 'response' }
    );
  }

  partialUpdate(systemParameters: ISystemParameters): Observable<EntityResponseType> {
    return this.http.patch<ISystemParameters>(
      `${this.resourceUrl}/${getSystemParametersIdentifier(systemParameters) as number}`,
      systemParameters,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISystemParameters>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISystemParameters[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSystemParametersToCollectionIfMissing(
    systemParametersCollection: ISystemParameters[],
    ...systemParametersToCheck: (ISystemParameters | null | undefined)[]
  ): ISystemParameters[] {
    const systemParameters: ISystemParameters[] = systemParametersToCheck.filter(isPresent);
    if (systemParameters.length > 0) {
      const systemParametersCollectionIdentifiers = systemParametersCollection.map(
        systemParametersItem => getSystemParametersIdentifier(systemParametersItem)!
      );
      const systemParametersToAdd = systemParameters.filter(systemParametersItem => {
        const systemParametersIdentifier = getSystemParametersIdentifier(systemParametersItem);
        if (systemParametersIdentifier == null || systemParametersCollectionIdentifiers.includes(systemParametersIdentifier)) {
          return false;
        }
        systemParametersCollectionIdentifiers.push(systemParametersIdentifier);
        return true;
      });
      return [...systemParametersToAdd, ...systemParametersCollection];
    }
    return systemParametersCollection;
  }
}
