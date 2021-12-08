import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITariff, Tariff } from '../tariff.model';
import { TariffService } from '../service/tariff.service';

@Injectable({ providedIn: 'root' })
export class TariffRoutingResolveService implements Resolve<ITariff> {
  constructor(protected service: TariffService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITariff> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tariff: HttpResponse<Tariff>) => {
          if (tariff.body) {
            return of(tariff.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tariff());
  }
}
