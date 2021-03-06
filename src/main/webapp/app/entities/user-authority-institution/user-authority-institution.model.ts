import { IUser } from 'app/admin/user-management/user-management.model';
import { IInstitution } from 'app/entities/institution/institution.model';
import { IUserAuthority } from 'app/entities/user-authority/user-authority.model';

export interface IUserAuthorityInstitution {
  id?: number;
  active?: boolean | null;
  institution?: IInstitution | null;
  userAuthority?: IUserAuthority | null;
  user?: IUser | null;
}

export class UserAuthorityInstitution implements IUserAuthorityInstitution {
  constructor(
    public id?: number,
    public active?: boolean | null,
    public institution?: IInstitution | null,
    public userAuthority?: IUserAuthority | null,
    public user?: IUser | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getUserAuthorityInstitutionIdentifier(userAuthorityInstitution: IUserAuthorityInstitution): number | undefined {
  return userAuthorityInstitution.id;
}
