import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReceipt, getReceiptIdentifier } from '../receipt.model';

export type EntityResponseType = HttpResponse<IReceipt>;
export type EntityArrayResponseType = HttpResponse<IReceipt[]>;

@Injectable({ providedIn: 'root' })
export class ReceiptService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/receipts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(receipt: IReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(receipt);
    return this.http
      .post<IReceipt>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(receipt: IReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(receipt);
    return this.http
      .put<IReceipt>(`${this.resourceUrl}/${getReceiptIdentifier(receipt) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(receipt: IReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(receipt);
    return this.http
      .patch<IReceipt>(`${this.resourceUrl}/${getReceiptIdentifier(receipt) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IReceipt>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReceipt[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReceiptToCollectionIfMissing(receiptCollection: IReceipt[], ...receiptsToCheck: (IReceipt | null | undefined)[]): IReceipt[] {
    const receipts: IReceipt[] = receiptsToCheck.filter(isPresent);
    if (receipts.length > 0) {
      const receiptCollectionIdentifiers = receiptCollection.map(receiptItem => getReceiptIdentifier(receiptItem)!);
      const receiptsToAdd = receipts.filter(receiptItem => {
        const receiptIdentifier = getReceiptIdentifier(receiptItem);
        if (receiptIdentifier == null || receiptCollectionIdentifiers.includes(receiptIdentifier)) {
          return false;
        }
        receiptCollectionIdentifiers.push(receiptIdentifier);
        return true;
      });
      return [...receiptsToAdd, ...receiptCollection];
    }
    return receiptCollection;
  }

  protected convertDateFromClient(receipt: IReceipt): IReceipt {
    return Object.assign({}, receipt, {
      sriauthorizationdate: receipt.sriauthorizationdate?.isValid() ? receipt.sriauthorizationdate.format(DATE_FORMAT) : undefined,
      receiptdate: receipt.receiptdate?.isValid() ? receipt.receiptdate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.sriauthorizationdate = res.body.sriauthorizationdate ? dayjs(res.body.sriauthorizationdate) : undefined;
      res.body.receiptdate = res.body.receiptdate ? dayjs(res.body.receiptdate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((receipt: IReceipt) => {
        receipt.sriauthorizationdate = receipt.sriauthorizationdate ? dayjs(receipt.sriauthorizationdate) : undefined;
        receipt.receiptdate = receipt.receiptdate ? dayjs(receipt.receiptdate) : undefined;
      });
    }
    return res;
  }
}
