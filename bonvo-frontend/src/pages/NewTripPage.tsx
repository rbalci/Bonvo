import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useApp } from '../AppContext';
import { NewTripForm, TripType } from '../types';
import { TRIP_TYPES, INTERESTS } from '../data';
import styles from './NewTripPage.module.css';

const DEFAULT_FORM: NewTripForm = {
  destination: '',
  startDate: '',
  endDate: '',
  type: 'culture',
  budget: 25000,
  travelers: 2,
  accommodation: 'comfort',
  interests: ['museum', 'street_food', 'temple'],
};

export default function NewTripPage() {
  const [form, setForm] = useState<NewTripForm>(DEFAULT_FORM);
  const { addTrip } = useApp();
  const navigate = useNavigate();

  function setField<K extends keyof NewTripForm>(key: K, value: NewTripForm[K]) {
    setForm(prev => ({ ...prev, [key]: value }));
  }

  function toggleInterest(value: string) {
    setForm(prev => ({
      ...prev,
      interests: prev.interests.includes(value)
        ? prev.interests.filter(i => i !== value)
        : [...prev.interests, value],
    }));
  }

  function handleSubmit() {
    const typeInfo = TRIP_TYPES.find(t => t.value === form.type);
    const flagMap: Record<string, string> = {
      tokyo: '🇯🇵', japonya: '🇯🇵',
      paris: '🇫🇷', fransa: '🇫🇷',
      roma: '🇮🇹', italya: '🇮🇹',
      barselona: '🇪🇸', ispanya: '🇪🇸',
      londra: '🇬🇧', ingilizce: '🇬🇧',
      istanbul: '🇹🇷',
    };
    const flag = flagMap[form.destination.toLowerCase()] ?? '🌍';

    addTrip({
      id: `trip-${Date.now()}`,
      destination: form.destination,
      country: '',
      flag,
      startDate: form.startDate,
      endDate: form.endDate,
      type: form.type,
      typeLabelTr: typeInfo?.label ?? form.type,
      status: 'planned',
      budget: form.budget,
      travelers: form.travelers,
      accommodation: form.accommodation,
      interests: form.interests,
      progress: 0,
      daysLeft: form.startDate
        ? Math.max(0, Math.ceil((new Date(form.startDate).getTime() - Date.now()) / 86400000))
        : undefined,
    });

    navigate('/vocab');
  }

  const sliderPct = Math.round(((form.budget - 5000) / (100000 - 5000)) * 100);

  return (
    <div className={styles.page}>
      <div className={styles.titleBar}>
        <div className={styles.pageTitle}>Yeni Gezi 🗺️</div>
        <div className={styles.pageSub}>AI ile kişiselleştirilmiş plan oluştur</div>
      </div>

      <div className={styles.form}>

        {/* Destination */}
        <div className={styles.block}>
          <div className={styles.blockLabel}>Destinasyon</div>
          <input
            className={styles.input}
            type="text"
            placeholder="Nereye gitmek istiyorsun? (örn: Tokyo)"
            value={form.destination}
            onChange={e => setField('destination', e.target.value)}
          />
        </div>

        {/* Dates */}
        <div className={styles.block}>
          <div className={styles.blockLabel}>Tarihler</div>
          <div className={styles.dateRow}>
            <div>
              <div className={styles.subLabel}>Gidiş</div>
              <input
                className={styles.input}
                type="date"
                value={form.startDate}
                onChange={e => setField('startDate', e.target.value)}
              />
            </div>
            <div>
              <div className={styles.subLabel}>Dönüş</div>
              <input
                className={styles.input}
                type="date"
                value={form.endDate}
                onChange={e => setField('endDate', e.target.value)}
              />
            </div>
          </div>
        </div>

        {/* Trip Type */}
        <div className={styles.block}>
          <div className={styles.blockLabel}>Gezi Türü</div>
          <div className={styles.typeGrid}>
            {TRIP_TYPES.map(t => (
              <button
                key={t.value}
                className={`${styles.typeChip} ${form.type === t.value ? styles.chipSelected : ''}`}
                onClick={() => setField('type', t.value as TripType)}
              >
                <span className={styles.chipIcon}>{t.icon}</span>
                {t.label}
              </button>
            ))}
          </div>
        </div>

        {/* Budget */}
        <div className={styles.block}>
          <div className={styles.blockLabel}>Bütçe</div>
          <div className={styles.budgetDisplay}>
            ₺{form.budget.toLocaleString('tr-TR')}{' '}
            <span>kişi başı</span>
          </div>
          <input
            type="range"
            className={styles.slider}
            min={5000}
            max={100000}
            step={1000}
            value={form.budget}
            style={{
              background: `linear-gradient(to right, var(--orange) ${sliderPct}%, var(--light) ${sliderPct}%)`,
            }}
            onChange={e => setField('budget', Number(e.target.value))}
          />
          <div className={styles.sliderLabels}>
            <span>₺5K</span>
            <span>₺100K</span>
          </div>
        </div>

        {/* Interests */}
        <div className={styles.block}>
          <div className={styles.blockLabel}>İlgi Alanları</div>
          <div className={styles.interestTags}>
            {INTERESTS.map(i => (
              <button
                key={i.value}
                className={`${styles.tag} ${form.interests.includes(i.value) ? styles.tagSelected : ''}`}
                onClick={() => toggleInterest(i.value)}
              >
                {i.label}
              </button>
            ))}
          </div>
        </div>

        {/* Travelers & Accommodation */}
        <div className={styles.block}>
          <div className={styles.blockLabel}>Seyahat Bilgileri</div>
          <div className={styles.infoGrid}>
            <div>
              <div className={styles.subLabel}>Kişi Sayısı</div>
              <select
                className={styles.input}
                value={form.travelers}
                onChange={e => setField('travelers', Number(e.target.value))}
              >
                <option value={1}>1 Kişi</option>
                <option value={2}>2 Kişi</option>
                <option value={3}>3 Kişi</option>
                <option value={4}>4+ Kişi</option>
              </select>
            </div>
            <div>
              <div className={styles.subLabel}>Konaklama</div>
              <select
                className={styles.input}
                value={form.accommodation}
                onChange={e =>
                  setField('accommodation', e.target.value as NewTripForm['accommodation'])
                }
              >
                <option value="budget">Ekonomik</option>
                <option value="comfort">Konforlu</option>
                <option value="luxury">Lüks</option>
              </select>
            </div>
          </div>
        </div>

        <button className={styles.btnCreate} onClick={handleSubmit}>
          <i className="ti ti-sparkles" />
          AI ile Plan Oluştur
        </button>
      </div>
    </div>
  );
}
