import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FunctionalityComponent } from './list/functionality.component';
import { FunctionalityDetailComponent } from './detail/functionality-detail.component';
import { FunctionalityUpdateComponent } from './update/functionality-update.component';
import { FunctionalityDeleteDialogComponent } from './delete/functionality-delete-dialog.component';
import { FunctionalityRoutingModule } from './route/functionality-routing.module';

@NgModule({
  imports: [SharedModule, FunctionalityRoutingModule],
  declarations: [FunctionalityComponent, FunctionalityDetailComponent, FunctionalityUpdateComponent, FunctionalityDeleteDialogComponent],
  entryComponents: [FunctionalityDeleteDialogComponent],
})
export class FunctionalityModule {}
