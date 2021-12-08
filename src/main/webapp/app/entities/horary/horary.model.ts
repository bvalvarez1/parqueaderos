import * as dayjs from 'dayjs';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { IContract } from 'app/entities/contract/contract.model';

export interface IHorary {
  id?: number;
  name?: string;
  startTime?: dayjs.Dayjs;
  finalHour?: dayjs.Dayjs;
  status?: IItemCatalogue;
  contract?: IContract | null;
}

export class Horary implements IHorary {
  constructor(
    public id?: number,
    public name?: string,
    public startTime?: dayjs.Dayjs,
    public finalHour?: dayjs.Dayjs,
    public status?: IItemCatalogue,
    public contract?: IContract | null
  ) {}
}

export function getHoraryIdentifier(horary: IHorary): number | undefined {
  return horary.id;
}
