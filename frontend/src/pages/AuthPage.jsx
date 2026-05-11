import { useMemo, useState } from 'react';
import { Eye, EyeOff, KeyRound, LogIn, UserPlus } from 'lucide-react';
import { COUNTRIES, getErrorMessage } from '../utils/gameMeta.js';

const emptyRegister = {
  login: '',
  firstName: '',
  lastName: '',
  dateOfBirth: '1995-01-01',
  password: '',
  country: 'ENGLAND',
};

export function AuthPage({ onLogin, onRegister }) {
  const [mode, setMode] = useState('login');
  const [login, setLogin] = useState('');
  const [password, setPassword] = useState('');
  const [register, setRegister] = useState(emptyRegister);
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  const passwordScore = useMemo(() => {
    const value = mode === 'login' ? password : register.password;
    return [
      value.length >= 8,
      /[a-z]/.test(value) && /[A-Z]/.test(value),
      /\d/.test(value),
      /[@$!%*?&_]/.test(value),
    ].filter(Boolean).length;
  }, [mode, password, register.password]);

  async function submit(event) {
    event.preventDefault();
    setError('');
    setBusy(true);

    try {
      if (mode === 'register') {
        await onRegister(register);
        await onLogin({ login: register.login, password: register.password });
      } else {
        await onLogin({ login, password });
      }
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setBusy(false);
    }
  }

  return (
    <div className="auth-layout">
      <div className="auth-panel">
        <div className="crest">
          <span className="seal">PT</span>
          <span>
            PirateTeam
            <small>поднять паруса</small>
          </span>
        </div>
        <h1>Твой <span>пиратский</span> рейс начинается здесь</h1>
        <p>Войди в аккаунт или создай пирата. Форма отправляет реальные запросы в Spring Boot: `/auth/login` и `/api/pirates`.</p>

        <div className="tabs" role="tablist">
          <button type="button" aria-selected={mode === 'login'} onClick={() => setMode('login')}>Вход</button>
          <button type="button" aria-selected={mode === 'register'} onClick={() => setMode('register')}>Регистрация</button>
        </div>

        <form className="auth-form" onSubmit={submit}>
          {mode === 'login' ? (
            <>
              <label>
                <span>Логин</span>
                <input value={login} onChange={(event) => setLogin(event.target.value)} autoComplete="username" required />
              </label>
              <label>
                <span>Пароль</span>
                <div className="input-action">
                  <input
                    type={showPassword ? 'text' : 'password'}
                    value={password}
                    onChange={(event) => setPassword(event.target.value)}
                    autoComplete="current-password"
                    required
                  />
                  <button type="button" onClick={() => setShowPassword((value) => !value)} title={showPassword ? 'Скрыть пароль' : 'Показать пароль'}>
                    {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                  </button>
                </div>
              </label>
            </>
          ) : (
            <>
              <div className="form-grid">
                <label>
                  <span>Логин</span>
                  <input value={register.login} onChange={(event) => setRegister({ ...register, login: event.target.value })} minLength={3} required />
                </label>
                <label>
                  <span>Страна</span>
                  <select value={register.country} onChange={(event) => setRegister({ ...register, country: event.target.value })}>
                    {COUNTRIES.map((country) => <option value={country.code} key={country.code}>{country.name}</option>)}
                  </select>
                </label>
              </div>
              <div className="form-grid">
                <label>
                  <span>Имя</span>
                  <input value={register.firstName} onChange={(event) => setRegister({ ...register, firstName: event.target.value })} minLength={3} required />
                </label>
                <label>
                  <span>Фамилия</span>
                  <input value={register.lastName} onChange={(event) => setRegister({ ...register, lastName: event.target.value })} minLength={3} required />
                </label>
              </div>
              <label>
                <span>Дата рождения</span>
                <input type="date" value={register.dateOfBirth} onChange={(event) => setRegister({ ...register, dateOfBirth: event.target.value })} required />
              </label>
              <label>
                <span>Пароль</span>
                <input
                  type="password"
                  value={register.password}
                  onChange={(event) => setRegister({ ...register, password: event.target.value })}
                  minLength={8}
                  required
                />
              </label>
            </>
          )}

          <div className="password-meter" aria-label="Надёжность пароля">
            {[0, 1, 2, 3].map((step) => <span className={step < passwordScore ? 'on' : ''} key={step} />)}
          </div>
          {error ? <div className="error-box">{error}</div> : null}
          <button className="btn primary" type="submit" disabled={busy}>
            {mode === 'login' ? <LogIn size={18} /> : <UserPlus size={18} />}
            {busy ? 'Связь с портом...' : mode === 'login' ? 'Войти' : 'Создать и войти'}
          </button>
        </form>
      </div>
      <section className="map-scene">
        <div className="scene-head">
          <h2>Карибские воды</h2>
          <p>Команды, флот, рынок кораблей и снабжение теперь живут в React и ходят в твой REST API.</p>
        </div>
        <div className="seamap">
          <i className="island i1" />
          <i className="island i2" />
          <i className="island i3" />
          <i className="ship s1" />
          <i className="ship s2" />
          <i className="ship s3" />
        </div>
      </section>
    </div>
  );
}
