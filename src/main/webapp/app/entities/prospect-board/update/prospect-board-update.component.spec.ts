import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ProspectBoardService } from '../service/prospect-board.service';
import { IProspectBoard } from '../prospect-board.model';
import { ProspectBoardFormService } from './prospect-board-form.service';

import { ProspectBoardUpdateComponent } from './prospect-board-update.component';

describe('ProspectBoard Management Update Component', () => {
  let comp: ProspectBoardUpdateComponent;
  let fixture: ComponentFixture<ProspectBoardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prospectBoardFormService: ProspectBoardFormService;
  let prospectBoardService: ProspectBoardService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProspectBoardUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProspectBoardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProspectBoardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prospectBoardFormService = TestBed.inject(ProspectBoardFormService);
    prospectBoardService = TestBed.inject(ProspectBoardService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const prospectBoard: IProspectBoard = { id: 456 };
      const owner: IUser = { id: 'b5994874-6b21-451d-8229-183d20c812d6' };
      prospectBoard.owner = owner;

      const userCollection: IUser[] = [{ id: 'd2205912-dae7-4940-b594-172e68285b87' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [owner];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prospectBoard });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prospectBoard: IProspectBoard = { id: 456 };
      const owner: IUser = { id: '5918b1c3-38a3-42fb-b6a4-00e393d1c067' };
      prospectBoard.owner = owner;

      activatedRoute.data = of({ prospectBoard });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(owner);
      expect(comp.prospectBoard).toEqual(prospectBoard);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProspectBoard>>();
      const prospectBoard = { id: 123 };
      jest.spyOn(prospectBoardFormService, 'getProspectBoard').mockReturnValue(prospectBoard);
      jest.spyOn(prospectBoardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prospectBoard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prospectBoard }));
      saveSubject.complete();

      // THEN
      expect(prospectBoardFormService.getProspectBoard).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(prospectBoardService.update).toHaveBeenCalledWith(expect.objectContaining(prospectBoard));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProspectBoard>>();
      const prospectBoard = { id: 123 };
      jest.spyOn(prospectBoardFormService, 'getProspectBoard').mockReturnValue({ id: null });
      jest.spyOn(prospectBoardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prospectBoard: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prospectBoard }));
      saveSubject.complete();

      // THEN
      expect(prospectBoardFormService.getProspectBoard).toHaveBeenCalled();
      expect(prospectBoardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProspectBoard>>();
      const prospectBoard = { id: 123 };
      jest.spyOn(prospectBoardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prospectBoard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prospectBoardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
