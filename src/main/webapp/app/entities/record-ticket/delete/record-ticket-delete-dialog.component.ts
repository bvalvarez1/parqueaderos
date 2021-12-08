import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecordTicket } from '../record-ticket.model';
import { RecordTicketService } from '../service/record-ticket.service';

@Component({
  templateUrl: './record-ticket-delete-dialog.component.html',
})
export class RecordTicketDeleteDialogComponent {
  recordTicket?: IRecordTicket;

  constructor(protected recordTicketService: RecordTicketService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recordTicketService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
