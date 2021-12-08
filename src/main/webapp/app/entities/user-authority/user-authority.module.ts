import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserAuthorityComponent } from './list/user-authority.component';
import { UserAuthorityDetailComponent } from './detail/user-authority-detail.component';
import { UserAuthorityUpdateComponent } from './update/user-authority-update.component';
import { UserAuthorityDeleteDialogComponent } from './delete/user-authority-delete-dialog.component';
import { UserAuthorityRoutingModule } from './route/user-authority-routing.module';

@NgModule({
  imports: [SharedModule, UserAuthorityRoutingModule],
  declarations: [UserAuthorityComponent, UserAuthorityDetailComponent, UserAuthorityUpdateComponent, UserAuthorityDeleteDialogComponent],
  entryComponents: [UserAuthorityDeleteDialogComponent],
})
export class UserAuthorityModule {}
