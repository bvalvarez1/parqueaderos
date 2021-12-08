import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHorary, getHoraryIdentifier } from '../horary.model';

export type EntityResponseType = HttpResponse<IHorary>;
export type EntityArrayResponseType = HttpResponse<IHorary[]>;

@Injectable({ providedIn: 'root' })
export class HoraryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/horaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(horary: IHorary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(horary);
    return this.http
      .post<IHorary>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(horary: IHorary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(horary);
    return this.http
      .put<IHorary>(`${this.resourceUrl}/${getHoraryIdentifier(horary) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(horary: IHorary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(horary);
    return this.http
      .patch<IHorary>(`${this.resourceUrl}/${getHoraryIdentifier(horary) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHorary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHorary[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHoraryToCollectionIfMissing(horaryCollection: IHorary[], ...horariesToCheck: (IHorary | null | undefined)[]): IHorary[] {
    const horaries: IHorary[] = horariesToCheck.filter(isPresent);
    if (horaries.length > 0) {
      const horaryCollectionIdentifiers = horaryCollection.map(horaryItem => getHoraryIdentifier(horaryItem)!);
      const horariesToAdd = horaries.filter(horaryItem => {
        const horaryIdentifier = getHoraryIdentifier(horaryItem);
        if (horaryIdentifier == null || horaryCollectionIdentifiers.includes(horaryIdentifier)) {
          return false;
        }
        horaryCollectionIdentifiers.push(horaryIdentifier);
        return true;
      });
      return [...horariesToAdd, ...horaryCollection];
    }
    return horaryCollection;
  }

  protected convertDateFromClient(horary: IHorary): IHorary {
    return Object.assign({}, horary, {
      startTime: horary.startTime?.isValid() ? horary.startTime.toJSON() : undefined,
      finalHour: horary.finalHour?.isValid() ? horary.finalHour.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startTime = res.body.startTime ? dayjs(res.body.startTime) : undefined;
      res.body.finalHour = res.body.finalHour ? dayjs(res.body.finalHour) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((horary: IHorary) => {
        horary.startTime = horary.startTime ? dayjs(horary.startTime) : undefined;
        horary.finalHour = horary.finalHour ? dayjs(horary.finalHour) : undefined;
      });
    }
    return res;
  }
}
