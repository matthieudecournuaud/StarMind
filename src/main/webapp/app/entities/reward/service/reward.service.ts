import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReward, NewReward } from '../reward.model';

export type PartialUpdateReward = Partial<IReward> & Pick<IReward, 'id'>;

export type EntityResponseType = HttpResponse<IReward>;
export type EntityArrayResponseType = HttpResponse<IReward[]>;

@Injectable({ providedIn: 'root' })
export class RewardService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rewards');

  create(reward: NewReward): Observable<EntityResponseType> {
    return this.http.post<IReward>(this.resourceUrl, reward, { observe: 'response' });
  }

  update(reward: IReward): Observable<EntityResponseType> {
    return this.http.put<IReward>(`${this.resourceUrl}/${this.getRewardIdentifier(reward)}`, reward, { observe: 'response' });
  }

  partialUpdate(reward: PartialUpdateReward): Observable<EntityResponseType> {
    return this.http.patch<IReward>(`${this.resourceUrl}/${this.getRewardIdentifier(reward)}`, reward, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReward>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReward[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRewardIdentifier(reward: Pick<IReward, 'id'>): number {
    return reward.id;
  }

  compareReward(o1: Pick<IReward, 'id'> | null, o2: Pick<IReward, 'id'> | null): boolean {
    return o1 && o2 ? this.getRewardIdentifier(o1) === this.getRewardIdentifier(o2) : o1 === o2;
  }

  addRewardToCollectionIfMissing<Type extends Pick<IReward, 'id'>>(
    rewardCollection: Type[],
    ...rewardsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rewards: Type[] = rewardsToCheck.filter(isPresent);
    if (rewards.length > 0) {
      const rewardCollectionIdentifiers = rewardCollection.map(rewardItem => this.getRewardIdentifier(rewardItem));
      const rewardsToAdd = rewards.filter(rewardItem => {
        const rewardIdentifier = this.getRewardIdentifier(rewardItem);
        if (rewardCollectionIdentifiers.includes(rewardIdentifier)) {
          return false;
        }
        rewardCollectionIdentifiers.push(rewardIdentifier);
        return true;
      });
      return [...rewardsToAdd, ...rewardCollection];
    }
    return rewardCollection;
  }
}
