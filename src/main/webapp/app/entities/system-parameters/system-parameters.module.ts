import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SystemParametersComponent } from './list/system-parameters.component';
import { SystemParametersDetailComponent } from './detail/system-parameters-detail.component';
import { SystemParametersUpdateComponent } from './update/system-parameters-update.component';
import { SystemParametersDeleteDialogComponent } from './delete/system-parameters-delete-dialog.component';
import { SystemParametersRoutingModule } from './route/system-parameters-routing.module';

@NgModule({
  imports: [SharedModule, SystemParametersRoutingModule],
  declarations: [
    SystemParametersComponent,
    SystemParametersDetailComponent,
    SystemParametersUpdateComponent,
    SystemParametersDeleteDialogComponent,
  ],
  entryComponents: [SystemParametersDeleteDialogComponent],
})
export class SystemParametersModule {}
