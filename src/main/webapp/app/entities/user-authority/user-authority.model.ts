import { IUser } from 'app/entities/user/user.model';

export interface IUserAuthority {
  id?: number;
  authority?: string | null;
  active?: boolean | null;
  user?: IUser | null;
}

export class UserAuthority implements IUserAuthority {
  constructor(public id?: number, public authority?: string | null, public active?: boolean | null, public user?: IUser | null) {
    this.active = this.active ?? false;
  }
}

export function getUserAuthorityIdentifier(userAuthority: IUserAuthority): number | undefined {
  return userAuthority.id;
}
