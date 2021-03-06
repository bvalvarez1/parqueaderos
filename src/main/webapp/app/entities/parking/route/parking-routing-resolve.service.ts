import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInstitution, Institution } from '../parking.model';
import { ParkingService } from '../service/parking.service';

@Injectable({ providedIn: 'root' })
export class InstitutionRoutingResolveService implements Resolve<IInstitution> {
  constructor(protected service: ParkingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInstitution> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((institution: HttpResponse<Institution>) => {
          if (institution.body) {
            return of(institution.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Institution());
  }
}
