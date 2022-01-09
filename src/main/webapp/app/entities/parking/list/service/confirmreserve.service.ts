import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRecordTicket } from 'app/entities/record-ticket/record-ticket.model';

export type EntityResponseType = HttpResponse<IRecordTicket>;

@Injectable({ providedIn: 'root' })
export class ConfirmReserveService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/record-tickets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  confirmReservation(ticket: IRecordTicket): Observable<EntityResponseType> {
    return this.http.post<IRecordTicket>(`${this.resourceUrl}/confirmReserve`, ticket, { observe: 'response' });
  }

  denyReservation(ticket: IRecordTicket): Observable<EntityResponseType> {
    return this.http.post<IRecordTicket>(`${this.resourceUrl}/denyReserve`, ticket, { observe: 'response' });
  }
}
