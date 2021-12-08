import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAuthorityFunctionality } from '../authority-functionality.model';
import { AuthorityFunctionalityService } from '../service/authority-functionality.service';

@Component({
  templateUrl: './authority-functionality-delete-dialog.component.html',
})
export class AuthorityFunctionalityDeleteDialogComponent {
  authorityFunctionality?: IAuthorityFunctionality;

  constructor(protected authorityFunctionalityService: AuthorityFunctionalityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.authorityFunctionalityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
