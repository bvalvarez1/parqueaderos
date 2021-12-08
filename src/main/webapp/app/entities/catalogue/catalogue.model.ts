import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';

export interface ICatalogue {
  id?: number;
  name?: string;
  code?: string;
  description?: string | null;
  active?: boolean | null;
  items?: IItemCatalogue[] | null;
}

export class Catalogue implements ICatalogue {
  constructor(
    public id?: number,
    public name?: string,
    public code?: string,
    public description?: string | null,
    public active?: boolean | null,
    public items?: IItemCatalogue[] | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getCatalogueIdentifier(catalogue: ICatalogue): number | undefined {
  return catalogue.id;
}
