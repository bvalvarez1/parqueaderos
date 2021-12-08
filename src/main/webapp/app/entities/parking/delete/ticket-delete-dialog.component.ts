import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IRecordTicket } from 'app/entities/record-ticket/record-ticket.model';
import { RecordTicketService } from 'app/entities/record-ticket/service/record-ticket.service';

import { IInstitution } from '../parking.model';
import { ParkingService } from '../service/parking.service';

@Component({
  templateUrl: './ticket-delete-dialog.component.html',
})
export class TicketDeleteDialogComponent {
  ticket?: IRecordTicket;

  constructor(protected recordTicketService: RecordTicketService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(): void {
    this.recordTicketService.cancelTicket(this.ticket!).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
