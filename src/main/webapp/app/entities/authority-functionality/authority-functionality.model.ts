import { IFunctionality } from 'app/entities/functionality/functionality.model';

export interface IAuthorityFunctionality {
  id?: number;
  authority?: string;
  priority?: number;
  active?: boolean | null;
  functionality?: IFunctionality;
}

export class AuthorityFunctionality implements IAuthorityFunctionality {
  constructor(
    public id?: number,
    public authority?: string,
    public priority?: number,
    public active?: boolean | null,
    public functionality?: IFunctionality
  ) {
    this.active = this.active ?? false;
  }
}

export function getAuthorityFunctionalityIdentifier(authorityFunctionality: IAuthorityFunctionality): number | undefined {
  return authorityFunctionality.id;
}
