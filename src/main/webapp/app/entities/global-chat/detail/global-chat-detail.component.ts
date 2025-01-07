import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IGlobalChat } from '../global-chat.model';

@Component({
  standalone: true,
  selector: 'jhi-global-chat-detail',
  templateUrl: './global-chat-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class GlobalChatDetailComponent {
  globalChat = input<IGlobalChat | null>(null);

  previousState(): void {
    window.history.back();
  }
}
