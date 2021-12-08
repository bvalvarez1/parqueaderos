import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICatalogue, getCatalogueIdentifier } from '../catalogue.model';

export type EntityResponseType = HttpResponse<ICatalogue>;
export type EntityArrayResponseType = HttpResponse<ICatalogue[]>;

@Injectable({ providedIn: 'root' })
export class CatalogueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/catalogues');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(catalogue: ICatalogue): Observable<EntityResponseType> {
    return this.http.post<ICatalogue>(this.resourceUrl, catalogue, { observe: 'response' });
  }

  update(catalogue: ICatalogue): Observable<EntityResponseType> {
    return this.http.put<ICatalogue>(`${this.resourceUrl}/${getCatalogueIdentifier(catalogue) as number}`, catalogue, {
      observe: 'response',
    });
  }

  partialUpdate(catalogue: ICatalogue): Observable<EntityResponseType> {
    return this.http.patch<ICatalogue>(`${this.resourceUrl}/${getCatalogueIdentifier(catalogue) as number}`, catalogue, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICatalogue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICatalogue[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCatalogueToCollectionIfMissing(
    catalogueCollection: ICatalogue[],
    ...cataloguesToCheck: (ICatalogue | null | undefined)[]
  ): ICatalogue[] {
    const catalogues: ICatalogue[] = cataloguesToCheck.filter(isPresent);
    if (catalogues.length > 0) {
      const catalogueCollectionIdentifiers = catalogueCollection.map(catalogueItem => getCatalogueIdentifier(catalogueItem)!);
      const cataloguesToAdd = catalogues.filter(catalogueItem => {
        const catalogueIdentifier = getCatalogueIdentifier(catalogueItem);
        if (catalogueIdentifier == null || catalogueCollectionIdentifiers.includes(catalogueIdentifier)) {
          return false;
        }
        catalogueCollectionIdentifiers.push(catalogueIdentifier);
        return true;
      });
      return [...cataloguesToAdd, ...catalogueCollection];
    }
    return catalogueCollection;
  }
}
