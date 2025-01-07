import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IIdeaChat } from '../idea-chat.model';
import { IdeaChatService } from '../service/idea-chat.service';
import { IdeaChatFormGroup, IdeaChatFormService } from './idea-chat-form.service';

@Component({
  standalone: true,
  selector: 'jhi-idea-chat-update',
  templateUrl: './idea-chat-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class IdeaChatUpdateComponent implements OnInit {
  isSaving = false;
  ideaChat: IIdeaChat | null = null;

  usersSharedCollection: IUser[] = [];

  protected ideaChatService = inject(IdeaChatService);
  protected ideaChatFormService = inject(IdeaChatFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: IdeaChatFormGroup = this.ideaChatFormService.createIdeaChatFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ideaChat }) => {
      this.ideaChat = ideaChat;
      if (ideaChat) {
        this.updateForm(ideaChat);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ideaChat = this.ideaChatFormService.getIdeaChat(this.editForm);
    if (ideaChat.id !== null) {
      this.subscribeToSaveResponse(this.ideaChatService.update(ideaChat));
    } else {
      this.subscribeToSaveResponse(this.ideaChatService.create(ideaChat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIdeaChat>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(ideaChat: IIdeaChat): void {
    this.ideaChat = ideaChat;
    this.ideaChatFormService.resetForm(this.editForm, ideaChat);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      ...(ideaChat.users ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, ...(this.ideaChat?.users ?? []))))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
