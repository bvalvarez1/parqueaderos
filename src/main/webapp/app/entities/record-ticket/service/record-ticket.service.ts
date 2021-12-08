import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRecordTicket, getRecordTicketIdentifier } from '../record-ticket.model';

export type EntityResponseType = HttpResponse<IRecordTicket>;
export type EntityArrayResponseType = HttpResponse<IRecordTicket[]>;

@Injectable({ providedIn: 'root' })
export class RecordTicketService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/record-tickets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(recordTicket: IRecordTicket): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recordTicket);
    return this.http
      .post<IRecordTicket>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  createFromParking(recordTicket: IRecordTicket): Observable<EntityResponseType> {
    // eslint-disable-next-line no-console
    console.log('Serviciooooo ');
    // eslint-disable-next-line no-console
    console.log(recordTicket.institution);
    return this.http
      .post<IRecordTicket>(this.resourceUrl + '/record-tickets-parking', recordTicket, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(recordTicket: IRecordTicket): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recordTicket);
    return this.http
      .put<IRecordTicket>(`${this.resourceUrl}/${getRecordTicketIdentifier(recordTicket) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(recordTicket: IRecordTicket): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recordTicket);
    return this.http
      .patch<IRecordTicket>(`${this.resourceUrl}/${getRecordTicketIdentifier(recordTicket) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRecordTicket>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findByTicket(ticket: string, idInstitution: number): Observable<EntityResponseType> {
    const ticketRequest = {
      sequential: ticket,
      institutionid: idInstitution,
    };

    return this.http
      .post<IRecordTicket>(`${this.resourceUrl}/findByserialAndInstitution`, ticketRequest, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  /**
   * Pagar el ticket
   * @param ticketForPay
   * @returns
   */
  payTicket(ticketForPay: IRecordTicket): Observable<EntityResponseType> {
    return this.http
      .post<IRecordTicket>(`${this.resourceUrl}/payTicket`, ticketForPay, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  /**
   *
   * @param recordTicket
   * @returns
   */
  cancelTicket(ticket: IRecordTicket): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ticket);
    return this.http
      .patch<IRecordTicket>(`${this.resourceUrl}/cancel/${getRecordTicketIdentifier(ticket) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRecordTicket[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRecordTicketToCollectionIfMissing(
    recordTicketCollection: IRecordTicket[],
    ...recordTicketsToCheck: (IRecordTicket | null | undefined)[]
  ): IRecordTicket[] {
    const recordTickets: IRecordTicket[] = recordTicketsToCheck.filter(isPresent);
    if (recordTickets.length > 0) {
      const recordTicketCollectionIdentifiers = recordTicketCollection.map(
        recordTicketItem => getRecordTicketIdentifier(recordTicketItem)!
      );
      const recordTicketsToAdd = recordTickets.filter(recordTicketItem => {
        const recordTicketIdentifier = getRecordTicketIdentifier(recordTicketItem);
        if (recordTicketIdentifier == null || recordTicketCollectionIdentifiers.includes(recordTicketIdentifier)) {
          return false;
        }
        recordTicketCollectionIdentifiers.push(recordTicketIdentifier);
        return true;
      });
      return [...recordTicketsToAdd, ...recordTicketCollection];
    }
    return recordTicketCollection;
  }

  protected convertDateFromClient(recordTicket: IRecordTicket): IRecordTicket {
    return Object.assign({}, recordTicket, {
      initDate: recordTicket.initDate?.isValid() ? recordTicket.initDate.toJSON() : undefined,
      endDate: recordTicket.endDate?.isValid() ? recordTicket.endDate.toJSON() : undefined,
      parkingTime: recordTicket.parkingTime?.isValid() ? recordTicket.parkingTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.initDate = res.body.initDate ? dayjs(res.body.initDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
      res.body.parkingTime = res.body.parkingTime ? dayjs(res.body.parkingTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((recordTicket: IRecordTicket) => {
        recordTicket.initDate = recordTicket.initDate ? dayjs(recordTicket.initDate) : undefined;
        recordTicket.endDate = recordTicket.endDate ? dayjs(recordTicket.endDate) : undefined;
        recordTicket.parkingTime = recordTicket.parkingTime ? dayjs(recordTicket.parkingTime) : undefined;
      });
    }
    return res;
  }
}
