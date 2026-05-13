import { Outlet, NavLink, useNavigate } from 'react-router-dom';
import { useApp } from '../AppContext';
import styles from './AppLayout.module.css';

export default function AppLayout() {
  const { user } = useApp();
  const navigate = useNavigate();

  const initials = user?.avatarInitials ?? '?';

  return (
    <div className={styles.shell}>
      <header className={styles.header}>
        <div className={styles.logo}>
          bon<span>vo</span>
        </div>
        <button className={styles.avatar} onClick={() => navigate('/')}>
          {initials}
        </button>
      </header>

      <main className={styles.main}>
        <Outlet />
      </main>

      <nav className={styles.bottomNav}>
        <NavLink to="/home" className={({ isActive }) => `${styles.navItem} ${isActive ? styles.active : ''}`}>
          <i className="ti ti-home" />
          <span>Ana Sayfa</span>
        </NavLink>
        <NavLink to="/new-trip" className={({ isActive }) => `${styles.navItem} ${isActive ? styles.active : ''}`}>
          <i className="ti ti-plus" />
          <span>Gezi</span>
        </NavLink>
        <NavLink to="/vocab" className={({ isActive }) => `${styles.navItem} ${isActive ? styles.active : ''}`}>
          <i className="ti ti-book" />
          <span>Kelimeler</span>
        </NavLink>
        <NavLink to="/profile" className={({ isActive }) => `${styles.navItem} ${isActive ? styles.active : ''}`}>
          <i className="ti ti-user" />
          <span>Profil</span>
        </NavLink>
      </nav>
    </div>
  );
}
