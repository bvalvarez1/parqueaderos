import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFunctionality, getFunctionalityIdentifier } from '../functionality.model';

export type EntityResponseType = HttpResponse<IFunctionality>;
export type EntityArrayResponseType = HttpResponse<IFunctionality[]>;

@Injectable({ providedIn: 'root' })
export class FunctionalityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/functionalities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(functionality: IFunctionality): Observable<EntityResponseType> {
    return this.http.post<IFunctionality>(this.resourceUrl, functionality, { observe: 'response' });
  }

  update(functionality: IFunctionality): Observable<EntityResponseType> {
    return this.http.put<IFunctionality>(`${this.resourceUrl}/${getFunctionalityIdentifier(functionality) as number}`, functionality, {
      observe: 'response',
    });
  }

  partialUpdate(functionality: IFunctionality): Observable<EntityResponseType> {
    return this.http.patch<IFunctionality>(`${this.resourceUrl}/${getFunctionalityIdentifier(functionality) as number}`, functionality, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFunctionality>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFunctionality[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFunctionalityToCollectionIfMissing(
    functionalityCollection: IFunctionality[],
    ...functionalitiesToCheck: (IFunctionality | null | undefined)[]
  ): IFunctionality[] {
    const functionalities: IFunctionality[] = functionalitiesToCheck.filter(isPresent);
    if (functionalities.length > 0) {
      const functionalityCollectionIdentifiers = functionalityCollection.map(
        functionalityItem => getFunctionalityIdentifier(functionalityItem)!
      );
      const functionalitiesToAdd = functionalities.filter(functionalityItem => {
        const functionalityIdentifier = getFunctionalityIdentifier(functionalityItem);
        if (functionalityIdentifier == null || functionalityCollectionIdentifiers.includes(functionalityIdentifier)) {
          return false;
        }
        functionalityCollectionIdentifiers.push(functionalityIdentifier);
        return true;
      });
      return [...functionalitiesToAdd, ...functionalityCollection];
    }
    return functionalityCollection;
  }
}
