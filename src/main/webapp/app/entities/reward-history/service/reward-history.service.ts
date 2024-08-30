import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRewardHistory, NewRewardHistory } from '../reward-history.model';

export type PartialUpdateRewardHistory = Partial<IRewardHistory> & Pick<IRewardHistory, 'id'>;

type RestOf<T extends IRewardHistory | NewRewardHistory> = Omit<T, 'actionDate'> & {
  actionDate?: string | null;
};

export type RestRewardHistory = RestOf<IRewardHistory>;

export type NewRestRewardHistory = RestOf<NewRewardHistory>;

export type PartialUpdateRestRewardHistory = RestOf<PartialUpdateRewardHistory>;

export type EntityResponseType = HttpResponse<IRewardHistory>;
export type EntityArrayResponseType = HttpResponse<IRewardHistory[]>;

@Injectable({ providedIn: 'root' })
export class RewardHistoryService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reward-histories');

  create(rewardHistory: NewRewardHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rewardHistory);
    return this.http
      .post<RestRewardHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(rewardHistory: IRewardHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rewardHistory);
    return this.http
      .put<RestRewardHistory>(`${this.resourceUrl}/${this.getRewardHistoryIdentifier(rewardHistory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(rewardHistory: PartialUpdateRewardHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rewardHistory);
    return this.http
      .patch<RestRewardHistory>(`${this.resourceUrl}/${this.getRewardHistoryIdentifier(rewardHistory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRewardHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRewardHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRewardHistoryIdentifier(rewardHistory: Pick<IRewardHistory, 'id'>): number {
    return rewardHistory.id;
  }

  compareRewardHistory(o1: Pick<IRewardHistory, 'id'> | null, o2: Pick<IRewardHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getRewardHistoryIdentifier(o1) === this.getRewardHistoryIdentifier(o2) : o1 === o2;
  }

  addRewardHistoryToCollectionIfMissing<Type extends Pick<IRewardHistory, 'id'>>(
    rewardHistoryCollection: Type[],
    ...rewardHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rewardHistories: Type[] = rewardHistoriesToCheck.filter(isPresent);
    if (rewardHistories.length > 0) {
      const rewardHistoryCollectionIdentifiers = rewardHistoryCollection.map(rewardHistoryItem =>
        this.getRewardHistoryIdentifier(rewardHistoryItem),
      );
      const rewardHistoriesToAdd = rewardHistories.filter(rewardHistoryItem => {
        const rewardHistoryIdentifier = this.getRewardHistoryIdentifier(rewardHistoryItem);
        if (rewardHistoryCollectionIdentifiers.includes(rewardHistoryIdentifier)) {
          return false;
        }
        rewardHistoryCollectionIdentifiers.push(rewardHistoryIdentifier);
        return true;
      });
      return [...rewardHistoriesToAdd, ...rewardHistoryCollection];
    }
    return rewardHistoryCollection;
  }

  protected convertDateFromClient<T extends IRewardHistory | NewRewardHistory | PartialUpdateRewardHistory>(rewardHistory: T): RestOf<T> {
    return {
      ...rewardHistory,
      actionDate: rewardHistory.actionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRewardHistory: RestRewardHistory): IRewardHistory {
    return {
      ...restRewardHistory,
      actionDate: restRewardHistory.actionDate ? dayjs(restRewardHistory.actionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRewardHistory>): HttpResponse<IRewardHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRewardHistory[]>): HttpResponse<IRewardHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
