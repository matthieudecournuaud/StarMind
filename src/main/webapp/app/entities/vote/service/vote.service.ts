import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVote, NewVote } from '../vote.model';

export type PartialUpdateVote = Partial<IVote> & Pick<IVote, 'id'>;

type RestOf<T extends IVote | NewVote> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestVote = RestOf<IVote>;

export type NewRestVote = RestOf<NewVote>;

export type PartialUpdateRestVote = RestOf<PartialUpdateVote>;

export type EntityResponseType = HttpResponse<IVote>;
export type EntityArrayResponseType = HttpResponse<IVote[]>;

@Injectable({ providedIn: 'root' })
export class VoteService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/votes');

  create(vote: NewVote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vote);
    return this.http.post<RestVote>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(vote: IVote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vote);
    return this.http
      .put<RestVote>(`${this.resourceUrl}/${this.getVoteIdentifier(vote)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(vote: PartialUpdateVote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vote);
    return this.http
      .patch<RestVote>(`${this.resourceUrl}/${this.getVoteIdentifier(vote)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestVote>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVote[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVoteIdentifier(vote: Pick<IVote, 'id'>): number {
    return vote.id;
  }

  compareVote(o1: Pick<IVote, 'id'> | null, o2: Pick<IVote, 'id'> | null): boolean {
    return o1 && o2 ? this.getVoteIdentifier(o1) === this.getVoteIdentifier(o2) : o1 === o2;
  }

  addVoteToCollectionIfMissing<Type extends Pick<IVote, 'id'>>(
    voteCollection: Type[],
    ...votesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const votes: Type[] = votesToCheck.filter(isPresent);
    if (votes.length > 0) {
      const voteCollectionIdentifiers = voteCollection.map(voteItem => this.getVoteIdentifier(voteItem));
      const votesToAdd = votes.filter(voteItem => {
        const voteIdentifier = this.getVoteIdentifier(voteItem);
        if (voteCollectionIdentifiers.includes(voteIdentifier)) {
          return false;
        }
        voteCollectionIdentifiers.push(voteIdentifier);
        return true;
      });
      return [...votesToAdd, ...voteCollection];
    }
    return voteCollection;
  }

  protected convertDateFromClient<T extends IVote | NewVote | PartialUpdateVote>(vote: T): RestOf<T> {
    return {
      ...vote,
      createdDate: vote.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restVote: RestVote): IVote {
    return {
      ...restVote,
      createdDate: restVote.createdDate ? dayjs(restVote.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVote>): HttpResponse<IVote> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVote[]>): HttpResponse<IVote[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
