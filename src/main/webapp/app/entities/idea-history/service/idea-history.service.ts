import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIdeaHistory, NewIdeaHistory } from '../idea-history.model';

export type PartialUpdateIdeaHistory = Partial<IIdeaHistory> & Pick<IIdeaHistory, 'id'>;

type RestOf<T extends IIdeaHistory | NewIdeaHistory> = Omit<T, 'actionDate'> & {
  actionDate?: string | null;
};

export type RestIdeaHistory = RestOf<IIdeaHistory>;

export type NewRestIdeaHistory = RestOf<NewIdeaHistory>;

export type PartialUpdateRestIdeaHistory = RestOf<PartialUpdateIdeaHistory>;

export type EntityResponseType = HttpResponse<IIdeaHistory>;
export type EntityArrayResponseType = HttpResponse<IIdeaHistory[]>;

@Injectable({ providedIn: 'root' })
export class IdeaHistoryService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/idea-histories');

  create(ideaHistory: NewIdeaHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ideaHistory);
    return this.http
      .post<RestIdeaHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ideaHistory: IIdeaHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ideaHistory);
    return this.http
      .put<RestIdeaHistory>(`${this.resourceUrl}/${this.getIdeaHistoryIdentifier(ideaHistory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ideaHistory: PartialUpdateIdeaHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ideaHistory);
    return this.http
      .patch<RestIdeaHistory>(`${this.resourceUrl}/${this.getIdeaHistoryIdentifier(ideaHistory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestIdeaHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIdeaHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIdeaHistoryIdentifier(ideaHistory: Pick<IIdeaHistory, 'id'>): number {
    return ideaHistory.id;
  }

  compareIdeaHistory(o1: Pick<IIdeaHistory, 'id'> | null, o2: Pick<IIdeaHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getIdeaHistoryIdentifier(o1) === this.getIdeaHistoryIdentifier(o2) : o1 === o2;
  }

  addIdeaHistoryToCollectionIfMissing<Type extends Pick<IIdeaHistory, 'id'>>(
    ideaHistoryCollection: Type[],
    ...ideaHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ideaHistories: Type[] = ideaHistoriesToCheck.filter(isPresent);
    if (ideaHistories.length > 0) {
      const ideaHistoryCollectionIdentifiers = ideaHistoryCollection.map(ideaHistoryItem => this.getIdeaHistoryIdentifier(ideaHistoryItem));
      const ideaHistoriesToAdd = ideaHistories.filter(ideaHistoryItem => {
        const ideaHistoryIdentifier = this.getIdeaHistoryIdentifier(ideaHistoryItem);
        if (ideaHistoryCollectionIdentifiers.includes(ideaHistoryIdentifier)) {
          return false;
        }
        ideaHistoryCollectionIdentifiers.push(ideaHistoryIdentifier);
        return true;
      });
      return [...ideaHistoriesToAdd, ...ideaHistoryCollection];
    }
    return ideaHistoryCollection;
  }

  protected convertDateFromClient<T extends IIdeaHistory | NewIdeaHistory | PartialUpdateIdeaHistory>(ideaHistory: T): RestOf<T> {
    return {
      ...ideaHistory,
      actionDate: ideaHistory.actionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restIdeaHistory: RestIdeaHistory): IIdeaHistory {
    return {
      ...restIdeaHistory,
      actionDate: restIdeaHistory.actionDate ? dayjs(restIdeaHistory.actionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestIdeaHistory>): HttpResponse<IIdeaHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestIdeaHistory[]>): HttpResponse<IIdeaHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
