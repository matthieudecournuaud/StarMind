import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../prospect-board.test-samples';

import { ProspectBoardFormService } from './prospect-board-form.service';

describe('ProspectBoard Form Service', () => {
  let service: ProspectBoardFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProspectBoardFormService);
  });

  describe('Service methods', () => {
    describe('createProspectBoardFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProspectBoardFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            owner: expect.any(Object),
          }),
        );
      });

      it('passing IProspectBoard should create a new form with FormGroup', () => {
        const formGroup = service.createProspectBoardFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            owner: expect.any(Object),
          }),
        );
      });
    });

    describe('getProspectBoard', () => {
      it('should return NewProspectBoard for default ProspectBoard initial value', () => {
        const formGroup = service.createProspectBoardFormGroup(sampleWithNewData);

        const prospectBoard = service.getProspectBoard(formGroup) as any;

        expect(prospectBoard).toMatchObject(sampleWithNewData);
      });

      it('should return NewProspectBoard for empty ProspectBoard initial value', () => {
        const formGroup = service.createProspectBoardFormGroup();

        const prospectBoard = service.getProspectBoard(formGroup) as any;

        expect(prospectBoard).toMatchObject({});
      });

      it('should return IProspectBoard', () => {
        const formGroup = service.createProspectBoardFormGroup(sampleWithRequiredData);

        const prospectBoard = service.getProspectBoard(formGroup) as any;

        expect(prospectBoard).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProspectBoard should not enable id FormControl', () => {
        const formGroup = service.createProspectBoardFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProspectBoard should disable id FormControl', () => {
        const formGroup = service.createProspectBoardFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
