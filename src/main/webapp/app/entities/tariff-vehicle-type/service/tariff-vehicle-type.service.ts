import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITariffVehicleType, getTariffVehicleTypeIdentifier } from '../tariff-vehicle-type.model';

export type EntityResponseType = HttpResponse<ITariffVehicleType>;
export type EntityArrayResponseType = HttpResponse<ITariffVehicleType[]>;

@Injectable({ providedIn: 'root' })
export class TariffVehicleTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tariff-vehicle-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tariffVehicleType: ITariffVehicleType): Observable<EntityResponseType> {
    return this.http.post<ITariffVehicleType>(this.resourceUrl, tariffVehicleType, { observe: 'response' });
  }

  update(tariffVehicleType: ITariffVehicleType): Observable<EntityResponseType> {
    return this.http.put<ITariffVehicleType>(
      `${this.resourceUrl}/${getTariffVehicleTypeIdentifier(tariffVehicleType) as number}`,
      tariffVehicleType,
      { observe: 'response' }
    );
  }

  partialUpdate(tariffVehicleType: ITariffVehicleType): Observable<EntityResponseType> {
    return this.http.patch<ITariffVehicleType>(
      `${this.resourceUrl}/${getTariffVehicleTypeIdentifier(tariffVehicleType) as number}`,
      tariffVehicleType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITariffVehicleType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITariffVehicleType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTariffVehicleTypeToCollectionIfMissing(
    tariffVehicleTypeCollection: ITariffVehicleType[],
    ...tariffVehicleTypesToCheck: (ITariffVehicleType | null | undefined)[]
  ): ITariffVehicleType[] {
    const tariffVehicleTypes: ITariffVehicleType[] = tariffVehicleTypesToCheck.filter(isPresent);
    if (tariffVehicleTypes.length > 0) {
      const tariffVehicleTypeCollectionIdentifiers = tariffVehicleTypeCollection.map(
        tariffVehicleTypeItem => getTariffVehicleTypeIdentifier(tariffVehicleTypeItem)!
      );
      const tariffVehicleTypesToAdd = tariffVehicleTypes.filter(tariffVehicleTypeItem => {
        const tariffVehicleTypeIdentifier = getTariffVehicleTypeIdentifier(tariffVehicleTypeItem);
        if (tariffVehicleTypeIdentifier == null || tariffVehicleTypeCollectionIdentifiers.includes(tariffVehicleTypeIdentifier)) {
          return false;
        }
        tariffVehicleTypeCollectionIdentifiers.push(tariffVehicleTypeIdentifier);
        return true;
      });
      return [...tariffVehicleTypesToAdd, ...tariffVehicleTypeCollection];
    }
    return tariffVehicleTypeCollection;
  }
}
