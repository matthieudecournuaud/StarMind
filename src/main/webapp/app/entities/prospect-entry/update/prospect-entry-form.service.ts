import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProspectEntry, NewProspectEntry } from '../prospect-entry.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProspectEntry for edit and NewProspectEntryFormGroupInput for create.
 */
type ProspectEntryFormGroupInput = IProspectEntry | PartialWithRequiredKeyOf<NewProspectEntry>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProspectEntry | NewProspectEntry> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type ProspectEntryFormRawValue = FormValueOf<IProspectEntry>;

type NewProspectEntryFormRawValue = FormValueOf<NewProspectEntry>;

type ProspectEntryFormDefaults = Pick<NewProspectEntry, 'id' | 'createdDate' | 'modifiedDate'>;

type ProspectEntryFormGroupContent = {
  id: FormControl<ProspectEntryFormRawValue['id'] | NewProspectEntry['id']>;
  status: FormControl<ProspectEntryFormRawValue['status']>;
  createdDate: FormControl<ProspectEntryFormRawValue['createdDate']>;
  modifiedDate: FormControl<ProspectEntryFormRawValue['modifiedDate']>;
  prospect: FormControl<ProspectEntryFormRawValue['prospect']>;
  prospectBoard: FormControl<ProspectEntryFormRawValue['prospectBoard']>;
};

export type ProspectEntryFormGroup = FormGroup<ProspectEntryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProspectEntryFormService {
  createProspectEntryFormGroup(prospectEntry: ProspectEntryFormGroupInput = { id: null }): ProspectEntryFormGroup {
    const prospectEntryRawValue = this.convertProspectEntryToProspectEntryRawValue({
      ...this.getFormDefaults(),
      ...prospectEntry,
    });
    return new FormGroup<ProspectEntryFormGroupContent>({
      id: new FormControl(
        { value: prospectEntryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(prospectEntryRawValue.status, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(prospectEntryRawValue.createdDate, {
        validators: [Validators.required],
      }),
      modifiedDate: new FormControl(prospectEntryRawValue.modifiedDate, {
        validators: [Validators.required],
      }),
      prospect: new FormControl(prospectEntryRawValue.prospect),
      prospectBoard: new FormControl(prospectEntryRawValue.prospectBoard),
    });
  }

  getProspectEntry(form: ProspectEntryFormGroup): IProspectEntry | NewProspectEntry {
    return this.convertProspectEntryRawValueToProspectEntry(form.getRawValue() as ProspectEntryFormRawValue | NewProspectEntryFormRawValue);
  }

  resetForm(form: ProspectEntryFormGroup, prospectEntry: ProspectEntryFormGroupInput): void {
    const prospectEntryRawValue = this.convertProspectEntryToProspectEntryRawValue({ ...this.getFormDefaults(), ...prospectEntry });
    form.reset(
      {
        ...prospectEntryRawValue,
        id: { value: prospectEntryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProspectEntryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      modifiedDate: currentTime,
    };
  }

  private convertProspectEntryRawValueToProspectEntry(
    rawProspectEntry: ProspectEntryFormRawValue | NewProspectEntryFormRawValue,
  ): IProspectEntry | NewProspectEntry {
    return {
      ...rawProspectEntry,
      createdDate: dayjs(rawProspectEntry.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawProspectEntry.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertProspectEntryToProspectEntryRawValue(
    prospectEntry: IProspectEntry | (Partial<NewProspectEntry> & ProspectEntryFormDefaults),
  ): ProspectEntryFormRawValue | PartialWithRequiredKeyOf<NewProspectEntryFormRawValue> {
    return {
      ...prospectEntry,
      createdDate: prospectEntry.createdDate ? prospectEntry.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: prospectEntry.modifiedDate ? prospectEntry.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
