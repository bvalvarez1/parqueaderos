import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserAuthority } from '../user-authority.model';
import { UserAuthorityService } from '../service/user-authority.service';

@Component({
  templateUrl: './user-authority-delete-dialog.component.html',
})
export class UserAuthorityDeleteDialogComponent {
  userAuthority?: IUserAuthority;

  constructor(protected userAuthorityService: UserAuthorityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userAuthorityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
