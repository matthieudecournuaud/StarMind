import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVote } from '../vote.model';
import { VoteService } from '../service/vote.service';

const voteResolve = (route: ActivatedRouteSnapshot): Observable<null | IVote> => {
  const id = route.params.id;
  if (id) {
    return inject(VoteService)
      .find(id)
      .pipe(
        mergeMap((vote: HttpResponse<IVote>) => {
          if (vote.body) {
            return of(vote.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default voteResolve;
