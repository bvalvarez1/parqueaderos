import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IRecordTicket } from 'app/entities/record-ticket/record-ticket.model';

export type EntityResponseType = HttpResponse<IRecordTicket>;

@Injectable({ providedIn: 'root' })
export class ReserveService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/record-tickets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  makeReservation(institutionId: number): Observable<EntityResponseType> {
    return this.http.post<IRecordTicket>(`${this.resourceUrl}/prereserve`, institutionId, { observe: 'response' });
  }
}
