import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIdeaHistory } from '../idea-history.model';
import { IdeaHistoryService } from '../service/idea-history.service';

const ideaHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IIdeaHistory> => {
  const id = route.params.id;
  if (id) {
    return inject(IdeaHistoryService)
      .find(id)
      .pipe(
        mergeMap((ideaHistory: HttpResponse<IIdeaHistory>) => {
          if (ideaHistory.body) {
            return of(ideaHistory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ideaHistoryResolve;
