import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AuthorityFunctionalityComponent } from './list/authority-functionality.component';
import { AuthorityFunctionalityDetailComponent } from './detail/authority-functionality-detail.component';
import { AuthorityFunctionalityUpdateComponent } from './update/authority-functionality-update.component';
import { AuthorityFunctionalityDeleteDialogComponent } from './delete/authority-functionality-delete-dialog.component';
import { AuthorityFunctionalityRoutingModule } from './route/authority-functionality-routing.module';

@NgModule({
  imports: [SharedModule, AuthorityFunctionalityRoutingModule],
  declarations: [
    AuthorityFunctionalityComponent,
    AuthorityFunctionalityDetailComponent,
    AuthorityFunctionalityUpdateComponent,
    AuthorityFunctionalityDeleteDialogComponent,
  ],
  entryComponents: [AuthorityFunctionalityDeleteDialogComponent],
})
export class AuthorityFunctionalityModule {}
