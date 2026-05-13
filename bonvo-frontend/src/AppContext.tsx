import React, { createContext, useContext, useState } from 'react';
import { Trip, VocabWord, User } from './types';
import { MOCK_TRIPS, MOCK_VOCAB_WORDS, MOCK_USER } from './data';

interface AppContextType {
  user: User | null;
  trips: Trip[];
  vocabWords: VocabWord[];
  isLoggedIn: boolean;
  login: (email: string, password: string) => void;
  logout: () => void;
  addTrip: (trip: Trip) => void;
  updateWordStatus: (wordId: string, status: VocabWord['status']) => void;
  addWord: (word: VocabWord) => void;
}

const AppContext = createContext<AppContextType | null>(null);

export function AppProvider({ children }: { children: React.ReactNode }) {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState<User | null>(null);
  const [trips, setTrips] = useState<Trip[]>(MOCK_TRIPS);
  const [vocabWords, setVocabWords] = useState<VocabWord[]>(MOCK_VOCAB_WORDS);

  function login(_email: string, _password: string) {
    setUser(MOCK_USER);
    setIsLoggedIn(true);
  }

  function logout() {
    setUser(null);
    setIsLoggedIn(false);
  }

  function addTrip(trip: Trip) {
    setTrips(prev => [trip, ...prev]);
  }

  function updateWordStatus(wordId: string, status: VocabWord['status']) {
    setVocabWords(prev =>
      prev.map(w => (w.id === wordId ? { ...w, status } : w))
    );
  }

  function addWord(word: VocabWord) {
    setVocabWords(prev => [...prev, word]);
  }

  return (
    <AppContext.Provider
      value={{
        user,
        trips,
        vocabWords,
        isLoggedIn,
        login,
        logout,
        addTrip,
        updateWordStatus,
        addWord,
      }}
    >
      {children}
    </AppContext.Provider>
  );
}

export function useApp() {
  const ctx = useContext(AppContext);
  if (!ctx) throw new Error('useApp must be used within AppProvider');
  return ctx;
}
