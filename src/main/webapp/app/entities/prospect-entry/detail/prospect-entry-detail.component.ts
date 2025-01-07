import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IProspectEntry } from '../prospect-entry.model';

@Component({
  standalone: true,
  selector: 'jhi-prospect-entry-detail',
  templateUrl: './prospect-entry-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProspectEntryDetailComponent {
  prospectEntry = input<IProspectEntry | null>(null);

  previousState(): void {
    window.history.back();
  }
}
