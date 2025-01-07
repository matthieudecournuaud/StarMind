import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIdeaChat, NewIdeaChat } from '../idea-chat.model';

export type PartialUpdateIdeaChat = Partial<IIdeaChat> & Pick<IIdeaChat, 'id'>;

export type EntityResponseType = HttpResponse<IIdeaChat>;
export type EntityArrayResponseType = HttpResponse<IIdeaChat[]>;

@Injectable({ providedIn: 'root' })
export class IdeaChatService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/idea-chats');

  create(ideaChat: NewIdeaChat): Observable<EntityResponseType> {
    return this.http.post<IIdeaChat>(this.resourceUrl, ideaChat, { observe: 'response' });
  }

  update(ideaChat: IIdeaChat): Observable<EntityResponseType> {
    return this.http.put<IIdeaChat>(`${this.resourceUrl}/${this.getIdeaChatIdentifier(ideaChat)}`, ideaChat, { observe: 'response' });
  }

  partialUpdate(ideaChat: PartialUpdateIdeaChat): Observable<EntityResponseType> {
    return this.http.patch<IIdeaChat>(`${this.resourceUrl}/${this.getIdeaChatIdentifier(ideaChat)}`, ideaChat, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIdeaChat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIdeaChat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIdeaChatIdentifier(ideaChat: Pick<IIdeaChat, 'id'>): number {
    return ideaChat.id;
  }

  compareIdeaChat(o1: Pick<IIdeaChat, 'id'> | null, o2: Pick<IIdeaChat, 'id'> | null): boolean {
    return o1 && o2 ? this.getIdeaChatIdentifier(o1) === this.getIdeaChatIdentifier(o2) : o1 === o2;
  }

  addIdeaChatToCollectionIfMissing<Type extends Pick<IIdeaChat, 'id'>>(
    ideaChatCollection: Type[],
    ...ideaChatsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ideaChats: Type[] = ideaChatsToCheck.filter(isPresent);
    if (ideaChats.length > 0) {
      const ideaChatCollectionIdentifiers = ideaChatCollection.map(ideaChatItem => this.getIdeaChatIdentifier(ideaChatItem));
      const ideaChatsToAdd = ideaChats.filter(ideaChatItem => {
        const ideaChatIdentifier = this.getIdeaChatIdentifier(ideaChatItem);
        if (ideaChatCollectionIdentifiers.includes(ideaChatIdentifier)) {
          return false;
        }
        ideaChatCollectionIdentifiers.push(ideaChatIdentifier);
        return true;
      });
      return [...ideaChatsToAdd, ...ideaChatCollection];
    }
    return ideaChatCollection;
  }
}
