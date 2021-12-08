import * as dayjs from 'dayjs';

export interface IPlaceStatusTotal {
  status?: string;
  total?: number;
}

export class PlaceStatusTotal implements IPlaceStatusTotal {
  constructor(public status?: string, public total?: number) {}
}
