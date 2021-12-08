import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalogue, Catalogue } from '../catalogue.model';
import { CatalogueService } from '../service/catalogue.service';

@Injectable({ providedIn: 'root' })
export class CatalogueRoutingResolveService implements Resolve<ICatalogue> {
  constructor(protected service: CatalogueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICatalogue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((catalogue: HttpResponse<Catalogue>) => {
          if (catalogue.body) {
            return of(catalogue.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Catalogue());
  }
}
