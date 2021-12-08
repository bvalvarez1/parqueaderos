import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IItemCatalogue, getItemCatalogueIdentifier } from '../item-catalogue.model';

export type EntityResponseType = HttpResponse<IItemCatalogue>;
export type EntityArrayResponseType = HttpResponse<IItemCatalogue[]>;

@Injectable({ providedIn: 'root' })
export class ItemCatalogueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/item-catalogues');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(itemCatalogue: IItemCatalogue): Observable<EntityResponseType> {
    return this.http.post<IItemCatalogue>(this.resourceUrl, itemCatalogue, { observe: 'response' });
  }

  update(itemCatalogue: IItemCatalogue): Observable<EntityResponseType> {
    return this.http.put<IItemCatalogue>(`${this.resourceUrl}/${getItemCatalogueIdentifier(itemCatalogue) as number}`, itemCatalogue, {
      observe: 'response',
    });
  }

  partialUpdate(itemCatalogue: IItemCatalogue): Observable<EntityResponseType> {
    return this.http.patch<IItemCatalogue>(`${this.resourceUrl}/${getItemCatalogueIdentifier(itemCatalogue) as number}`, itemCatalogue, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IItemCatalogue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IItemCatalogue[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addItemCatalogueToCollectionIfMissing(
    itemCatalogueCollection: IItemCatalogue[],
    ...itemCataloguesToCheck: (IItemCatalogue | null | undefined)[]
  ): IItemCatalogue[] {
    const itemCatalogues: IItemCatalogue[] = itemCataloguesToCheck.filter(isPresent);
    if (itemCatalogues.length > 0) {
      const itemCatalogueCollectionIdentifiers = itemCatalogueCollection.map(
        itemCatalogueItem => getItemCatalogueIdentifier(itemCatalogueItem)!
      );
      const itemCataloguesToAdd = itemCatalogues.filter(itemCatalogueItem => {
        const itemCatalogueIdentifier = getItemCatalogueIdentifier(itemCatalogueItem);
        if (itemCatalogueIdentifier == null || itemCatalogueCollectionIdentifiers.includes(itemCatalogueIdentifier)) {
          return false;
        }
        itemCatalogueCollectionIdentifiers.push(itemCatalogueIdentifier);
        return true;
      });
      return [...itemCataloguesToAdd, ...itemCatalogueCollection];
    }
    return itemCatalogueCollection;
  }
}
