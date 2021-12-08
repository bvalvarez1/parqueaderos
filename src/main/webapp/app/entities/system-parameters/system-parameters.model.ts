export interface ISystemParameters {
  id?: number;
  name?: string;
  code?: string;
  clase?: string;
}

export class SystemParameters implements ISystemParameters {
  constructor(public id?: number, public name?: string, public code?: string, public clase?: string) {}
}

export function getSystemParametersIdentifier(systemParameters: ISystemParameters): number | undefined {
  return systemParameters.id;
}
