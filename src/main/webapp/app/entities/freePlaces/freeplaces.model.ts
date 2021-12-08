import { IPlace } from 'app/entities/place/place.model';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';

export interface IFreePlaces {
  id?: number;
  name?: string;
  sequencename?: string;
  latitude?: number;
  longitude?: number;
  availables?: number | null;
}

export class FreePlaces implements IFreePlaces {
  constructor(
    public id?: number,
    public name?: string,
    public sequencename?: string,
    public latitude?: number,
    public longitude?: number,
    public availables?: number | null
  ) {}
}

export function getFreePlacesIdentifier(freeplaces: FreePlaces): number | undefined {
  return freeplaces.id;
}
