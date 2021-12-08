import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserAuthorityInstitutionComponent } from './list/user-authority-institution.component';
import { UserAuthorityInstitutionDetailComponent } from './detail/user-authority-institution-detail.component';
import { UserAuthorityInstitutionUpdateComponent } from './update/user-authority-institution-update.component';
import { UserAuthorityInstitutionDeleteDialogComponent } from './delete/user-authority-institution-delete-dialog.component';
import { UserAuthorityInstitutionRoutingModule } from './route/user-authority-institution-routing.module';

@NgModule({
  imports: [SharedModule, UserAuthorityInstitutionRoutingModule],
  declarations: [
    UserAuthorityInstitutionComponent,
    UserAuthorityInstitutionDetailComponent,
    UserAuthorityInstitutionUpdateComponent,
    UserAuthorityInstitutionDeleteDialogComponent,
  ],
  entryComponents: [UserAuthorityInstitutionDeleteDialogComponent],
})
export class UserAuthorityInstitutionModule {}
