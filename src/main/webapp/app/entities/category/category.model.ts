export interface ICategory {
  id: number;
  name?: string | null;
  description?: string | null;
  level?: string | null;
  parentCategory?: ICategory | null;
  superCategory?: ICategory | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
