import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SystemParametersComponent } from '../list/system-parameters.component';
import { SystemParametersDetailComponent } from '../detail/system-parameters-detail.component';
import { SystemParametersUpdateComponent } from '../update/system-parameters-update.component';
import { SystemParametersRoutingResolveService } from './system-parameters-routing-resolve.service';

const systemParametersRoute: Routes = [
  {
    path: '',
    component: SystemParametersComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SystemParametersDetailComponent,
    resolve: {
      systemParameters: SystemParametersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SystemParametersUpdateComponent,
    resolve: {
      systemParameters: SystemParametersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SystemParametersUpdateComponent,
    resolve: {
      systemParameters: SystemParametersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(systemParametersRoute)],
  exports: [RouterModule],
})
export class SystemParametersRoutingModule {}
