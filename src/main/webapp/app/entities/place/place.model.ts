import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { IInstitution } from 'app/entities/institution/institution.model';

export interface IPlace {
  id?: number;
  number?: string;
  status?: IItemCatalogue | null;
  institution?: IInstitution | null;
}

export class Place implements IPlace {
  constructor(
    public id?: number,
    public number?: string,
    public status?: IItemCatalogue | null,
    public institution?: IInstitution | null
  ) {}
}

export function getPlaceIdentifier(place: IPlace): number | undefined {
  return place.id;
}
