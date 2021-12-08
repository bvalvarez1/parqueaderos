import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FunctionalityComponent } from '../list/functionality.component';
import { FunctionalityDetailComponent } from '../detail/functionality-detail.component';
import { FunctionalityUpdateComponent } from '../update/functionality-update.component';
import { FunctionalityRoutingResolveService } from './functionality-routing-resolve.service';

const functionalityRoute: Routes = [
  {
    path: '',
    component: FunctionalityComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FunctionalityDetailComponent,
    resolve: {
      functionality: FunctionalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FunctionalityUpdateComponent,
    resolve: {
      functionality: FunctionalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FunctionalityUpdateComponent,
    resolve: {
      functionality: FunctionalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(functionalityRoute)],
  exports: [RouterModule],
})
export class FunctionalityRoutingModule {}
