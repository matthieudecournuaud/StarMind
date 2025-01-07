import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { GlobalChatDetailComponent } from './global-chat-detail.component';

describe('GlobalChat Management Detail Component', () => {
  let comp: GlobalChatDetailComponent;
  let fixture: ComponentFixture<GlobalChatDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GlobalChatDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./global-chat-detail.component').then(m => m.GlobalChatDetailComponent),
              resolve: { globalChat: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(GlobalChatDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GlobalChatDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load globalChat on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', GlobalChatDetailComponent);

      // THEN
      expect(instance.globalChat()).toEqual(expect.objectContaining({ id: 123 }));
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
