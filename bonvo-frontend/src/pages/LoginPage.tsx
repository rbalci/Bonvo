import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useApp } from '../AppContext';
import styles from './LoginPage.module.css';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useApp();
  const navigate = useNavigate();

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    login(email, password);
    navigate('/home');
  }

  return (
    <div className={styles.page}>
      <div className={styles.blob1} />
      <div className={styles.blob2} />

      <div className={styles.logo}>
        bon<span>vo</span>
      </div>
      <div className={styles.tagline}>AI Travel Assistant</div>

      <form className={styles.card} onSubmit={handleSubmit}>
        <h2>Hoş geldin ✈️</h2>

        <div className={styles.formGroup}>
          <label>E-posta</label>
          <input
            type="email"
            placeholder="sen@bonvo.ai"
            value={email}
            onChange={e => setEmail(e.target.value)}
            required
          />
        </div>

        <div className={styles.formGroup}>
          <label>Şifre</label>
          <input
            type="password"
            placeholder="••••••••"
            value={password}
            onChange={e => setPassword(e.target.value)}
            required
          />
        </div>

        <button type="submit" className={styles.btnPrimary}>
          Giriş Yap
        </button>

        <div className={styles.footer}>
          Hesabın yok mu? <a href="#">Kayıt Ol</a>
        </div>
      </form>
    </div>
  );
}
