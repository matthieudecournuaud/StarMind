import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IIdea } from 'app/entities/idea/idea.model';
import { IdeaService } from 'app/entities/idea/service/idea.service';
import { LikeHistoryService } from '../service/like-history.service';
import { ILikeHistory } from '../like-history.model';
import { LikeHistoryFormService } from './like-history-form.service';

import { LikeHistoryUpdateComponent } from './like-history-update.component';

describe('LikeHistory Management Update Component', () => {
  let comp: LikeHistoryUpdateComponent;
  let fixture: ComponentFixture<LikeHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let likeHistoryFormService: LikeHistoryFormService;
  let likeHistoryService: LikeHistoryService;
  let ideaService: IdeaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LikeHistoryUpdateComponent],
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
      .overrideTemplate(LikeHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LikeHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    likeHistoryFormService = TestBed.inject(LikeHistoryFormService);
    likeHistoryService = TestBed.inject(LikeHistoryService);
    ideaService = TestBed.inject(IdeaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Idea query and add missing value', () => {
      const likeHistory: ILikeHistory = { id: 456 };
      const idea: IIdea = { id: 17157 };
      likeHistory.idea = idea;

      const ideaCollection: IIdea[] = [{ id: 27855 }];
      jest.spyOn(ideaService, 'query').mockReturnValue(of(new HttpResponse({ body: ideaCollection })));
      const additionalIdeas = [idea];
      const expectedCollection: IIdea[] = [...additionalIdeas, ...ideaCollection];
      jest.spyOn(ideaService, 'addIdeaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ likeHistory });
      comp.ngOnInit();

      expect(ideaService.query).toHaveBeenCalled();
      expect(ideaService.addIdeaToCollectionIfMissing).toHaveBeenCalledWith(
        ideaCollection,
        ...additionalIdeas.map(expect.objectContaining),
      );
      expect(comp.ideasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const likeHistory: ILikeHistory = { id: 456 };
      const idea: IIdea = { id: 19229 };
      likeHistory.idea = idea;

      activatedRoute.data = of({ likeHistory });
      comp.ngOnInit();

      expect(comp.ideasSharedCollection).toContain(idea);
      expect(comp.likeHistory).toEqual(likeHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikeHistory>>();
      const likeHistory = { id: 123 };
      jest.spyOn(likeHistoryFormService, 'getLikeHistory').mockReturnValue(likeHistory);
      jest.spyOn(likeHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likeHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: likeHistory }));
      saveSubject.complete();

      // THEN
      expect(likeHistoryFormService.getLikeHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(likeHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(likeHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikeHistory>>();
      const likeHistory = { id: 123 };
      jest.spyOn(likeHistoryFormService, 'getLikeHistory').mockReturnValue({ id: null });
      jest.spyOn(likeHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likeHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: likeHistory }));
      saveSubject.complete();

      // THEN
      expect(likeHistoryFormService.getLikeHistory).toHaveBeenCalled();
      expect(likeHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikeHistory>>();
      const likeHistory = { id: 123 };
      jest.spyOn(likeHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likeHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(likeHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareIdea', () => {
      it('Should forward to ideaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ideaService, 'compareIdea');
        comp.compareIdea(entity, entity2);
        expect(ideaService.compareIdea).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
