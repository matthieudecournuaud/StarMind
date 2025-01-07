import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIdeaChat } from '../idea-chat.model';
import { IdeaChatService } from '../service/idea-chat.service';

const ideaChatResolve = (route: ActivatedRouteSnapshot): Observable<null | IIdeaChat> => {
  const id = route.params.id;
  if (id) {
    return inject(IdeaChatService)
      .find(id)
      .pipe(
        mergeMap((ideaChat: HttpResponse<IIdeaChat>) => {
          if (ideaChat.body) {
            return of(ideaChat.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ideaChatResolve;
