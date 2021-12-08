import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserAuthorityInstitutionComponent } from '../list/user-authority-institution.component';
import { UserAuthorityInstitutionDetailComponent } from '../detail/user-authority-institution-detail.component';
import { UserAuthorityInstitutionUpdateComponent } from '../update/user-authority-institution-update.component';
import { UserAuthorityInstitutionRoutingResolveService } from './user-authority-institution-routing-resolve.service';

const userAuthorityInstitutionRoute: Routes = [
  {
    path: '',
    component: UserAuthorityInstitutionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserAuthorityInstitutionDetailComponent,
    resolve: {
      userAuthorityInstitution: UserAuthorityInstitutionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserAuthorityInstitutionUpdateComponent,
    resolve: {
      userAuthorityInstitution: UserAuthorityInstitutionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserAuthorityInstitutionUpdateComponent,
    resolve: {
      userAuthorityInstitution: UserAuthorityInstitutionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userAuthorityInstitutionRoute)],
  exports: [RouterModule],
})
export class UserAuthorityInstitutionRoutingModule {}
