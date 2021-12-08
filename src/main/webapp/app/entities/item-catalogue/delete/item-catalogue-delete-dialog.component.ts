import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemCatalogue } from '../item-catalogue.model';
import { ItemCatalogueService } from '../service/item-catalogue.service';

@Component({
  templateUrl: './item-catalogue-delete-dialog.component.html',
})
export class ItemCatalogueDeleteDialogComponent {
  itemCatalogue?: IItemCatalogue;

  constructor(protected itemCatalogueService: ItemCatalogueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemCatalogueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
