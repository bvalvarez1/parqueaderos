import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SystemParameterInstitutionComponent } from '../list/system-parameter-institution.component';
import { SystemParameterInstitutionDetailComponent } from '../detail/system-parameter-institution-detail.component';
import { SystemParameterInstitutionUpdateComponent } from '../update/system-parameter-institution-update.component';
import { SystemParameterInstitutionRoutingResolveService } from './system-parameter-institution-routing-resolve.service';

const systemParameterInstitutionRoute: Routes = [
  {
    path: '',
    component: SystemParameterInstitutionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SystemParameterInstitutionDetailComponent,
    resolve: {
      systemParameterInstitution: SystemParameterInstitutionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SystemParameterInstitutionUpdateComponent,
    resolve: {
      systemParameterInstitution: SystemParameterInstitutionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SystemParameterInstitutionUpdateComponent,
    resolve: {
      systemParameterInstitution: SystemParameterInstitutionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(systemParameterInstitutionRoute)],
  exports: [RouterModule],
})
export class SystemParameterInstitutionRoutingModule {}
