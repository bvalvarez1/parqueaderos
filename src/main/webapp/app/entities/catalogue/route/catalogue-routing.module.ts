import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CatalogueComponent } from '../list/catalogue.component';
import { CatalogueDetailComponent } from '../detail/catalogue-detail.component';
import { CatalogueUpdateComponent } from '../update/catalogue-update.component';
import { CatalogueRoutingResolveService } from './catalogue-routing-resolve.service';

const catalogueRoute: Routes = [
  {
    path: '',
    component: CatalogueComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CatalogueDetailComponent,
    resolve: {
      catalogue: CatalogueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalogueUpdateComponent,
    resolve: {
      catalogue: CatalogueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CatalogueUpdateComponent,
    resolve: {
      catalogue: CatalogueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(catalogueRoute)],
  exports: [RouterModule],
})
export class CatalogueRoutingModule {}
