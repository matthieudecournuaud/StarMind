import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IIdeaChat, NewIdeaChat } from '../idea-chat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIdeaChat for edit and NewIdeaChatFormGroupInput for create.
 */
type IdeaChatFormGroupInput = IIdeaChat | PartialWithRequiredKeyOf<NewIdeaChat>;

type IdeaChatFormDefaults = Pick<NewIdeaChat, 'id' | 'users'>;

type IdeaChatFormGroupContent = {
  id: FormControl<IIdeaChat['id'] | NewIdeaChat['id']>;
  name: FormControl<IIdeaChat['name']>;
  users: FormControl<IIdeaChat['users']>;
};

export type IdeaChatFormGroup = FormGroup<IdeaChatFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IdeaChatFormService {
  createIdeaChatFormGroup(ideaChat: IdeaChatFormGroupInput = { id: null }): IdeaChatFormGroup {
    const ideaChatRawValue = {
      ...this.getFormDefaults(),
      ...ideaChat,
    };
    return new FormGroup<IdeaChatFormGroupContent>({
      id: new FormControl(
        { value: ideaChatRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(ideaChatRawValue.name, {
        validators: [Validators.required],
      }),
      users: new FormControl(ideaChatRawValue.users ?? []),
    });
  }

  getIdeaChat(form: IdeaChatFormGroup): IIdeaChat | NewIdeaChat {
    return form.getRawValue() as IIdeaChat | NewIdeaChat;
  }

  resetForm(form: IdeaChatFormGroup, ideaChat: IdeaChatFormGroupInput): void {
    const ideaChatRawValue = { ...this.getFormDefaults(), ...ideaChat };
    form.reset(
      {
        ...ideaChatRawValue,
        id: { value: ideaChatRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): IdeaChatFormDefaults {
    return {
      id: null,
      users: [],
    };
  }
}
