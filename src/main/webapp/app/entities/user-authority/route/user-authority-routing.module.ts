import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserAuthorityComponent } from '../list/user-authority.component';
import { UserAuthorityDetailComponent } from '../detail/user-authority-detail.component';
import { UserAuthorityUpdateComponent } from '../update/user-authority-update.component';
import { UserAuthorityRoutingResolveService } from './user-authority-routing-resolve.service';

const userAuthorityRoute: Routes = [
  {
    path: '',
    component: UserAuthorityComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserAuthorityDetailComponent,
    resolve: {
      userAuthority: UserAuthorityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserAuthorityUpdateComponent,
    resolve: {
      userAuthority: UserAuthorityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserAuthorityUpdateComponent,
    resolve: {
      userAuthority: UserAuthorityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userAuthorityRoute)],
  exports: [RouterModule],
})
export class UserAuthorityRoutingModule {}
