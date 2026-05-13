export interface User {
  id: string;
  name: string;
  email: string;
  avatarInitials: string;
}

export type TripType =
  | 'culture'
  | 'beach'
  | 'gastronomy'
  | 'nature'
  | 'shopping'
  | 'art';

export type TripStatus = 'planned' | 'ongoing' | 'completed';

export interface Trip {
  id: string;
  destination: string;
  country: string;
  flag: string;
  startDate: string;
  endDate: string;
  type: TripType;
  typeLabelTr: string;
  status: TripStatus;
  budget: number;
  travelers: number;
  accommodation: 'budget' | 'comfort' | 'luxury';
  interests: string[];
  progress: number; // 0-100
  daysLeft?: number;
}

export type WordStatus = 'learned' | 'learning' | 'hard';

export interface VocabWord {
  id: string;
  tripId: string;
  original: string;
  translation: string;
  example?: string;
  status: WordStatus;
}

export interface NewTripForm {
  destination: string;
  startDate: string;
  endDate: string;
  type: TripType;
  budget: number;
  travelers: number;
  accommodation: 'budget' | 'comfort' | 'luxury';
  interests: string[];
}
