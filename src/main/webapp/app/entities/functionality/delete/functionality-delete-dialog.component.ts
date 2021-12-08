import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFunctionality } from '../functionality.model';
import { FunctionalityService } from '../service/functionality.service';

@Component({
  templateUrl: './functionality-delete-dialog.component.html',
})
export class FunctionalityDeleteDialogComponent {
  functionality?: IFunctionality;

  constructor(protected functionalityService: FunctionalityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.functionalityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
