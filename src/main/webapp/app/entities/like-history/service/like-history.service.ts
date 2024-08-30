import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILikeHistory, NewLikeHistory } from '../like-history.model';

export type PartialUpdateLikeHistory = Partial<ILikeHistory> & Pick<ILikeHistory, 'id'>;

type RestOf<T extends ILikeHistory | NewLikeHistory> = Omit<T, 'actionDate'> & {
  actionDate?: string | null;
};

export type RestLikeHistory = RestOf<ILikeHistory>;

export type NewRestLikeHistory = RestOf<NewLikeHistory>;

export type PartialUpdateRestLikeHistory = RestOf<PartialUpdateLikeHistory>;

export type EntityResponseType = HttpResponse<ILikeHistory>;
export type EntityArrayResponseType = HttpResponse<ILikeHistory[]>;

@Injectable({ providedIn: 'root' })
export class LikeHistoryService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/like-histories');

  create(likeHistory: NewLikeHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(likeHistory);
    return this.http
      .post<RestLikeHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(likeHistory: ILikeHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(likeHistory);
    return this.http
      .put<RestLikeHistory>(`${this.resourceUrl}/${this.getLikeHistoryIdentifier(likeHistory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(likeHistory: PartialUpdateLikeHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(likeHistory);
    return this.http
      .patch<RestLikeHistory>(`${this.resourceUrl}/${this.getLikeHistoryIdentifier(likeHistory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLikeHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLikeHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLikeHistoryIdentifier(likeHistory: Pick<ILikeHistory, 'id'>): number {
    return likeHistory.id;
  }

  compareLikeHistory(o1: Pick<ILikeHistory, 'id'> | null, o2: Pick<ILikeHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getLikeHistoryIdentifier(o1) === this.getLikeHistoryIdentifier(o2) : o1 === o2;
  }

  addLikeHistoryToCollectionIfMissing<Type extends Pick<ILikeHistory, 'id'>>(
    likeHistoryCollection: Type[],
    ...likeHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const likeHistories: Type[] = likeHistoriesToCheck.filter(isPresent);
    if (likeHistories.length > 0) {
      const likeHistoryCollectionIdentifiers = likeHistoryCollection.map(likeHistoryItem => this.getLikeHistoryIdentifier(likeHistoryItem));
      const likeHistoriesToAdd = likeHistories.filter(likeHistoryItem => {
        const likeHistoryIdentifier = this.getLikeHistoryIdentifier(likeHistoryItem);
        if (likeHistoryCollectionIdentifiers.includes(likeHistoryIdentifier)) {
          return false;
        }
        likeHistoryCollectionIdentifiers.push(likeHistoryIdentifier);
        return true;
      });
      return [...likeHistoriesToAdd, ...likeHistoryCollection];
    }
    return likeHistoryCollection;
  }

  protected convertDateFromClient<T extends ILikeHistory | NewLikeHistory | PartialUpdateLikeHistory>(likeHistory: T): RestOf<T> {
    return {
      ...likeHistory,
      actionDate: likeHistory.actionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restLikeHistory: RestLikeHistory): ILikeHistory {
    return {
      ...restLikeHistory,
      actionDate: restLikeHistory.actionDate ? dayjs(restLikeHistory.actionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLikeHistory>): HttpResponse<ILikeHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLikeHistory[]>): HttpResponse<ILikeHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
