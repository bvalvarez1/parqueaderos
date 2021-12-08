import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITariff, getTariffIdentifier } from '../tariff.model';

export type EntityResponseType = HttpResponse<ITariff>;
export type EntityArrayResponseType = HttpResponse<ITariff[]>;

@Injectable({ providedIn: 'root' })
export class TariffService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tariffs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tariff: ITariff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tariff);
    return this.http
      .post<ITariff>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tariff: ITariff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tariff);
    return this.http
      .put<ITariff>(`${this.resourceUrl}/${getTariffIdentifier(tariff) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tariff: ITariff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tariff);
    return this.http
      .patch<ITariff>(`${this.resourceUrl}/${getTariffIdentifier(tariff) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITariff>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITariff[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTariffToCollectionIfMissing(tariffCollection: ITariff[], ...tariffsToCheck: (ITariff | null | undefined)[]): ITariff[] {
    const tariffs: ITariff[] = tariffsToCheck.filter(isPresent);
    if (tariffs.length > 0) {
      const tariffCollectionIdentifiers = tariffCollection.map(tariffItem => getTariffIdentifier(tariffItem)!);
      const tariffsToAdd = tariffs.filter(tariffItem => {
        const tariffIdentifier = getTariffIdentifier(tariffItem);
        if (tariffIdentifier == null || tariffCollectionIdentifiers.includes(tariffIdentifier)) {
          return false;
        }
        tariffCollectionIdentifiers.push(tariffIdentifier);
        return true;
      });
      return [...tariffsToAdd, ...tariffCollection];
    }
    return tariffCollection;
  }

  protected convertDateFromClient(tariff: ITariff): ITariff {
    return Object.assign({}, tariff, {
      initialDate: tariff.initialDate?.isValid() ? tariff.initialDate.format(DATE_FORMAT) : undefined,
      finalDate: tariff.finalDate?.isValid() ? tariff.finalDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.initialDate = res.body.initialDate ? dayjs(res.body.initialDate) : undefined;
      res.body.finalDate = res.body.finalDate ? dayjs(res.body.finalDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tariff: ITariff) => {
        tariff.initialDate = tariff.initialDate ? dayjs(tariff.initialDate) : undefined;
        tariff.finalDate = tariff.finalDate ? dayjs(tariff.finalDate) : undefined;
      });
    }
    return res;
  }
}
