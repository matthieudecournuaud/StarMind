export interface ICategory {
  id: number;
  name?: string | null;
  description?: string | null;
  level?: string | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
