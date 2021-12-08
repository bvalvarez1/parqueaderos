import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserAuthorityInstitution, UserAuthorityInstitution } from '../user-authority-institution.model';
import { UserAuthorityInstitutionService } from '../service/user-authority-institution.service';

@Injectable({ providedIn: 'root' })
export class UserAuthorityInstitutionRoutingResolveService implements Resolve<IUserAuthorityInstitution> {
  constructor(protected service: UserAuthorityInstitutionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserAuthorityInstitution> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userAuthorityInstitution: HttpResponse<UserAuthorityInstitution>) => {
          if (userAuthorityInstitution.body) {
            return of(userAuthorityInstitution.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserAuthorityInstitution());
  }
}
