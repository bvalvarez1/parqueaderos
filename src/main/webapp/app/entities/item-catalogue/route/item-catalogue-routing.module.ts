import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ItemCatalogueComponent } from '../list/item-catalogue.component';
import { ItemCatalogueDetailComponent } from '../detail/item-catalogue-detail.component';
import { ItemCatalogueUpdateComponent } from '../update/item-catalogue-update.component';
import { ItemCatalogueRoutingResolveService } from './item-catalogue-routing-resolve.service';

const itemCatalogueRoute: Routes = [
  {
    path: '',
    component: ItemCatalogueComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ItemCatalogueDetailComponent,
    resolve: {
      itemCatalogue: ItemCatalogueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ItemCatalogueUpdateComponent,
    resolve: {
      itemCatalogue: ItemCatalogueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ItemCatalogueUpdateComponent,
    resolve: {
      itemCatalogue: ItemCatalogueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(itemCatalogueRoute)],
  exports: [RouterModule],
})
export class ItemCatalogueRoutingModule {}
