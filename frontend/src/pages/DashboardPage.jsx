import { useState } from 'react';
import { Coins, Crown, Flame, Gauge, Swords, Trophy } from 'lucide-react';
import { COUNTRY_NAMES, fmtMoney, RANKS, RANK_ORDER } from '../utils/gameMeta.js';

function StatBar({ icon, label, value, tone, onUpgrade, disabled }) {
  const pct = Math.max(0, Math.min(100, Number(value || 0)));
  return (
    <div className="stat-item">
      <span className={`stat-icon ${tone}`}>{icon}</span>
      <div className="stat-body">
        <div className="stat-label">
          <span>{label}</span>
          <b>{value}</b>
        </div>
        <div className={`bar ${tone}`}><span style={{ width: `${pct}%` }} /></div>
      </div>
      <button className="up-btn" type="button" disabled={disabled} onClick={onUpgrade}>+1</button>
    </div>
  );
}

export function DashboardPage({ pirate, team, fleet, onUpgrade, onUpgradeRank, onNavigate }) {
  const [busyStat, setBusyStat] = useState('');
  const rankIndex = Math.max(0, RANK_ORDER.indexOf(pirate?.rank));
  const canUpgrade = Number(pirate?.exp || 0) >= 10;
  const canUpgradeRank = Number(pirate?.exp || 0) >= 300 && rankIndex < RANK_ORDER.length - 1;

  async function upgrade(stat) {
    setBusyStat(stat);
    try {
      await onUpgrade(stat);
    } finally {
      setBusyStat('');
    }
  }

  return (
    <>
      <div className="crumbs"><span>каюта</span><span>/</span><b>профиль</b></div>
      <div className="page-h">
        <div>
          <h1>КАПИТАНСКИЙ <span>МОСТИК</span></h1>
          <p>Добро пожаловать обратно, {pirate.firstName}. Море ждёт.</p>
        </div>
        <div className="action-row">
          <button className="btn ghost" type="button" onClick={() => onNavigate('team')}>Команда</button>
          <button className="btn primary" type="button" onClick={() => onNavigate('market')}>В порт</button>
        </div>
      </div>

      <section className="hero-card">
        <div className="portrait">
          <div className="avatar-big">{pirate.firstName?.[0] || '@'}</div>
          <span>{COUNTRY_NAMES[pirate.country] || pirate.country}</span>
        </div>
        <div className="profile-copy">
          <span className="login">@{pirate.login}</span>
          <h2>{pirate.firstName} {pirate.lastName}</h2>
          <div className="rank-chip"><Crown size={16} /> {RANKS[pirate.rank] || pirate.rank}</div>
          <p>
            Репутация растёт через рейды, характеристики стоят 10 опыта, а новый ранг стоит 300 опыта.
          </p>
          <div className="action-row">
            <button className="btn ghost" type="button" onClick={onUpgradeRank} disabled={!canUpgradeRank}>Повысить ранг</button>
            <button className="btn ghost" type="button" onClick={() => onNavigate('market')}>Купить корабль</button>
          </div>
        </div>
        <div className="vault">
          <div><span>Казна</span><b>{fmtMoney(pirate.treasury)}</b><Coins size={18} /></div>
          <div><span>Репутация</span><b>{pirate.reputation}</b><Trophy size={18} /></div>
          <div><span>Опыт</span><b>{pirate.exp}</b><Gauge size={18} /></div>
          <div><span>Команда</span><b>{team?.name || 'нет команды'}</b><Swords size={18} /></div>
        </div>
      </section>

      <div className="content-grid">
        <section className="card">
          <div className="card-h"><h3>Характеристики</h3><span>PATCH /api/pirates</span></div>
          <div className="card-b stack">
            <StatBar icon={<Flame size={18} />} label="Жажда боя" value={pirate.bloodlust} tone="blood" disabled={!canUpgrade || busyStat === 'bloodlust'} onUpgrade={() => upgrade('bloodlust')} />
            <StatBar icon={<Gauge size={18} />} label="Интеллект" value={pirate.intelligence} tone="sea" disabled={!canUpgrade || busyStat === 'intelligence'} onUpgrade={() => upgrade('intelligence')} />
            <StatBar icon={<Swords size={18} />} label="Сила" value={pirate.strength} tone="gold" disabled={!canUpgrade || busyStat === 'strength'} onUpgrade={() => upgrade('strength')} />
          </div>
        </section>

        <section className="card">
          <div className="card-h"><h3>Флот</h3><span>GET /api/fleets/by-owner</span></div>
          <div className="card-b fleet-panel">
            {fleet ? (
              <>
                <div className="fleet-name">{fleet.name}</div>
                <div className="spec-grid">
                  <div><small>X</small><b>{fleet.coordinateX}</b></div>
                  <div><small>Y</small><b>{fleet.coordinateY}</b></div>
                  <div><small>Порох</small><b>{fleet.ammo}</b></div>
                  <div><small>Провизия</small><b>{fleet.provision}</b></div>
                </div>
              </>
            ) : (
              <p className="muted">Флот появится после покупки первого корабля в маркете.</p>
            )}
          </div>
        </section>
      </div>

      <section className="card">
        <div className="card-h"><h3>Путь рангов</h3><span>{rankIndex + 1}/{RANK_ORDER.length}</span></div>
        <div className="rank-rail">
          {RANK_ORDER.map((rank, index) => (
            <div className={`rank-step ${index < rankIndex ? 'done' : ''} ${index === rankIndex ? 'now' : ''}`} key={rank}>
              <span>{index + 1}</span>
              <small>{RANKS[rank]}</small>
            </div>
          ))}
        </div>
      </section>
    </>
  );
}
