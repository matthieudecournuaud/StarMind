import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { IdeaChatDetailComponent } from './idea-chat-detail.component';

describe('IdeaChat Management Detail Component', () => {
  let comp: IdeaChatDetailComponent;
  let fixture: ComponentFixture<IdeaChatDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IdeaChatDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./idea-chat-detail.component').then(m => m.IdeaChatDetailComponent),
              resolve: { ideaChat: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(IdeaChatDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IdeaChatDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ideaChat on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', IdeaChatDetailComponent);

      // THEN
      expect(instance.ideaChat()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
