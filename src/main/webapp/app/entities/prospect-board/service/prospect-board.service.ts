import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProspectBoard, NewProspectBoard } from '../prospect-board.model';

export type PartialUpdateProspectBoard = Partial<IProspectBoard> & Pick<IProspectBoard, 'id'>;

type RestOf<T extends IProspectBoard | NewProspectBoard> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

export type RestProspectBoard = RestOf<IProspectBoard>;

export type NewRestProspectBoard = RestOf<NewProspectBoard>;

export type PartialUpdateRestProspectBoard = RestOf<PartialUpdateProspectBoard>;

export type EntityResponseType = HttpResponse<IProspectBoard>;
export type EntityArrayResponseType = HttpResponse<IProspectBoard[]>;

@Injectable({ providedIn: 'root' })
export class ProspectBoardService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prospect-boards');

  create(prospectBoard: NewProspectBoard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prospectBoard);
    return this.http
      .post<RestProspectBoard>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(prospectBoard: IProspectBoard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prospectBoard);
    return this.http
      .put<RestProspectBoard>(`${this.resourceUrl}/${this.getProspectBoardIdentifier(prospectBoard)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(prospectBoard: PartialUpdateProspectBoard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prospectBoard);
    return this.http
      .patch<RestProspectBoard>(`${this.resourceUrl}/${this.getProspectBoardIdentifier(prospectBoard)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProspectBoard>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProspectBoard[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProspectBoardIdentifier(prospectBoard: Pick<IProspectBoard, 'id'>): number {
    return prospectBoard.id;
  }

  compareProspectBoard(o1: Pick<IProspectBoard, 'id'> | null, o2: Pick<IProspectBoard, 'id'> | null): boolean {
    return o1 && o2 ? this.getProspectBoardIdentifier(o1) === this.getProspectBoardIdentifier(o2) : o1 === o2;
  }

  addProspectBoardToCollectionIfMissing<Type extends Pick<IProspectBoard, 'id'>>(
    prospectBoardCollection: Type[],
    ...prospectBoardsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const prospectBoards: Type[] = prospectBoardsToCheck.filter(isPresent);
    if (prospectBoards.length > 0) {
      const prospectBoardCollectionIdentifiers = prospectBoardCollection.map(prospectBoardItem =>
        this.getProspectBoardIdentifier(prospectBoardItem),
      );
      const prospectBoardsToAdd = prospectBoards.filter(prospectBoardItem => {
        const prospectBoardIdentifier = this.getProspectBoardIdentifier(prospectBoardItem);
        if (prospectBoardCollectionIdentifiers.includes(prospectBoardIdentifier)) {
          return false;
        }
        prospectBoardCollectionIdentifiers.push(prospectBoardIdentifier);
        return true;
      });
      return [...prospectBoardsToAdd, ...prospectBoardCollection];
    }
    return prospectBoardCollection;
  }

  protected convertDateFromClient<T extends IProspectBoard | NewProspectBoard | PartialUpdateProspectBoard>(prospectBoard: T): RestOf<T> {
    return {
      ...prospectBoard,
      createdDate: prospectBoard.createdDate?.toJSON() ?? null,
      modifiedDate: prospectBoard.modifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restProspectBoard: RestProspectBoard): IProspectBoard {
    return {
      ...restProspectBoard,
      createdDate: restProspectBoard.createdDate ? dayjs(restProspectBoard.createdDate) : undefined,
      modifiedDate: restProspectBoard.modifiedDate ? dayjs(restProspectBoard.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProspectBoard>): HttpResponse<IProspectBoard> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProspectBoard[]>): HttpResponse<IProspectBoard[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
