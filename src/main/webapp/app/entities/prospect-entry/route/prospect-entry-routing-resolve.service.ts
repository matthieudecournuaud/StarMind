import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProspectEntry } from '../prospect-entry.model';
import { ProspectEntryService } from '../service/prospect-entry.service';

const prospectEntryResolve = (route: ActivatedRouteSnapshot): Observable<null | IProspectEntry> => {
  const id = route.params.id;
  if (id) {
    return inject(ProspectEntryService)
      .find(id)
      .pipe(
        mergeMap((prospectEntry: HttpResponse<IProspectEntry>) => {
          if (prospectEntry.body) {
            return of(prospectEntry.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default prospectEntryResolve;
