import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIdea, NewIdea } from '../idea.model';

export type PartialUpdateIdea = Partial<IIdea> & Pick<IIdea, 'id'>;

type RestOf<T extends IIdea | NewIdea> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

export type RestIdea = RestOf<IIdea>;

export type NewRestIdea = RestOf<NewIdea>;

export type PartialUpdateRestIdea = RestOf<PartialUpdateIdea>;

export type EntityResponseType = HttpResponse<IIdea>;
export type EntityArrayResponseType = HttpResponse<IIdea[]>;

@Injectable({ providedIn: 'root' })
export class IdeaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ideas');

  create(idea: NewIdea): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(idea);
    return this.http.post<RestIdea>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(idea: IIdea): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(idea);
    return this.http
      .put<RestIdea>(`${this.resourceUrl}/${this.getIdeaIdentifier(idea)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(idea: PartialUpdateIdea): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(idea);
    return this.http
      .patch<RestIdea>(`${this.resourceUrl}/${this.getIdeaIdentifier(idea)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestIdea>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIdea[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIdeaIdentifier(idea: Pick<IIdea, 'id'>): number {
    return idea.id;
  }

  compareIdea(o1: Pick<IIdea, 'id'> | null, o2: Pick<IIdea, 'id'> | null): boolean {
    return o1 && o2 ? this.getIdeaIdentifier(o1) === this.getIdeaIdentifier(o2) : o1 === o2;
  }

  addIdeaToCollectionIfMissing<Type extends Pick<IIdea, 'id'>>(
    ideaCollection: Type[],
    ...ideasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ideas: Type[] = ideasToCheck.filter(isPresent);
    if (ideas.length > 0) {
      const ideaCollectionIdentifiers = ideaCollection.map(ideaItem => this.getIdeaIdentifier(ideaItem));
      const ideasToAdd = ideas.filter(ideaItem => {
        const ideaIdentifier = this.getIdeaIdentifier(ideaItem);
        if (ideaCollectionIdentifiers.includes(ideaIdentifier)) {
          return false;
        }
        ideaCollectionIdentifiers.push(ideaIdentifier);
        return true;
      });
      return [...ideasToAdd, ...ideaCollection];
    }
    return ideaCollection;
  }

  protected convertDateFromClient<T extends IIdea | NewIdea | PartialUpdateIdea>(idea: T): RestOf<T> {
    return {
      ...idea,
      createdDate: idea.createdDate?.toJSON() ?? null,
      modifiedDate: idea.modifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restIdea: RestIdea): IIdea {
    return {
      ...restIdea,
      createdDate: restIdea.createdDate ? dayjs(restIdea.createdDate) : undefined,
      modifiedDate: restIdea.modifiedDate ? dayjs(restIdea.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestIdea>): HttpResponse<IIdea> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestIdea[]>): HttpResponse<IIdea[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
