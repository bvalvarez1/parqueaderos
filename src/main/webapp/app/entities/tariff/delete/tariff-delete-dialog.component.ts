import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITariff } from '../tariff.model';
import { TariffService } from '../service/tariff.service';

@Component({
  templateUrl: './tariff-delete-dialog.component.html',
})
export class TariffDeleteDialogComponent {
  tariff?: ITariff;

  constructor(protected tariffService: TariffService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tariffService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
