import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IRecordTicket } from 'app/entities/record-ticket/record-ticket.model';

import { ConfirmReserveService } from '../service/confirmreserve.service';

@Component({
  templateUrl: './confirmreserve-dialog.component.html',
})
export class ConfirmReserveDialogComponent {
  ticket?: IRecordTicket;

  constructor(protected confirmReserveService: ConfirmReserveService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    //this.activeModal.dismiss();
    this.confirmReserveService.denyReservation(this.ticket!).subscribe(() => {
      this.activeModal.close('confirmed');
    });
  }

  confirm(): void {
    // eslint-disable-next-line no-console
    console.log('________');
    //this.activeModal.dismiss();
    this.confirmReserveService.confirmReservation(this.ticket!).subscribe(() => {
      this.activeModal.close('confirmed');
    });
  }
}
