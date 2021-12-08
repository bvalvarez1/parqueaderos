export interface IFunctionality {
  id?: number;
  name?: string;
  description?: string | null;
  icon?: string | null;
  url?: string | null;
  active?: boolean;
  childrens?: IFunctionality[] | null;
  parent?: IFunctionality | null;
}

export class Functionality implements IFunctionality {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public icon?: string | null,
    public url?: string | null,
    public active?: boolean,
    public childrens?: IFunctionality[] | null,
    public parent?: IFunctionality | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getFunctionalityIdentifier(functionality: IFunctionality): number | undefined {
  return functionality.id;
}
