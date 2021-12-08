import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecord } from '../record.model';
import { RecordService } from '../service/record.service';
import { RecordDeleteDialogComponent } from '../delete/record-delete-dialog.component';

@Component({
  selector: 'jhi-record',
  templateUrl: './record.component.html',
})
export class RecordComponent implements OnInit {
  records?: IRecord[];
  isLoading = false;

  constructor(protected recordService: RecordService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.recordService.query().subscribe(
      (res: HttpResponse<IRecord[]>) => {
        this.isLoading = false;
        this.records = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IRecord): number {
    return item.id!;
  }

  delete(record: IRecord): void {
    const modalRef = this.modalService.open(RecordDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.record = record;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
