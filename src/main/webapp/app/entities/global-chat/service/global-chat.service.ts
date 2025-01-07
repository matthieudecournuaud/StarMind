import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGlobalChat, NewGlobalChat } from '../global-chat.model';

export type PartialUpdateGlobalChat = Partial<IGlobalChat> & Pick<IGlobalChat, 'id'>;

export type EntityResponseType = HttpResponse<IGlobalChat>;
export type EntityArrayResponseType = HttpResponse<IGlobalChat[]>;

@Injectable({ providedIn: 'root' })
export class GlobalChatService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/global-chats');

  create(globalChat: NewGlobalChat): Observable<EntityResponseType> {
    return this.http.post<IGlobalChat>(this.resourceUrl, globalChat, { observe: 'response' });
  }

  update(globalChat: IGlobalChat): Observable<EntityResponseType> {
    return this.http.put<IGlobalChat>(`${this.resourceUrl}/${this.getGlobalChatIdentifier(globalChat)}`, globalChat, {
      observe: 'response',
    });
  }

  partialUpdate(globalChat: PartialUpdateGlobalChat): Observable<EntityResponseType> {
    return this.http.patch<IGlobalChat>(`${this.resourceUrl}/${this.getGlobalChatIdentifier(globalChat)}`, globalChat, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGlobalChat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGlobalChat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGlobalChatIdentifier(globalChat: Pick<IGlobalChat, 'id'>): number {
    return globalChat.id;
  }

  compareGlobalChat(o1: Pick<IGlobalChat, 'id'> | null, o2: Pick<IGlobalChat, 'id'> | null): boolean {
    return o1 && o2 ? this.getGlobalChatIdentifier(o1) === this.getGlobalChatIdentifier(o2) : o1 === o2;
  }

  addGlobalChatToCollectionIfMissing<Type extends Pick<IGlobalChat, 'id'>>(
    globalChatCollection: Type[],
    ...globalChatsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const globalChats: Type[] = globalChatsToCheck.filter(isPresent);
    if (globalChats.length > 0) {
      const globalChatCollectionIdentifiers = globalChatCollection.map(globalChatItem => this.getGlobalChatIdentifier(globalChatItem));
      const globalChatsToAdd = globalChats.filter(globalChatItem => {
        const globalChatIdentifier = this.getGlobalChatIdentifier(globalChatItem);
        if (globalChatCollectionIdentifiers.includes(globalChatIdentifier)) {
          return false;
        }
        globalChatCollectionIdentifiers.push(globalChatIdentifier);
        return true;
      });
      return [...globalChatsToAdd, ...globalChatCollection];
    }
    return globalChatCollection;
  }
}
