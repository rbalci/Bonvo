import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useApp } from './AppContext';
import LoginPage from './pages/LoginPage';
import AppLayout from './components/AppLayout';
import HomePage from './pages/HomePage';
import NewTripPage from './pages/NewTripPage';
import VocabPage from './pages/VocabPage';
import ProfilePage from './pages/ProfilePage';

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isLoggedIn } = useApp();
  return isLoggedIn ? <>{children}</> : <Navigate to="/" replace />;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route
          element={
            <ProtectedRoute>
              <AppLayout />
            </ProtectedRoute>
          }
        >
          <Route path="/home" element={<HomePage />} />
          <Route path="/new-trip" element={<NewTripPage />} />
          <Route path="/vocab" element={<VocabPage />} />
          <Route path="/profile" element={<ProfilePage />} />
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
