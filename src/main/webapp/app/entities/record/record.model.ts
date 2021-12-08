export interface IRecord {
  id?: number;
}

export class Record implements IRecord {
  constructor(public id?: number) {}
}

export function getRecordIdentifier(record: IRecord): number | undefined {
  return record.id;
}
