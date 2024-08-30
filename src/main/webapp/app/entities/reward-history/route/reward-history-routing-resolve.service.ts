import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRewardHistory } from '../reward-history.model';
import { RewardHistoryService } from '../service/reward-history.service';

const rewardHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IRewardHistory> => {
  const id = route.params.id;
  if (id) {
    return inject(RewardHistoryService)
      .find(id)
      .pipe(
        mergeMap((rewardHistory: HttpResponse<IRewardHistory>) => {
          if (rewardHistory.body) {
            return of(rewardHistory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default rewardHistoryResolve;
