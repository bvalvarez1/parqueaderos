import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHorary } from '../horary.model';
import { HoraryService } from '../service/horary.service';

@Component({
  templateUrl: './horary-delete-dialog.component.html',
})
export class HoraryDeleteDialogComponent {
  horary?: IHorary;

  constructor(protected horaryService: HoraryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.horaryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
