import { ISystemParameters } from 'app/entities/system-parameters/system-parameters.model';
import { IInstitution } from 'app/entities/institution/institution.model';

export interface ISystemParameterInstitution {
  id?: number;
  value?: string;
  active?: boolean | null;
  parameter?: ISystemParameters;
  institution?: IInstitution;
}

export class SystemParameterInstitution implements ISystemParameterInstitution {
  constructor(
    public id?: number,
    public value?: string,
    public active?: boolean | null,
    public parameter?: ISystemParameters,
    public institution?: IInstitution
  ) {
    this.active = this.active ?? false;
  }
}

export function getSystemParameterInstitutionIdentifier(systemParameterInstitution: ISystemParameterInstitution): number | undefined {
  return systemParameterInstitution.id;
}
