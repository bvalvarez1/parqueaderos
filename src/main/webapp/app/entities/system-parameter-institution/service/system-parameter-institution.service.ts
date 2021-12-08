import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISystemParameterInstitution, getSystemParameterInstitutionIdentifier } from '../system-parameter-institution.model';

export type EntityResponseType = HttpResponse<ISystemParameterInstitution>;
export type EntityArrayResponseType = HttpResponse<ISystemParameterInstitution[]>;

@Injectable({ providedIn: 'root' })
export class SystemParameterInstitutionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/system-parameter-institutions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(systemParameterInstitution: ISystemParameterInstitution): Observable<EntityResponseType> {
    return this.http.post<ISystemParameterInstitution>(this.resourceUrl, systemParameterInstitution, { observe: 'response' });
  }

  update(systemParameterInstitution: ISystemParameterInstitution): Observable<EntityResponseType> {
    return this.http.put<ISystemParameterInstitution>(
      `${this.resourceUrl}/${getSystemParameterInstitutionIdentifier(systemParameterInstitution) as number}`,
      systemParameterInstitution,
      { observe: 'response' }
    );
  }

  partialUpdate(systemParameterInstitution: ISystemParameterInstitution): Observable<EntityResponseType> {
    return this.http.patch<ISystemParameterInstitution>(
      `${this.resourceUrl}/${getSystemParameterInstitutionIdentifier(systemParameterInstitution) as number}`,
      systemParameterInstitution,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISystemParameterInstitution>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISystemParameterInstitution[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSystemParameterInstitutionToCollectionIfMissing(
    systemParameterInstitutionCollection: ISystemParameterInstitution[],
    ...systemParameterInstitutionsToCheck: (ISystemParameterInstitution | null | undefined)[]
  ): ISystemParameterInstitution[] {
    const systemParameterInstitutions: ISystemParameterInstitution[] = systemParameterInstitutionsToCheck.filter(isPresent);
    if (systemParameterInstitutions.length > 0) {
      const systemParameterInstitutionCollectionIdentifiers = systemParameterInstitutionCollection.map(
        systemParameterInstitutionItem => getSystemParameterInstitutionIdentifier(systemParameterInstitutionItem)!
      );
      const systemParameterInstitutionsToAdd = systemParameterInstitutions.filter(systemParameterInstitutionItem => {
        const systemParameterInstitutionIdentifier = getSystemParameterInstitutionIdentifier(systemParameterInstitutionItem);
        if (
          systemParameterInstitutionIdentifier == null ||
          systemParameterInstitutionCollectionIdentifiers.includes(systemParameterInstitutionIdentifier)
        ) {
          return false;
        }
        systemParameterInstitutionCollectionIdentifiers.push(systemParameterInstitutionIdentifier);
        return true;
      });
      return [...systemParameterInstitutionsToAdd, ...systemParameterInstitutionCollection];
    }
    return systemParameterInstitutionCollection;
  }
}
