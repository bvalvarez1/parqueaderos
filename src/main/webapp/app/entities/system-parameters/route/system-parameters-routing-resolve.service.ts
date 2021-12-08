import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISystemParameters, SystemParameters } from '../system-parameters.model';
import { SystemParametersService } from '../service/system-parameters.service';

@Injectable({ providedIn: 'root' })
export class SystemParametersRoutingResolveService implements Resolve<ISystemParameters> {
  constructor(protected service: SystemParametersService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISystemParameters> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((systemParameters: HttpResponse<SystemParameters>) => {
          if (systemParameters.body) {
            return of(systemParameters.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SystemParameters());
  }
}
