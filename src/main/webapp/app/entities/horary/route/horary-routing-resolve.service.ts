import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHorary, Horary } from '../horary.model';
import { HoraryService } from '../service/horary.service';

@Injectable({ providedIn: 'root' })
export class HoraryRoutingResolveService implements Resolve<IHorary> {
  constructor(protected service: HoraryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHorary> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((horary: HttpResponse<Horary>) => {
          if (horary.body) {
            return of(horary.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Horary());
  }
}
