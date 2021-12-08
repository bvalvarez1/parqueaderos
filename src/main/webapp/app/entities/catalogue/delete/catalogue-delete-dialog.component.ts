import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogue } from '../catalogue.model';
import { CatalogueService } from '../service/catalogue.service';

@Component({
  templateUrl: './catalogue-delete-dialog.component.html',
})
export class CatalogueDeleteDialogComponent {
  catalogue?: ICatalogue;

  constructor(protected catalogueService: CatalogueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
