import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TariffComponent } from '../list/tariff.component';
import { TariffDetailComponent } from '../detail/tariff-detail.component';
import { TariffUpdateComponent } from '../update/tariff-update.component';
import { TariffRoutingResolveService } from './tariff-routing-resolve.service';

const tariffRoute: Routes = [
  {
    path: '',
    component: TariffComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TariffDetailComponent,
    resolve: {
      tariff: TariffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TariffUpdateComponent,
    resolve: {
      tariff: TariffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TariffUpdateComponent,
    resolve: {
      tariff: TariffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tariffRoute)],
  exports: [RouterModule],
})
export class TariffRoutingModule {}
