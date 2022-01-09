import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserAuthority, getUserAuthorityIdentifier } from '../user-authority.model';

export type EntityResponseType = HttpResponse<IUserAuthority>;
export type EntityArrayResponseType = HttpResponse<IUserAuthority[]>;

@Injectable({ providedIn: 'root' })
export class UserAuthorityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-authorities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userAuthority: IUserAuthority): Observable<EntityResponseType> {
    return this.http.post<IUserAuthority>(this.resourceUrl, userAuthority, { observe: 'response' });
  }

  update(userAuthority: IUserAuthority): Observable<EntityResponseType> {
    return this.http.put<IUserAuthority>(`${this.resourceUrl}/${getUserAuthorityIdentifier(userAuthority) as number}`, userAuthority, {
      observe: 'response',
    });
  }

  partialUpdate(userAuthority: IUserAuthority): Observable<EntityResponseType> {
    return this.http.patch<IUserAuthority>(`${this.resourceUrl}/${getUserAuthorityIdentifier(userAuthority) as number}`, userAuthority, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserAuthority>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserAuthority[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  findByUserid(userid: number): Observable<EntityResponseType> {
    return this.http.post<IUserAuthority>(`${this.resourceUrl}/findByUserid`, userid, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUserAuthorityToCollectionIfMissing(
    userAuthorityCollection: IUserAuthority[],
    ...userAuthoritiesToCheck: (IUserAuthority | null | undefined)[]
  ): IUserAuthority[] {
    const userAuthorities: IUserAuthority[] = userAuthoritiesToCheck.filter(isPresent);
    if (userAuthorities.length > 0) {
      const userAuthorityCollectionIdentifiers = userAuthorityCollection.map(
        userAuthorityItem => getUserAuthorityIdentifier(userAuthorityItem)!
      );
      const userAuthoritiesToAdd = userAuthorities.filter(userAuthorityItem => {
        const userAuthorityIdentifier = getUserAuthorityIdentifier(userAuthorityItem);
        if (userAuthorityIdentifier == null || userAuthorityCollectionIdentifiers.includes(userAuthorityIdentifier)) {
          return false;
        }
        userAuthorityCollectionIdentifiers.push(userAuthorityIdentifier);
        return true;
      });
      return [...userAuthoritiesToAdd, ...userAuthorityCollection];
    }
    return userAuthorityCollection;
  }
}
