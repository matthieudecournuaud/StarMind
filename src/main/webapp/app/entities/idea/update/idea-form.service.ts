import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IIdea, NewIdea } from '../idea.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIdea for edit and NewIdeaFormGroupInput for create.
 */
type IdeaFormGroupInput = IIdea | PartialWithRequiredKeyOf<NewIdea>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IIdea | NewIdea> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type IdeaFormRawValue = FormValueOf<IIdea>;

type NewIdeaFormRawValue = FormValueOf<NewIdea>;

type IdeaFormDefaults = Pick<NewIdea, 'id' | 'isConfidential' | 'validation' | 'createdDate' | 'modifiedDate' | 'isPublic'>;

type IdeaFormGroupContent = {
  id: FormControl<IdeaFormRawValue['id'] | NewIdea['id']>;
  title: FormControl<IdeaFormRawValue['title']>;
  description: FormControl<IdeaFormRawValue['description']>;
  status: FormControl<IdeaFormRawValue['status']>;
  isConfidential: FormControl<IdeaFormRawValue['isConfidential']>;
  validation: FormControl<IdeaFormRawValue['validation']>;
  rewardType: FormControl<IdeaFormRawValue['rewardType']>;
  likes: FormControl<IdeaFormRawValue['likes']>;
  createdDate: FormControl<IdeaFormRawValue['createdDate']>;
  modifiedDate: FormControl<IdeaFormRawValue['modifiedDate']>;
  isPublic: FormControl<IdeaFormRawValue['isPublic']>;
  impact: FormControl<IdeaFormRawValue['impact']>;
  author: FormControl<IdeaFormRawValue['author']>;
  assignedReward: FormControl<IdeaFormRawValue['assignedReward']>;
  manager: FormControl<IdeaFormRawValue['manager']>;
  category: FormControl<IdeaFormRawValue['category']>;
};

export type IdeaFormGroup = FormGroup<IdeaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IdeaFormService {
  createIdeaFormGroup(idea: IdeaFormGroupInput = { id: null }): IdeaFormGroup {
    const ideaRawValue = this.convertIdeaToIdeaRawValue({
      ...this.getFormDefaults(),
      ...idea,
    });
    return new FormGroup<IdeaFormGroupContent>({
      id: new FormControl(
        { value: ideaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(ideaRawValue.title, {
        validators: [Validators.required],
      }),
      description: new FormControl(ideaRawValue.description, {
        validators: [Validators.required],
      }),
      status: new FormControl(ideaRawValue.status, {
        validators: [Validators.required],
      }),
      isConfidential: new FormControl(ideaRawValue.isConfidential, {
        validators: [Validators.required],
      }),
      validation: new FormControl(ideaRawValue.validation),
      rewardType: new FormControl(ideaRawValue.rewardType),
      likes: new FormControl(ideaRawValue.likes),
      createdDate: new FormControl(ideaRawValue.createdDate),
      modifiedDate: new FormControl(ideaRawValue.modifiedDate),
      isPublic: new FormControl(ideaRawValue.isPublic),
      impact: new FormControl(ideaRawValue.impact),
      author: new FormControl(ideaRawValue.author),
      assignedReward: new FormControl(ideaRawValue.assignedReward),
      manager: new FormControl(ideaRawValue.manager),
      category: new FormControl(ideaRawValue.category),
    });
  }

  getIdea(form: IdeaFormGroup): IIdea | NewIdea {
    return this.convertIdeaRawValueToIdea(form.getRawValue() as IdeaFormRawValue | NewIdeaFormRawValue);
  }

  resetForm(form: IdeaFormGroup, idea: IdeaFormGroupInput): void {
    const ideaRawValue = this.convertIdeaToIdeaRawValue({ ...this.getFormDefaults(), ...idea });
    form.reset(
      {
        ...ideaRawValue,
        id: { value: ideaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): IdeaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isConfidential: false,
      validation: false,
      createdDate: currentTime,
      modifiedDate: currentTime,
      isPublic: false,
    };
  }

  private convertIdeaRawValueToIdea(rawIdea: IdeaFormRawValue | NewIdeaFormRawValue): IIdea | NewIdea {
    return {
      ...rawIdea,
      createdDate: dayjs(rawIdea.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawIdea.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertIdeaToIdeaRawValue(
    idea: IIdea | (Partial<NewIdea> & IdeaFormDefaults),
  ): IdeaFormRawValue | PartialWithRequiredKeyOf<NewIdeaFormRawValue> {
    return {
      ...idea,
      createdDate: idea.createdDate ? idea.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: idea.modifiedDate ? idea.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
