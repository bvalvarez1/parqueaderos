import * as dayjs from 'dayjs';
import { IRecordTicket } from 'app/entities/record-ticket/record-ticket.model';

export interface IReceipt {
  id?: number;
  authorizationNumber?: string | null;
  sequential?: string | null;
  status?: string | null;
  sriaccesskey?: string | null;
  sriauthorizationdate?: dayjs.Dayjs | null;
  receiptdate?: dayjs.Dayjs | null;
  recordticketid?: IRecordTicket | null;
}

export class Receipt implements IReceipt {
  constructor(
    public id?: number,
    public authorizationNumber?: string | null,
    public sequential?: string | null,
    public status?: string | null,
    public sriaccesskey?: string | null,
    public sriauthorizationdate?: dayjs.Dayjs | null,
    public receiptdate?: dayjs.Dayjs | null,
    public recordticketid?: IRecordTicket | null
  ) {}
}

export function getReceiptIdentifier(receipt: IReceipt): number | undefined {
  return receipt.id;
}
