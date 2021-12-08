import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItemCatalogue, ItemCatalogue } from '../item-catalogue.model';
import { ItemCatalogueService } from '../service/item-catalogue.service';

@Injectable({ providedIn: 'root' })
export class ItemCatalogueRoutingResolveService implements Resolve<IItemCatalogue> {
  constructor(protected service: ItemCatalogueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemCatalogue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((itemCatalogue: HttpResponse<ItemCatalogue>) => {
          if (itemCatalogue.body) {
            return of(itemCatalogue.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ItemCatalogue());
  }
}
