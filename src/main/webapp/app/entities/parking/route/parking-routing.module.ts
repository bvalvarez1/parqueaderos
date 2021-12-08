import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ParkingComponent } from '../list/parking.component';
import { InstitutionDetailComponent } from '../detail/institution-detail.component';
import { ParkingUpdateComponent } from '../update/parking-update.component';
import { InstitutionRoutingResolveService } from './parking-routing-resolve.service';

const parkingRoute: Routes = [
  {
    path: '',
    component: ParkingComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InstitutionDetailComponent,
    resolve: {
      institution: InstitutionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParkingUpdateComponent,
    resolve: {
      institution: InstitutionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParkingUpdateComponent,
    resolve: {
      institution: InstitutionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(parkingRoute)],
  exports: [RouterModule],
})
export class InstitutionRoutingModule {}
