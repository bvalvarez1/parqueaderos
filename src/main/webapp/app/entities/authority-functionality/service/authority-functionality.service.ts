import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAuthorityFunctionality, getAuthorityFunctionalityIdentifier } from '../authority-functionality.model';

export type EntityResponseType = HttpResponse<IAuthorityFunctionality>;
export type EntityArrayResponseType = HttpResponse<IAuthorityFunctionality[]>;

@Injectable({ providedIn: 'root' })
export class AuthorityFunctionalityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/authority-functionalities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(authorityFunctionality: IAuthorityFunctionality): Observable<EntityResponseType> {
    return this.http.post<IAuthorityFunctionality>(this.resourceUrl, authorityFunctionality, { observe: 'response' });
  }

  update(authorityFunctionality: IAuthorityFunctionality): Observable<EntityResponseType> {
    return this.http.put<IAuthorityFunctionality>(
      `${this.resourceUrl}/${getAuthorityFunctionalityIdentifier(authorityFunctionality) as number}`,
      authorityFunctionality,
      { observe: 'response' }
    );
  }

  partialUpdate(authorityFunctionality: IAuthorityFunctionality): Observable<EntityResponseType> {
    return this.http.patch<IAuthorityFunctionality>(
      `${this.resourceUrl}/${getAuthorityFunctionalityIdentifier(authorityFunctionality) as number}`,
      authorityFunctionality,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAuthorityFunctionality>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAuthorityFunctionality[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAuthorityFunctionalityToCollectionIfMissing(
    authorityFunctionalityCollection: IAuthorityFunctionality[],
    ...authorityFunctionalitiesToCheck: (IAuthorityFunctionality | null | undefined)[]
  ): IAuthorityFunctionality[] {
    const authorityFunctionalities: IAuthorityFunctionality[] = authorityFunctionalitiesToCheck.filter(isPresent);
    if (authorityFunctionalities.length > 0) {
      const authorityFunctionalityCollectionIdentifiers = authorityFunctionalityCollection.map(
        authorityFunctionalityItem => getAuthorityFunctionalityIdentifier(authorityFunctionalityItem)!
      );
      const authorityFunctionalitiesToAdd = authorityFunctionalities.filter(authorityFunctionalityItem => {
        const authorityFunctionalityIdentifier = getAuthorityFunctionalityIdentifier(authorityFunctionalityItem);
        if (
          authorityFunctionalityIdentifier == null ||
          authorityFunctionalityCollectionIdentifiers.includes(authorityFunctionalityIdentifier)
        ) {
          return false;
        }
        authorityFunctionalityCollectionIdentifiers.push(authorityFunctionalityIdentifier);
        return true;
      });
      return [...authorityFunctionalitiesToAdd, ...authorityFunctionalityCollection];
    }
    return authorityFunctionalityCollection;
  }
}
