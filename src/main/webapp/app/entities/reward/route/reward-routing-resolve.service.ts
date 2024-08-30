import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReward } from '../reward.model';
import { RewardService } from '../service/reward.service';

const rewardResolve = (route: ActivatedRouteSnapshot): Observable<null | IReward> => {
  const id = route.params.id;
  if (id) {
    return inject(RewardService)
      .find(id)
      .pipe(
        mergeMap((reward: HttpResponse<IReward>) => {
          if (reward.body) {
            return of(reward.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default rewardResolve;
