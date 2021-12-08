import * as dayjs from 'dayjs';
import { ITariffVehicleType } from 'app/entities/tariff-vehicle-type/tariff-vehicle-type.model';
import { IInstitution } from 'app/entities/institution/institution.model';

export interface ITariff {
  id?: number;
  initialDate?: dayjs.Dayjs;
  finalDate?: dayjs.Dayjs | null;
  tarifs?: ITariffVehicleType[] | null;
  institution?: IInstitution;
}

export class Tariff implements ITariff {
  constructor(
    public id?: number,
    public initialDate?: dayjs.Dayjs,
    public finalDate?: dayjs.Dayjs | null,
    public tarifs?: ITariffVehicleType[] | null,
    public institution?: IInstitution
  ) {}
}

export function getTariffIdentifier(tariff: ITariff): number | undefined {
  return tariff.id;
}
