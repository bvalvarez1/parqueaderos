import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

//import { ITariffVehicleType } from '../tariff-vehicle-type.model';
import { ReserveService } from '../service/reserve.service';

@Component({
  templateUrl: './reserve-dialog.component.html',
})
export class ReserveDialogComponent {
  institutionId?: number;

  constructor(protected reserveService: ReserveService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  preReserve(): void {
    // eslint-disable-next-line no-console
    console.log('________');

    //this.activeModal.dismiss();
    this.reserveService.makeReservation(this.institutionId!).subscribe(() => {
      this.activeModal.close('prereserved');
    });
  }
}
