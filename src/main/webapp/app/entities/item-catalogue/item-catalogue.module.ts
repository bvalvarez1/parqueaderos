import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ItemCatalogueComponent } from './list/item-catalogue.component';
import { ItemCatalogueDetailComponent } from './detail/item-catalogue-detail.component';
import { ItemCatalogueUpdateComponent } from './update/item-catalogue-update.component';
import { ItemCatalogueDeleteDialogComponent } from './delete/item-catalogue-delete-dialog.component';
import { ItemCatalogueRoutingModule } from './route/item-catalogue-routing.module';

@NgModule({
  imports: [SharedModule, ItemCatalogueRoutingModule],
  declarations: [ItemCatalogueComponent, ItemCatalogueDetailComponent, ItemCatalogueUpdateComponent, ItemCatalogueDeleteDialogComponent],
  entryComponents: [ItemCatalogueDeleteDialogComponent],
})
export class ItemCatalogueModule {}
