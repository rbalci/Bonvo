import { useState } from 'react';
import { useApp } from '../AppContext';
import { VocabWord, WordStatus } from '../types';
import styles from './VocabPage.module.css';

type Tab = 'all' | 'learning' | 'learned' | 'hard';

const STATUS_ICON: Record<WordStatus, string> = {
  learned: '✅',
  learning: '🔄',
  hard: '❌',
};

export default function VocabPage() {
  const { vocabWords, updateWordStatus } = useApp();
  const [activeTab, setActiveTab] = useState<Tab>('all');

  // For now always show trip-1 words
  const tripWords = vocabWords.filter(w => w.tripId === 'trip-1');

  const counts = {
    learned: tripWords.filter(w => w.status === 'learned').length,
    learning: tripWords.filter(w => w.status === 'learning').length,
    hard: tripWords.filter(w => w.status === 'hard').length,
  };

  const filtered: VocabWord[] =
    activeTab === 'all'
      ? tripWords
      : tripWords.filter(w => w.status === activeTab);

  return (
    <div className={styles.page}>
      {/* Hero */}
      <div className={styles.hero}>
        <div className={styles.heroInfo}>
          <h2>🇯🇵 Tokyo Japonca</h2>
          <p>Gezi öncesi kelime listesi</p>
        </div>
        <div className={styles.stats}>
          <div className={styles.stat}>
            <div className={styles.statNum}>{counts.learned}</div>
            <div className={styles.statLabel}>Öğrendi</div>
          </div>
          <div className={styles.stat}>
            <div className={styles.statNum}>{counts.learning}</div>
            <div className={styles.statLabel}>Öğreniyor</div>
          </div>
          <div className={styles.stat}>
            <div className={styles.statNum}>{counts.hard}</div>
            <div className={styles.statLabel}>Zor</div>
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div className={styles.tabs}>
        {(['all', 'learning', 'learned', 'hard'] as Tab[]).map(tab => {
          const labelMap: Record<Tab, string> = {
            all: 'Tümü',
            learning: 'Öğreniyor',
            learned: 'Öğrendi',
            hard: 'Zor',
          };
          return (
            <button
              key={tab}
              className={`${styles.tab} ${activeTab === tab ? styles.tabActive : ''}`}
              onClick={() => setActiveTab(tab)}
            >
              {labelMap[tab]}
            </button>
          );
        })}
      </div>

      {/* Add Word */}
      <button className={styles.addBtn}>
        <i className="ti ti-plus" /> Kelime Ekle
      </button>

      {/* Word List */}
      <div className={styles.list}>
        {filtered.length === 0 && (
          <div className={styles.empty}>
            <div className={styles.emptyIcon}>📭</div>
            <h3>Bu kategoride kelime yok</h3>
            <p>Kelime ekleyerek başlayabilirsin.</p>
          </div>
        )}

        {filtered.map(word => (
          <div key={word.id} className={styles.wordCard}>
            <div className={`${styles.statusDot} ${styles[word.status]}`}>
              {STATUS_ICON[word.status]}
            </div>

            <div className={styles.wordBody}>
              <div className={styles.original}>{word.original}</div>
              <div className={styles.translation}>{word.translation}</div>
              {word.example && (
                <div className={styles.example}>{word.example}</div>
              )}
            </div>

            <div className={styles.actions}>
              {word.status !== 'learned' && (
                <button
                  className={`${styles.actionBtn} ${styles.btnLearned}`}
                  onClick={() => updateWordStatus(word.id, 'learned')}
                >
                  Öğrendim
                </button>
              )}
              {word.status !== 'hard' && (
                <button
                  className={`${styles.actionBtn} ${styles.btnHard}`}
                  onClick={() => updateWordStatus(word.id, 'hard')}
                >
                  Zor
                </button>
              )}
              {word.status !== 'learning' && (
                <button
                  className={`${styles.actionBtn} ${styles.btnLearning}`}
                  onClick={() => updateWordStatus(word.id, 'learning')}
                >
                  Tekrar
                </button>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
