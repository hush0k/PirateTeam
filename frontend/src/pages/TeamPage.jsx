import { useMemo, useState } from 'react';
import { Shield, UserMinus, Users } from 'lucide-react';
import { EmptyState } from '../components/Chrome.jsx';
import { fmtMoney, RANKS } from '../utils/gameMeta.js';

export function TeamPage({ pirate, team, pirates, onCreateTeam, onRemovePirate, onCoup }) {
  const [name, setName] = useState('');
  const [query, setQuery] = useState('');
  const [filter, setFilter] = useState('all');
  const members = useMemo(() => {
    const ids = new Set(team?.pirateIds || []);
    return pirates
      .filter((item) => ids.has(item.id) || item.teamId === team?.id)
      .filter((item) => {
        if (filter === 'captain') return item.id === team?.capitanId;
        if (query) return `${item.login} ${item.firstName} ${item.lastName}`.toLowerCase().includes(query.toLowerCase());
        return true;
      });
  }, [filter, pirates, query, team]);

  if (!team) {
    return (
      <>
        <div className="crumbs"><span>каюта</span><span>/</span><b>моя команда</b></div>
        <div className="page-h">
          <div>
            <h1>МОЯ <span>КОМАНДА</span></h1>
            <p>Создай команду, чтобы собирать пиратов и управлять казной.</p>
          </div>
        </div>
        <section className="card create-team">
          <EmptyState title="Команды пока нет">Капитаном станет текущий пират: @{pirate.login}</EmptyState>
          <form onSubmit={(event) => { event.preventDefault(); onCreateTeam(name); }}>
            <label>
              <span>Название команды</span>
              <input value={name} onChange={(event) => setName(event.target.value)} minLength={3} placeholder="Чёрные паруса" required />
            </label>
            <button className="btn primary" type="submit"><Users size={18} /> Создать команду</button>
          </form>
        </section>
      </>
    );
  }

  return (
    <>
      <div className="crumbs"><span>каюта</span><span>/</span><b>моя команда</b></div>
      <div className="page-h">
        <div>
          <h1>МОЯ <span>КОМАНДА</span></h1>
          <p>{members.length} пиратов в строю · капитан @{pirates.find((item) => item.id === team.capitanId)?.login || pirate.login}</p>
        </div>
      </div>

      <section className="team-hero">
        <div>
          <h2>«{team.name}»</h2>
          <p>Казна, мораль и сила команды тянутся напрямую из `/api/teams/{team.id}`.</p>
          <div className="team-stats">
            <div><span>Казна</span><b>{fmtMoney(team.treasury)}</b></div>
            <div><span>Репутация</span><b>{team.reputation}</b></div>
            <div><span>Сила</span><b>{team.power}</b></div>
            <div><span>Мораль</span><b>{team.morale}/100</b></div>
            <div><span>Лояльность</span><b>{team.loyalty}/100</b></div>
            <div><span>Усталость</span><b>{team.fatigue}/100</b></div>
          </div>
        </div>
        <div className="crest-shield"><Shield size={54} /></div>
      </section>

      <div className="toolbar">
        <input placeholder="Найти пирата..." value={query} onChange={(event) => setQuery(event.target.value)} />
        <div className="seg">
          <button type="button" aria-selected={filter === 'all'} onClick={() => setFilter('all')}>Все</button>
          <button type="button" aria-selected={filter === 'captain'} onClick={() => setFilter('captain')}>Капитан</button>
        </div>
      </div>

      <section className="roster">
        {members.map((member) => {
          const isCaptain = member.id === team.capitanId;
          return (
            <article className={`pirate-card ${isCaptain ? 'captain' : ''}`} key={member.id}>
              <div className="pirate-head">
                <div className="avatar">{member.firstName?.[0] || member.login?.[0]}</div>
                <div>
                  <span>@{member.login}</span>
                  <h3>{member.firstName} {member.lastName}</h3>
                  <small>{RANKS[member.rank] || member.rank} · {member.country}</small>
                </div>
                <b>{isCaptain ? 'КАПИТАН' : 'ПИРАТ'}</b>
              </div>
              <div className="mini-stats">
                <span>Сила <b>{member.strength}</b></span>
                <span>Бой <b>{member.bloodlust}</b></span>
                <span>Ум <b>{member.intelligence}</b></span>
              </div>
              <div className="action-row">
                {!isCaptain ? <button className="btn ghost" type="button" onClick={() => onRemovePirate(member.id)}><UserMinus size={16} /> Изгнать</button> : null}
                {!isCaptain ? <button className="btn ghost danger-text" type="button" onClick={() => onCoup(member.id)}>Мятеж</button> : null}
              </div>
            </article>
          );
        })}
      </section>
    </>
  );
}
