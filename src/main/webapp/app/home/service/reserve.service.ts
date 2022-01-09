import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITariffVehicleType, getTariffVehicleTypeIdentifier } from '../../entities/tariff-vehicle-type/tariff-vehicle-type.model';
import { IRecordTicket } from 'app/entities/record-ticket/record-ticket.model';

export type EntityResponseType = HttpResponse<IRecordTicket>;
export type EntityArrayResponseType = HttpResponse<ITariffVehicleType[]>;

@Injectable({ providedIn: 'root' })
export class ReserveService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/record-tickets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  makeReservation(institutionId: number): Observable<EntityResponseType> {
    return this.http.post<IRecordTicket>(`${this.resourceUrl}/prereserve`, institutionId, { observe: 'response' });
  }
}
