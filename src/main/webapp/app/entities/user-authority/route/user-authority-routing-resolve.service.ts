import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserAuthority, UserAuthority } from '../user-authority.model';
import { UserAuthorityService } from '../service/user-authority.service';

@Injectable({ providedIn: 'root' })
export class UserAuthorityRoutingResolveService implements Resolve<IUserAuthority> {
  constructor(protected service: UserAuthorityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserAuthority> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userAuthority: HttpResponse<UserAuthority>) => {
          if (userAuthority.body) {
            return of(userAuthority.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserAuthority());
  }
}
