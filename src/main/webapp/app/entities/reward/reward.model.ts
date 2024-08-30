export interface IReward {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewReward = Omit<IReward, 'id'> & { id: null };
