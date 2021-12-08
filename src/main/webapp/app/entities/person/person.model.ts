import { IUser } from 'app/entities/user/user.model';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';

export interface IPerson {
  id?: number;
  fullName?: string | null;
  fistName?: string;
  surname?: string | null;
  email?: string | null;
  identificactionNumber?: string;
  telephoneNumber?: string | null;
  user?: IUser | null;
  identificationType?: IItemCatalogue;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public fullName?: string | null,
    public fistName?: string,
    public surname?: string | null,
    public email?: string | null,
    public identificactionNumber?: string,
    public telephoneNumber?: string | null,
    public user?: IUser | null,
    public identificationType?: IItemCatalogue
  ) {}
}

export function getPersonIdentifier(person: IPerson): number | undefined {
  return person.id;
}
