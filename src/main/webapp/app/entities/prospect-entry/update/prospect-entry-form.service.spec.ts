import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../prospect-entry.test-samples';

import { ProspectEntryFormService } from './prospect-entry-form.service';

describe('ProspectEntry Form Service', () => {
  let service: ProspectEntryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProspectEntryFormService);
  });

  describe('Service methods', () => {
    describe('createProspectEntryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProspectEntryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            prospect: expect.any(Object),
            prospectBoard: expect.any(Object),
          }),
        );
      });

      it('passing IProspectEntry should create a new form with FormGroup', () => {
        const formGroup = service.createProspectEntryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            prospect: expect.any(Object),
            prospectBoard: expect.any(Object),
          }),
        );
      });
    });

    describe('getProspectEntry', () => {
      it('should return NewProspectEntry for default ProspectEntry initial value', () => {
        const formGroup = service.createProspectEntryFormGroup(sampleWithNewData);

        const prospectEntry = service.getProspectEntry(formGroup) as any;

        expect(prospectEntry).toMatchObject(sampleWithNewData);
      });

      it('should return NewProspectEntry for empty ProspectEntry initial value', () => {
        const formGroup = service.createProspectEntryFormGroup();

        const prospectEntry = service.getProspectEntry(formGroup) as any;

        expect(prospectEntry).toMatchObject({});
      });

      it('should return IProspectEntry', () => {
        const formGroup = service.createProspectEntryFormGroup(sampleWithRequiredData);

        const prospectEntry = service.getProspectEntry(formGroup) as any;

        expect(prospectEntry).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProspectEntry should not enable id FormControl', () => {
        const formGroup = service.createProspectEntryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProspectEntry should disable id FormControl', () => {
        const formGroup = service.createProspectEntryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
