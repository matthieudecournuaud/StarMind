import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProspectEntry, NewProspectEntry } from '../prospect-entry.model';

export type PartialUpdateProspectEntry = Partial<IProspectEntry> & Pick<IProspectEntry, 'id'>;

type RestOf<T extends IProspectEntry | NewProspectEntry> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

export type RestProspectEntry = RestOf<IProspectEntry>;

export type NewRestProspectEntry = RestOf<NewProspectEntry>;

export type PartialUpdateRestProspectEntry = RestOf<PartialUpdateProspectEntry>;

export type EntityResponseType = HttpResponse<IProspectEntry>;
export type EntityArrayResponseType = HttpResponse<IProspectEntry[]>;

@Injectable({ providedIn: 'root' })
export class ProspectEntryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prospect-entries');

  create(prospectEntry: NewProspectEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prospectEntry);
    return this.http
      .post<RestProspectEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(prospectEntry: IProspectEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prospectEntry);
    return this.http
      .put<RestProspectEntry>(`${this.resourceUrl}/${this.getProspectEntryIdentifier(prospectEntry)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(prospectEntry: PartialUpdateProspectEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prospectEntry);
    return this.http
      .patch<RestProspectEntry>(`${this.resourceUrl}/${this.getProspectEntryIdentifier(prospectEntry)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProspectEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProspectEntry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProspectEntryIdentifier(prospectEntry: Pick<IProspectEntry, 'id'>): number {
    return prospectEntry.id;
  }

  compareProspectEntry(o1: Pick<IProspectEntry, 'id'> | null, o2: Pick<IProspectEntry, 'id'> | null): boolean {
    return o1 && o2 ? this.getProspectEntryIdentifier(o1) === this.getProspectEntryIdentifier(o2) : o1 === o2;
  }

  addProspectEntryToCollectionIfMissing<Type extends Pick<IProspectEntry, 'id'>>(
    prospectEntryCollection: Type[],
    ...prospectEntriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const prospectEntries: Type[] = prospectEntriesToCheck.filter(isPresent);
    if (prospectEntries.length > 0) {
      const prospectEntryCollectionIdentifiers = prospectEntryCollection.map(prospectEntryItem =>
        this.getProspectEntryIdentifier(prospectEntryItem),
      );
      const prospectEntriesToAdd = prospectEntries.filter(prospectEntryItem => {
        const prospectEntryIdentifier = this.getProspectEntryIdentifier(prospectEntryItem);
        if (prospectEntryCollectionIdentifiers.includes(prospectEntryIdentifier)) {
          return false;
        }
        prospectEntryCollectionIdentifiers.push(prospectEntryIdentifier);
        return true;
      });
      return [...prospectEntriesToAdd, ...prospectEntryCollection];
    }
    return prospectEntryCollection;
  }

  protected convertDateFromClient<T extends IProspectEntry | NewProspectEntry | PartialUpdateProspectEntry>(prospectEntry: T): RestOf<T> {
    return {
      ...prospectEntry,
      createdDate: prospectEntry.createdDate?.toJSON() ?? null,
      modifiedDate: prospectEntry.modifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restProspectEntry: RestProspectEntry): IProspectEntry {
    return {
      ...restProspectEntry,
      createdDate: restProspectEntry.createdDate ? dayjs(restProspectEntry.createdDate) : undefined,
      modifiedDate: restProspectEntry.modifiedDate ? dayjs(restProspectEntry.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProspectEntry>): HttpResponse<IProspectEntry> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProspectEntry[]>): HttpResponse<IProspectEntry[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
