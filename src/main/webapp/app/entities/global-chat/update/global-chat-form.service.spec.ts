import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../global-chat.test-samples';

import { GlobalChatFormService } from './global-chat-form.service';

describe('GlobalChat Form Service', () => {
  let service: GlobalChatFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GlobalChatFormService);
  });

  describe('Service methods', () => {
    describe('createGlobalChatFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGlobalChatFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            users: expect.any(Object),
          }),
        );
      });

      it('passing IGlobalChat should create a new form with FormGroup', () => {
        const formGroup = service.createGlobalChatFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            users: expect.any(Object),
          }),
        );
      });
    });

    describe('getGlobalChat', () => {
      it('should return NewGlobalChat for default GlobalChat initial value', () => {
        const formGroup = service.createGlobalChatFormGroup(sampleWithNewData);

        const globalChat = service.getGlobalChat(formGroup) as any;

        expect(globalChat).toMatchObject(sampleWithNewData);
      });

      it('should return NewGlobalChat for empty GlobalChat initial value', () => {
        const formGroup = service.createGlobalChatFormGroup();

        const globalChat = service.getGlobalChat(formGroup) as any;

        expect(globalChat).toMatchObject({});
      });

      it('should return IGlobalChat', () => {
        const formGroup = service.createGlobalChatFormGroup(sampleWithRequiredData);

        const globalChat = service.getGlobalChat(formGroup) as any;

        expect(globalChat).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGlobalChat should not enable id FormControl', () => {
        const formGroup = service.createGlobalChatFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGlobalChat should disable id FormControl', () => {
        const formGroup = service.createGlobalChatFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
