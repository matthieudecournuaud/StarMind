import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IReward } from 'app/entities/reward/reward.model';
import { RewardService } from 'app/entities/reward/service/reward.service';
import { IdeaStatus } from 'app/entities/enumerations/idea-status.model';
import { RewardType } from 'app/entities/enumerations/reward-type.model';
import { IdeaService } from '../service/idea.service';
import { IIdea } from '../idea.model';
import { IdeaFormGroup, IdeaFormService } from './idea-form.service';

@Component({
  standalone: true,
  selector: 'jhi-idea-update',
  templateUrl: './idea-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class IdeaUpdateComponent implements OnInit {
  isSaving = false;
  idea: IIdea | null = null;
  ideaStatusValues = Object.keys(IdeaStatus);
  rewardTypeValues = Object.keys(RewardType);

  usersSharedCollection: IUser[] = [];
  categoriesSharedCollection: ICategory[] = [];
  rewardsSharedCollection: IReward[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected ideaService = inject(IdeaService);
  protected ideaFormService = inject(IdeaFormService);
  protected userService = inject(UserService);
  protected categoryService = inject(CategoryService);
  protected rewardService = inject(RewardService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: IdeaFormGroup = this.ideaFormService.createIdeaFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareCategory = (o1: ICategory | null, o2: ICategory | null): boolean => this.categoryService.compareCategory(o1, o2);

  compareReward = (o1: IReward | null, o2: IReward | null): boolean => this.rewardService.compareReward(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ idea }) => {
      this.idea = idea;
      if (idea) {
        this.updateForm(idea);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('starMindApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const idea = this.ideaFormService.getIdea(this.editForm);
    if (idea.id !== null) {
      this.subscribeToSaveResponse(this.ideaService.update(idea));
    } else {
      this.subscribeToSaveResponse(this.ideaService.create(idea));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIdea>>): void {
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

  protected updateForm(idea: IIdea): void {
    this.idea = idea;
    this.ideaFormService.resetForm(this.editForm, idea);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, idea.author);
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing<ICategory>(
      this.categoriesSharedCollection,
      idea.ideaCategory,
      idea.category,
    );
    this.rewardsSharedCollection = this.rewardService.addRewardToCollectionIfMissing<IReward>(
      this.rewardsSharedCollection,
      idea.assignedReward,
      idea.reward,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.idea?.author)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing<ICategory>(categories, this.idea?.ideaCategory, this.idea?.category),
        ),
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));

    this.rewardService
      .query()
      .pipe(map((res: HttpResponse<IReward[]>) => res.body ?? []))
      .pipe(
        map((rewards: IReward[]) =>
          this.rewardService.addRewardToCollectionIfMissing<IReward>(rewards, this.idea?.assignedReward, this.idea?.reward),
        ),
      )
      .subscribe((rewards: IReward[]) => (this.rewardsSharedCollection = rewards));
  }
}
