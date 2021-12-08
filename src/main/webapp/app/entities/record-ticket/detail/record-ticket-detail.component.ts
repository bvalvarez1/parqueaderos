import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecordTicket } from '../record-ticket.model';

@Component({
  selector: 'jhi-record-ticket-detail',
  templateUrl: './record-ticket-detail.component.html',
})
export class RecordTicketDetailComponent implements OnInit {
  recordTicket: IRecordTicket | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recordTicket }) => {
      this.recordTicket = recordTicket;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
