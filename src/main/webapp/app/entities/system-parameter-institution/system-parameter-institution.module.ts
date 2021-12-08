import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SystemParameterInstitutionComponent } from './list/system-parameter-institution.component';
import { SystemParameterInstitutionDetailComponent } from './detail/system-parameter-institution-detail.component';
import { SystemParameterInstitutionUpdateComponent } from './update/system-parameter-institution-update.component';
import { SystemParameterInstitutionDeleteDialogComponent } from './delete/system-parameter-institution-delete-dialog.component';
import { SystemParameterInstitutionRoutingModule } from './route/system-parameter-institution-routing.module';

@NgModule({
  imports: [SharedModule, SystemParameterInstitutionRoutingModule],
  declarations: [
    SystemParameterInstitutionComponent,
    SystemParameterInstitutionDetailComponent,
    SystemParameterInstitutionUpdateComponent,
    SystemParameterInstitutionDeleteDialogComponent,
  ],
  entryComponents: [SystemParameterInstitutionDeleteDialogComponent],
})
export class SystemParameterInstitutionModule {}
