import { useNavigate } from 'react-router-dom';
import { useApp } from '../AppContext';
import styles from './HomePage.module.css';

export default function HomePage() {
  const { user, trips } = useApp();
  const navigate = useNavigate();

  const firstName = user?.name.split(' ')[0] ?? 'Kullanıcı';

  const badgeClass = (type: string) => {
    if (type === 'gastronomy') return `${styles.badge} ${styles.badgeCulture}`;
    if (type === 'beach') return `${styles.badge} ${styles.badgeBeach}`;
    return styles.badge;
  };

  const typeLabel = (type: string) => {
    const map: Record<string, string> = {
      culture: 'Kültür',
      beach: 'Plaj',
      gastronomy: 'Gastronomi',
      nature: 'Doğa',
      shopping: 'Alışveriş',
      art: 'Sanat',
    };
    return map[type] ?? type;
  };

  return (
    <div className={styles.page}>
      {/* Hero */}
      <div className={styles.hero}>
        <div className={styles.heroGreeting}>Merhaba, {firstName} 👋</div>
        <div className={styles.heroTitle}>
          Bir sonraki<br />maceraya <span>hazır mısın?</span>
        </div>
        <button className={styles.btnNewTrip} onClick={() => navigate('/new-trip')}>
          <span className={styles.btnIcon}>+</span>
          Yeni Gezi Oluştur
        </button>
      </div>

      {/* Trips List */}
      <div className={styles.section}>
        <div className={styles.sectionTitle}>
          <span>Gezilerin</span>
          <a href="#">Tümünü Gör</a>
        </div>

        {trips.map(trip => (
          <div
            key={trip.id}
            className={styles.tripCard}
            onClick={() => navigate(`/vocab?tripId=${trip.id}`)}
          >
            <div className={styles.tripFlag}>{trip.flag}</div>
            <div className={styles.tripInfo}>
              <div className={styles.tripDest}>
                {trip.destination}, {trip.country}
              </div>
              <div className={styles.tripMeta}>
                <span>
                  {new Date(trip.startDate).toLocaleDateString('tr-TR', {
                    day: 'numeric',
                    month: 'short',
                    year: 'numeric',
                  })}
                </span>
                <span className={badgeClass(trip.type)}>{typeLabel(trip.type)}</span>
              </div>
              <div className={styles.progressWrap}>
                <div className={styles.progressFill} style={{ width: `${trip.progress}%` }} />
              </div>
            </div>
            <div className={styles.tripDate}>
              {trip.status === 'completed'
                ? 'Tamamlandı'
                : `${trip.daysLeft} gün\nkaldı`}
            </div>
          </div>
        ))}
      </div>

      {/* Quick Actions */}
      <div className={styles.section}>
        <div className={styles.sectionTitle}><span>Hızlı Erişim</span></div>
      </div>
      <div className={styles.quickGrid}>
        <div className={styles.quickCard} onClick={() => navigate('/vocab')}>
          <div className={`${styles.quickIcon} ${styles.qOrange}`}>📚</div>
          <div className={styles.quickLabel}>Kelime Defteri</div>
          <div className={styles.quickSub}>Tokyo için 42 kelime öğrendi</div>
        </div>
        <div className={styles.quickCard} onClick={() => navigate('/new-trip')}>
          <div className={`${styles.quickIcon} ${styles.qOrange}`}>✈️</div>
          <div className={styles.quickLabel}>Yeni Gezi</div>
          <div className={styles.quickSub}>AI ile gezi planı oluştur</div>
        </div>
        <div className={styles.quickCard}>
          <div className={`${styles.quickIcon} ${styles.qGreen}`}>🗺️</div>
          <div className={styles.quickLabel}>Rota Planı</div>
          <div className={styles.quickSub}>Günlük programını gör</div>
        </div>
        <div className={styles.quickCard}>
          <div className={`${styles.quickIcon} ${styles.qGreen}`}>💰</div>
          <div className={styles.quickLabel}>Bütçe</div>
          <div className={styles.quickSub}>Harcama takibi</div>
        </div>
      </div>
    </div>
  );
}
