import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITariffVehicleType } from '../tariff-vehicle-type.model';
import { TariffVehicleTypeService } from '../service/tariff-vehicle-type.service';

@Component({
  templateUrl: './tariff-vehicle-type-delete-dialog.component.html',
})
export class TariffVehicleTypeDeleteDialogComponent {
  tariffVehicleType?: ITariffVehicleType;

  constructor(protected tariffVehicleTypeService: TariffVehicleTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tariffVehicleTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
