import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserAuthorityInstitution, getUserAuthorityInstitutionIdentifier } from '../user-authority-institution.model';

export type EntityResponseType = HttpResponse<IUserAuthorityInstitution>;
export type EntityArrayResponseType = HttpResponse<IUserAuthorityInstitution[]>;

@Injectable({ providedIn: 'root' })
export class UserAuthorityInstitutionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-authority-institutions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userAuthorityInstitution: IUserAuthorityInstitution): Observable<EntityResponseType> {
    return this.http.post<IUserAuthorityInstitution>(this.resourceUrl, userAuthorityInstitution, { observe: 'response' });
  }

  update(userAuthorityInstitution: IUserAuthorityInstitution): Observable<EntityResponseType> {
    return this.http.put<IUserAuthorityInstitution>(
      `${this.resourceUrl}/${getUserAuthorityInstitutionIdentifier(userAuthorityInstitution) as number}`,
      userAuthorityInstitution,
      { observe: 'response' }
    );
  }

  partialUpdate(userAuthorityInstitution: IUserAuthorityInstitution): Observable<EntityResponseType> {
    return this.http.patch<IUserAuthorityInstitution>(
      `${this.resourceUrl}/${getUserAuthorityInstitutionIdentifier(userAuthorityInstitution) as number}`,
      userAuthorityInstitution,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserAuthorityInstitution>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserAuthorityInstitution[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUserAuthorityInstitutionToCollectionIfMissing(
    userAuthorityInstitutionCollection: IUserAuthorityInstitution[],
    ...userAuthorityInstitutionsToCheck: (IUserAuthorityInstitution | null | undefined)[]
  ): IUserAuthorityInstitution[] {
    const userAuthorityInstitutions: IUserAuthorityInstitution[] = userAuthorityInstitutionsToCheck.filter(isPresent);
    if (userAuthorityInstitutions.length > 0) {
      const userAuthorityInstitutionCollectionIdentifiers = userAuthorityInstitutionCollection.map(
        userAuthorityInstitutionItem => getUserAuthorityInstitutionIdentifier(userAuthorityInstitutionItem)!
      );
      const userAuthorityInstitutionsToAdd = userAuthorityInstitutions.filter(userAuthorityInstitutionItem => {
        const userAuthorityInstitutionIdentifier = getUserAuthorityInstitutionIdentifier(userAuthorityInstitutionItem);
        if (
          userAuthorityInstitutionIdentifier == null ||
          userAuthorityInstitutionCollectionIdentifiers.includes(userAuthorityInstitutionIdentifier)
        ) {
          return false;
        }
        userAuthorityInstitutionCollectionIdentifiers.push(userAuthorityInstitutionIdentifier);
        return true;
      });
      return [...userAuthorityInstitutionsToAdd, ...userAuthorityInstitutionCollection];
    }
    return userAuthorityInstitutionCollection;
  }
}
