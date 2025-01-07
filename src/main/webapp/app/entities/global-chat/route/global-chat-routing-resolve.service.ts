import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGlobalChat } from '../global-chat.model';
import { GlobalChatService } from '../service/global-chat.service';

const globalChatResolve = (route: ActivatedRouteSnapshot): Observable<null | IGlobalChat> => {
  const id = route.params.id;
  if (id) {
    return inject(GlobalChatService)
      .find(id)
      .pipe(
        mergeMap((globalChat: HttpResponse<IGlobalChat>) => {
          if (globalChat.body) {
            return of(globalChat.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default globalChatResolve;
