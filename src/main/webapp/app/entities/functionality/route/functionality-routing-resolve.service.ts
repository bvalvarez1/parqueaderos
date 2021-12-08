import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFunctionality, Functionality } from '../functionality.model';
import { FunctionalityService } from '../service/functionality.service';

@Injectable({ providedIn: 'root' })
export class FunctionalityRoutingResolveService implements Resolve<IFunctionality> {
  constructor(protected service: FunctionalityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFunctionality> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((functionality: HttpResponse<Functionality>) => {
          if (functionality.body) {
            return of(functionality.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Functionality());
  }
}
