import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserAuthorityInstitution } from '../user-authority-institution.model';
import { UserAuthorityInstitutionService } from '../service/user-authority-institution.service';

@Component({
  templateUrl: './user-authority-institution-delete-dialog.component.html',
})
export class UserAuthorityInstitutionDeleteDialogComponent {
  userAuthorityInstitution?: IUserAuthorityInstitution;

  constructor(protected userAuthorityInstitutionService: UserAuthorityInstitutionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userAuthorityInstitutionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
