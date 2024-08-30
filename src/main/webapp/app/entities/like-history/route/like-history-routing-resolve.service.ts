import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILikeHistory } from '../like-history.model';
import { LikeHistoryService } from '../service/like-history.service';

const likeHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | ILikeHistory> => {
  const id = route.params.id;
  if (id) {
    return inject(LikeHistoryService)
      .find(id)
      .pipe(
        mergeMap((likeHistory: HttpResponse<ILikeHistory>) => {
          if (likeHistory.body) {
            return of(likeHistory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default likeHistoryResolve;
