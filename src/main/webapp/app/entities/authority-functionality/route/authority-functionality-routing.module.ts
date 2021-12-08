import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AuthorityFunctionalityComponent } from '../list/authority-functionality.component';
import { AuthorityFunctionalityDetailComponent } from '../detail/authority-functionality-detail.component';
import { AuthorityFunctionalityUpdateComponent } from '../update/authority-functionality-update.component';
import { AuthorityFunctionalityRoutingResolveService } from './authority-functionality-routing-resolve.service';

const authorityFunctionalityRoute: Routes = [
  {
    path: '',
    component: AuthorityFunctionalityComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AuthorityFunctionalityDetailComponent,
    resolve: {
      authorityFunctionality: AuthorityFunctionalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AuthorityFunctionalityUpdateComponent,
    resolve: {
      authorityFunctionality: AuthorityFunctionalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AuthorityFunctionalityUpdateComponent,
    resolve: {
      authorityFunctionality: AuthorityFunctionalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(authorityFunctionalityRoute)],
  exports: [RouterModule],
})
export class AuthorityFunctionalityRoutingModule {}
