import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITariffVehicleType, TariffVehicleType } from '../tariff-vehicle-type.model';
import { TariffVehicleTypeService } from '../service/tariff-vehicle-type.service';

@Injectable({ providedIn: 'root' })
export class TariffVehicleTypeRoutingResolveService implements Resolve<ITariffVehicleType> {
  constructor(protected service: TariffVehicleTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITariffVehicleType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tariffVehicleType: HttpResponse<TariffVehicleType>) => {
          if (tariffVehicleType.body) {
            return of(tariffVehicleType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TariffVehicleType());
  }
}
