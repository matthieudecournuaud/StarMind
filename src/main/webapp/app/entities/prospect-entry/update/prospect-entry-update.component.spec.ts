import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IProspectBoard } from 'app/entities/prospect-board/prospect-board.model';
import { ProspectBoardService } from 'app/entities/prospect-board/service/prospect-board.service';
import { IProspectEntry } from '../prospect-entry.model';
import { ProspectEntryService } from '../service/prospect-entry.service';
import { ProspectEntryFormService } from './prospect-entry-form.service';

import { ProspectEntryUpdateComponent } from './prospect-entry-update.component';

describe('ProspectEntry Management Update Component', () => {
  let comp: ProspectEntryUpdateComponent;
  let fixture: ComponentFixture<ProspectEntryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prospectEntryFormService: ProspectEntryFormService;
  let prospectEntryService: ProspectEntryService;
  let userService: UserService;
  let prospectBoardService: ProspectBoardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProspectEntryUpdateComponent],
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
      .overrideTemplate(ProspectEntryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProspectEntryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prospectEntryFormService = TestBed.inject(ProspectEntryFormService);
    prospectEntryService = TestBed.inject(ProspectEntryService);
    userService = TestBed.inject(UserService);
    prospectBoardService = TestBed.inject(ProspectBoardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const prospectEntry: IProspectEntry = { id: 456 };
      const prospect: IUser = { id: '4e0b4f9e-7470-4db9-bf42-4ed54531499a' };
      prospectEntry.prospect = prospect;

      const userCollection: IUser[] = [{ id: 'a16c53c3-9c7f-405d-ab31-bda086e380f4' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [prospect];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prospectEntry });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ProspectBoard query and add missing value', () => {
      const prospectEntry: IProspectEntry = { id: 456 };
      const prospectBoard: IProspectBoard = { id: 4141 };
      prospectEntry.prospectBoard = prospectBoard;

      const prospectBoardCollection: IProspectBoard[] = [{ id: 6684 }];
      jest.spyOn(prospectBoardService, 'query').mockReturnValue(of(new HttpResponse({ body: prospectBoardCollection })));
      const additionalProspectBoards = [prospectBoard];
      const expectedCollection: IProspectBoard[] = [...additionalProspectBoards, ...prospectBoardCollection];
      jest.spyOn(prospectBoardService, 'addProspectBoardToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prospectEntry });
      comp.ngOnInit();

      expect(prospectBoardService.query).toHaveBeenCalled();
      expect(prospectBoardService.addProspectBoardToCollectionIfMissing).toHaveBeenCalledWith(
        prospectBoardCollection,
        ...additionalProspectBoards.map(expect.objectContaining),
      );
      expect(comp.prospectBoardsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prospectEntry: IProspectEntry = { id: 456 };
      const prospect: IUser = { id: '013efa00-6146-40f8-a6af-72254393a3bc' };
      prospectEntry.prospect = prospect;
      const prospectBoard: IProspectBoard = { id: 19556 };
      prospectEntry.prospectBoard = prospectBoard;

      activatedRoute.data = of({ prospectEntry });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(prospect);
      expect(comp.prospectBoardsSharedCollection).toContain(prospectBoard);
      expect(comp.prospectEntry).toEqual(prospectEntry);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProspectEntry>>();
      const prospectEntry = { id: 123 };
      jest.spyOn(prospectEntryFormService, 'getProspectEntry').mockReturnValue(prospectEntry);
      jest.spyOn(prospectEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prospectEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prospectEntry }));
      saveSubject.complete();

      // THEN
      expect(prospectEntryFormService.getProspectEntry).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(prospectEntryService.update).toHaveBeenCalledWith(expect.objectContaining(prospectEntry));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProspectEntry>>();
      const prospectEntry = { id: 123 };
      jest.spyOn(prospectEntryFormService, 'getProspectEntry').mockReturnValue({ id: null });
      jest.spyOn(prospectEntryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prospectEntry: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prospectEntry }));
      saveSubject.complete();

      // THEN
      expect(prospectEntryFormService.getProspectEntry).toHaveBeenCalled();
      expect(prospectEntryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProspectEntry>>();
      const prospectEntry = { id: 123 };
      jest.spyOn(prospectEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prospectEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prospectEntryService.update).toHaveBeenCalled();
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

    describe('compareProspectBoard', () => {
      it('Should forward to prospectBoardService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(prospectBoardService, 'compareProspectBoard');
        comp.compareProspectBoard(entity, entity2);
        expect(prospectBoardService.compareProspectBoard).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
