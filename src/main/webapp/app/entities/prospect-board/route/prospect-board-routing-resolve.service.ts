import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProspectBoard } from '../prospect-board.model';
import { ProspectBoardService } from '../service/prospect-board.service';

const prospectBoardResolve = (route: ActivatedRouteSnapshot): Observable<null | IProspectBoard> => {
  const id = route.params.id;
  if (id) {
    return inject(ProspectBoardService)
      .find(id)
      .pipe(
        mergeMap((prospectBoard: HttpResponse<IProspectBoard>) => {
          if (prospectBoard.body) {
            return of(prospectBoard.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default prospectBoardResolve;
