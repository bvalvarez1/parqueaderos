import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ParkingComponent } from './list/parking.component';
import { InstitutionDetailComponent } from './detail/institution-detail.component';
import { ParkingUpdateComponent } from './update/parking-update.component';
import { TicketDeleteDialogComponent } from './delete/ticket-delete-dialog.component';
import { NewTicketComponent } from './newticket/newticket.component';
import { InstitutionRoutingModule } from './route/parking-routing.module';

@NgModule({
  imports: [SharedModule, InstitutionRoutingModule],
  declarations: [ParkingComponent, InstitutionDetailComponent, ParkingUpdateComponent, TicketDeleteDialogComponent, NewTicketComponent],
  entryComponents: [TicketDeleteDialogComponent],
})
export class ParkingModule {}
