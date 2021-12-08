import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISystemParameters } from '../system-parameters.model';
import { SystemParametersService } from '../service/system-parameters.service';

@Component({
  templateUrl: './system-parameters-delete-dialog.component.html',
})
export class SystemParametersDeleteDialogComponent {
  systemParameters?: ISystemParameters;

  constructor(protected systemParametersService: SystemParametersService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.systemParametersService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
