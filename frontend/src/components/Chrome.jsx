import { Anchor, LogOut, Menu, ShipWheel, Skull } from 'lucide-react';
import { RANKS } from '../utils/gameMeta.js';

const nav = [
  { id: 'dashboard', label: 'Профиль', group: 'каюта', icon: 'P' },
  { id: 'team', label: 'Моя команда', group: 'каюта', icon: 'T' },
  { id: 'fleet', label: 'Флот', group: 'море', icon: 'F' },
  { id: 'islands', label: 'Острова', group: 'море', icon: 'I' },
  { id: 'market', label: 'Маркет', group: 'порт', icon: '$' },
  { id: 'ships', label: 'Корабли', group: 'порт', icon: 'S' },
];

export function TopBar({ pirate, onLogout }) {
  return (
    <header className="topbar">
      <div className="brand">
        <span className="mark" aria-hidden="true"><Skull size={14} /></span>
        <b>PIRATETEAM</b>
        <span className="version">React</span>
      </div>
      <div className="top-actions">
        <span className="live">SERVER ONLINE</span>
        {pirate ? (
          <div className="me">
            <div className="avatar">{pirate.firstName?.[0] || pirate.login?.[0] || '@'}</div>
            <div>
              <span>{pirate.login}</span>
              <small>{RANKS[pirate.rank] || pirate.rank || 'Пират'}</small>
            </div>
          </div>
        ) : null}
        <button className="icon-button" type="button" onClick={onLogout} title="Выйти">
          <LogOut size={18} />
        </button>
      </div>
    </header>
  );
}

export function Sidebar({ activePage, setActivePage }) {
  return (
    <aside className="side">
      {['каюта', 'море', 'порт'].map((group) => (
        <div className="nav-group" key={group}>
          <div className="group">{group}</div>
          {nav.filter((item) => item.group === group).map((item) => (
            <button
              className={`nav-item ${activePage === item.id ? 'active' : ''}`}
              key={item.id}
              type="button"
              onClick={() => setActivePage(item.id)}
            >
              <span className="ico">{item.icon}</span>
              <span>{item.label}</span>
            </button>
          ))}
        </div>
      ))}
      <div className="nav-spacer" />
      <div className="dock-note">
        <Anchor size={16} />
        <span>Бэк: Spring Boot `:8080`</span>
      </div>
    </aside>
  );
}

export function EmptyState({ title, children }) {
  return (
    <div className="empty-state">
      <ShipWheel size={32} />
      <h3>{title}</h3>
      <p>{children}</p>
    </div>
  );
}

export function PageShell({ pirate, activePage, setActivePage, onLogout, children }) {
  return (
    <>
      <TopBar pirate={pirate} onLogout={onLogout} />
      <div className="shell">
        <Sidebar activePage={activePage} setActivePage={setActivePage} />
        <main className="main">{children}</main>
      </div>
    </>
  );
}
