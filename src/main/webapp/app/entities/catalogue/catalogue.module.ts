import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CatalogueComponent } from './list/catalogue.component';
import { CatalogueDetailComponent } from './detail/catalogue-detail.component';
import { CatalogueUpdateComponent } from './update/catalogue-update.component';
import { CatalogueDeleteDialogComponent } from './delete/catalogue-delete-dialog.component';
import { CatalogueRoutingModule } from './route/catalogue-routing.module';

@NgModule({
  imports: [SharedModule, CatalogueRoutingModule],
  declarations: [CatalogueComponent, CatalogueDetailComponent, CatalogueUpdateComponent, CatalogueDeleteDialogComponent],
  entryComponents: [CatalogueDeleteDialogComponent],
})
export class CatalogueModule {}
