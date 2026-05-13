import { useNavigate } from 'react-router-dom';
import { useApp } from '../AppContext';
import styles from './ProfilePage.module.css';

export default function ProfilePage() {
  const { user, logout } = useApp();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate('/');
  }

  return (
    <div className={styles.page}>
      <div className={styles.avatarWrap}>
        <div className={styles.avatar}>{user?.avatarInitials ?? '?'}</div>
        <div className={styles.name}>{user?.name}</div>
        <div className={styles.email}>{user?.email}</div>
      </div>

      <div className={styles.section}>
        <div className={styles.item}>
          <i className="ti ti-plane" /> Toplam Gezi
          <span>3</span>
        </div>
        <div className={styles.item}>
          <i className="ti ti-book" /> Öğrenilen Kelime
          <span>24</span>
        </div>
        <div className={styles.item}>
          <i className="ti ti-star" /> Tamamlanan Gezi
          <span>1</span>
        </div>
      </div>

      <button className={styles.logoutBtn} onClick={handleLogout}>
        <i className="ti ti-logout" /> Çıkış Yap
      </button>
    </div>
  );
}
