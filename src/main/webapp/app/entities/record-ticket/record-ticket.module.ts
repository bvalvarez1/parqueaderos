import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RecordTicketComponent } from './list/record-ticket.component';
import { RecordTicketDetailComponent } from './detail/record-ticket-detail.component';
import { RecordTicketUpdateComponent } from './update/record-ticket-update.component';
import { RecordTicketDeleteDialogComponent } from './delete/record-ticket-delete-dialog.component';
import { RecordTicketRoutingModule } from './route/record-ticket-routing.module';

@NgModule({
  imports: [SharedModule, RecordTicketRoutingModule],
  declarations: [RecordTicketComponent, RecordTicketDetailComponent, RecordTicketUpdateComponent, RecordTicketDeleteDialogComponent],
  entryComponents: [RecordTicketDeleteDialogComponent],
})
export class RecordTicketModule {}
