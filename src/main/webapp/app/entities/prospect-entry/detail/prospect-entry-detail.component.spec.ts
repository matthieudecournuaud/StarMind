import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProspectEntryDetailComponent } from './prospect-entry-detail.component';

describe('ProspectEntry Management Detail Component', () => {
  let comp: ProspectEntryDetailComponent;
  let fixture: ComponentFixture<ProspectEntryDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProspectEntryDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./prospect-entry-detail.component').then(m => m.ProspectEntryDetailComponent),
              resolve: { prospectEntry: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProspectEntryDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProspectEntryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prospectEntry on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProspectEntryDetailComponent);

      // THEN
      expect(instance.prospectEntry()).toEqual(expect.objectContaining({ id: 123 }));
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
