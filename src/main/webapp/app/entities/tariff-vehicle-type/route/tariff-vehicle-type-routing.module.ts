import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TariffVehicleTypeComponent } from '../list/tariff-vehicle-type.component';
import { TariffVehicleTypeDetailComponent } from '../detail/tariff-vehicle-type-detail.component';
import { TariffVehicleTypeUpdateComponent } from '../update/tariff-vehicle-type-update.component';
import { TariffVehicleTypeRoutingResolveService } from './tariff-vehicle-type-routing-resolve.service';

const tariffVehicleTypeRoute: Routes = [
  {
    path: '',
    component: TariffVehicleTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TariffVehicleTypeDetailComponent,
    resolve: {
      tariffVehicleType: TariffVehicleTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TariffVehicleTypeUpdateComponent,
    resolve: {
      tariffVehicleType: TariffVehicleTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TariffVehicleTypeUpdateComponent,
    resolve: {
      tariffVehicleType: TariffVehicleTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tariffVehicleTypeRoute)],
  exports: [RouterModule],
})
export class TariffVehicleTypeRoutingModule {}
