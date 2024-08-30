import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IIdea } from 'app/entities/idea/idea.model';
import { IdeaService } from 'app/entities/idea/service/idea.service';
import { IdeaHistoryService } from '../service/idea-history.service';
import { IIdeaHistory } from '../idea-history.model';
import { IdeaHistoryFormService } from './idea-history-form.service';

import { IdeaHistoryUpdateComponent } from './idea-history-update.component';

describe('IdeaHistory Management Update Component', () => {
  let comp: IdeaHistoryUpdateComponent;
  let fixture: ComponentFixture<IdeaHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ideaHistoryFormService: IdeaHistoryFormService;
  let ideaHistoryService: IdeaHistoryService;
  let ideaService: IdeaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [IdeaHistoryUpdateComponent],
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
      .overrideTemplate(IdeaHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IdeaHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ideaHistoryFormService = TestBed.inject(IdeaHistoryFormService);
    ideaHistoryService = TestBed.inject(IdeaHistoryService);
    ideaService = TestBed.inject(IdeaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Idea query and add missing value', () => {
      const ideaHistory: IIdeaHistory = { id: 456 };
      const idea: IIdea = { id: 15357 };
      ideaHistory.idea = idea;

      const ideaCollection: IIdea[] = [{ id: 24369 }];
      jest.spyOn(ideaService, 'query').mockReturnValue(of(new HttpResponse({ body: ideaCollection })));
      const additionalIdeas = [idea];
      const expectedCollection: IIdea[] = [...additionalIdeas, ...ideaCollection];
      jest.spyOn(ideaService, 'addIdeaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ideaHistory });
      comp.ngOnInit();

      expect(ideaService.query).toHaveBeenCalled();
      expect(ideaService.addIdeaToCollectionIfMissing).toHaveBeenCalledWith(
        ideaCollection,
        ...additionalIdeas.map(expect.objectContaining),
      );
      expect(comp.ideasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ideaHistory: IIdeaHistory = { id: 456 };
      const idea: IIdea = { id: 20874 };
      ideaHistory.idea = idea;

      activatedRoute.data = of({ ideaHistory });
      comp.ngOnInit();

      expect(comp.ideasSharedCollection).toContain(idea);
      expect(comp.ideaHistory).toEqual(ideaHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIdeaHistory>>();
      const ideaHistory = { id: 123 };
      jest.spyOn(ideaHistoryFormService, 'getIdeaHistory').mockReturnValue(ideaHistory);
      jest.spyOn(ideaHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ideaHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ideaHistory }));
      saveSubject.complete();

      // THEN
      expect(ideaHistoryFormService.getIdeaHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ideaHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(ideaHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIdeaHistory>>();
      const ideaHistory = { id: 123 };
      jest.spyOn(ideaHistoryFormService, 'getIdeaHistory').mockReturnValue({ id: null });
      jest.spyOn(ideaHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ideaHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ideaHistory }));
      saveSubject.complete();

      // THEN
      expect(ideaHistoryFormService.getIdeaHistory).toHaveBeenCalled();
      expect(ideaHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIdeaHistory>>();
      const ideaHistory = { id: 123 };
      jest.spyOn(ideaHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ideaHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ideaHistoryService.update).toHaveBeenCalled();
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
