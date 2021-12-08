import { ICatalogue } from 'app/entities/catalogue/catalogue.model';

export interface IItemCatalogue {
  id?: number;
  name?: string;
  code?: string;
  description?: string | null;
  catalogCode?: string;
  active?: boolean | null;
  catalogue?: ICatalogue | null;
}

export class ItemCatalogue implements IItemCatalogue {
  constructor(
    public id?: number,
    public name?: string,
    public code?: string,
    public description?: string | null,
    public catalogCode?: string,
    public active?: boolean | null,
    public catalogue?: ICatalogue | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getItemCatalogueIdentifier(itemCatalogue: IItemCatalogue): number | undefined {
  return itemCatalogue.id;
}
