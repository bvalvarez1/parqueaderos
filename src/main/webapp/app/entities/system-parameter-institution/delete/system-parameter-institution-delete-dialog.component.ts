import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISystemParameterInstitution } from '../system-parameter-institution.model';
import { SystemParameterInstitutionService } from '../service/system-parameter-institution.service';

@Component({
  templateUrl: './system-parameter-institution-delete-dialog.component.html',
})
export class SystemParameterInstitutionDeleteDialogComponent {
  systemParameterInstitution?: ISystemParameterInstitution;

  constructor(protected systemParameterInstitutionService: SystemParameterInstitutionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.systemParameterInstitutionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
