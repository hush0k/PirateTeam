import { useEffect, useMemo, useState } from 'react';
import { Banknote, Castle, Coins, Percent, ShieldPlus } from 'lucide-react';
import { fmtMoney, ISLAND_LEVELS, LOCATION_NAMES } from '../utils/gameMeta.js';

const LEVEL_META = {
  WILD_SHORE: { price: 0, required: null, gold: 1, reputation: 1 },
  BAY: { price: 1000, required: 'WILD_SHORE', gold: 1.9, reputation: 1.1 },
  FISHING_VILLAGE: { price: 2500, required: 'BAY', gold: 2.6, reputation: 0.9 },
  TRADE_POST: { price: 18000, required: 'FISHING_VILLAGE', gold: 3.8, reputation: 1.3 },
  HARBOR: { price: 22000, required: 'TRADE_POST', gold: 4.5, reputation: 1.15 },
  FREE_PORT: { price: 35000, required: 'HARBOR', gold: 4, reputation: 1.4 },
  FORTRESS_ISLAND: { price: 50000, required: 'HARBOR', gold: 3.2, reputation: 1.6 },
  SMUGGLER_DEN: { price: 5000, required: 'TRADE_POST', gold: 1.8, reputation: 1.8 },
  PIRATE_BAY: { price: 60000, required: 'SMUGGLER_DEN', gold: 1.4, reputation: 2.6 },
  PROSPEROUS_CITY: { price: 80000, required: 'FORTRESS_ISLAND', gold: 5.8, reputation: 0.8 },
};

const DEFENCE_NAMES = {
  CROWD: 'Толпа',
  MILITIA: 'Ополчение',
  GUARD_GROUP: 'Стража',
  TRAINED_GUARDS: 'Обученная стража',
  PROFESSIONAL_GUARDS: 'Проф. стража',
  VETERANS: 'Ветераны',
  ELITE_GUARD: 'Элитная гвардия',
  ROYAL_GUARD: 'Королевская гвардия',
  MERCENARIES: 'Наёмники',
  SPECIAL_FORCES: 'Спецназ',
};

export function IslandsPage({ pirate, fleet, islands, onAddTax, onWithdrawTax, onUpgrade, onTakeProfit, onUpgradeMarket }) {
  const ownIslands = useMemo(() => islands.filter((island) => island.ownerId === pirate.id), [islands, pirate.id]);
  const [selectedId, setSelectedId] = useState(ownIslands[0]?.id || islands[0]?.id || '');
  const [query, setQuery] = useState('');
  const [taxAmount, setTaxAmount] = useState(1);

  useEffect(() => {
    if (!selectedId && (ownIslands[0]?.id || islands[0]?.id)) setSelectedId(ownIslands[0]?.id || islands[0]?.id);
  }, [islands, ownIslands, selectedId]);

  const selected = islands.find((island) => island.id === selectedId) || ownIslands[0] || islands[0] || null;
  const owned = selected?.ownerId === pirate.id;
  const nextLevels = selected ? Object.entries(LEVEL_META).filter(([, meta]) => meta.required === selected.level) : [];
  const distance = selected && fleet ? Math.round(Math.hypot(Number(fleet.coordinateX || 0) - Number(selected.coordinateX || 0), Number(fleet.coordinateY || 0) - Number(selected.coordinateY || 0))) : null;
  const projectedProfit = selected ? Math.round(1500 * (LEVEL_META[selected.level]?.gold || 1) + Number(selected.population || 0) * 2 + Number(selected.goldTurnover || 0) * Number(selected.taxPercentage || 0) / 100) : 0;

  const visible = useMemo(() => {
    return islands.filter((island) => `${island.name} ${island.nickname || ''} ${island.location}`.toLowerCase().includes(query.toLowerCase()));
  }, [islands, query]);

  return (
    <>
      <div className="crumbs"><span>море</span><span>/</span><b>управление островами</b></div>
      <div className="page-h">
        <div>
          <h1>ОСТРОВА <span>И КОЛОНИИ</span></h1>
          <p>{ownIslands.length} твоих островов · {islands.length} всего на карте.</p>
        </div>
        <div className="wallet">Казна: <b>{fmtMoney(pirate.treasury)}</b></div>
      </div>

      {selected ? (
        <section className={`island-hero ${owned ? 'owned' : ''}`}>
          <div className="island-art" aria-hidden="true">
            <span className="land" />
            <span className="flag" />
            <span className="waves" />
          </div>
          <div>
            <span className="login">«{selected.nickname || LOCATION_NAMES[selected.location] || selected.location}»</span>
            <h2>{selected.name}</h2>
            <p>Координаты {selected.coordinateX}:{selected.coordinateY}{distance !== null ? ` · флот в ${distance} ед.` : ''}</p>
            <div className="island-tags">
              <span>{owned ? 'ТВОЙ ОСТРОВ' : 'ЦЕЛЬ'}</span>
              <span>{ISLAND_LEVELS[selected.level] || selected.level}</span>
              <span>{DEFENCE_NAMES[selected.defenseType] || selected.defenseType || 'оборона неизвестна'}</span>
              <span>{selected.area} км2</span>
            </div>
          </div>
          <div className="island-hero-actions">
            <button className="btn primary" type="button" disabled={!owned} onClick={() => onTakeProfit(selected.id)}><Coins size={16} /> Взять профит</button>
            <button className="btn ghost" type="button" disabled={!owned} onClick={() => onUpgradeMarket(selected.id)}><Banknote size={16} /> Улучшить рынок</button>
          </div>
        </section>
      ) : null}

      {selected ? (
        <div className="content-grid">
          <section className="card">
            <div className="card-h"><h3>Экономика</h3><span>tax · profit</span></div>
            <div className="card-b island-economy">
              <div className="spec-grid">
                <div><small>Население</small><b>{Number(selected.population || 0).toLocaleString('ru-RU')}</b></div>
                <div><small>Оборот</small><b>{fmtMoney(selected.goldTurnover)}</b></div>
                <div><small>Налог</small><b>{selected.taxPercentage}%</b></div>
                <div><small>Прогноз профита</small><b>{fmtMoney(projectedProfit)}</b></div>
              </div>
              <div className="tax-control">
                <div className="tax-bar"><span style={{ width: `${Math.min(100, Number(selected.taxPercentage || 0) * 2)}%` }} /></div>
                <div className="qty">
                  <button type="button" onClick={() => setTaxAmount(Math.max(0.1, taxAmount - 0.5))}>-</button>
                  <input type="number" min="0.1" step="0.1" value={taxAmount} onChange={(event) => setTaxAmount(Number(event.target.value))} />
                  <button type="button" onClick={() => setTaxAmount(taxAmount + 0.5)}>+</button>
                </div>
                <div className="action-row">
                  <button className="btn ghost" type="button" disabled={!owned} onClick={() => onAddTax(selected.id, taxAmount)}><Percent size={14} /> Повысить</button>
                  <button className="btn ghost" type="button" disabled={!owned} onClick={() => onWithdrawTax(selected.id, taxAmount)}><Percent size={14} /> Снизить</button>
                </div>
              </div>
            </div>
          </section>

          <section className="card">
            <div className="card-h"><h3>Развитие</h3><span>{nextLevels.length ? `${nextLevels.length} путей` : 'max'}</span></div>
            <div className="card-b action-stack">
              {nextLevels.length ? nextLevels.map(([level, meta]) => (
                <button className="btn ghost" type="button" disabled={!owned} key={level} onClick={() => onUpgrade(selected.id, level)}>
                  <ShieldPlus size={15} /> {ISLAND_LEVELS[level]} · {fmtMoney(meta.price)}
                </button>
              )) : <p className="muted">Для текущего уровня нет следующего апгрейда.</p>}
            </div>
          </section>
        </div>
      ) : null}

      <div className="toolbar">
        <input placeholder="Найти остров..." value={query} onChange={(event) => setQuery(event.target.value)} />
      </div>

      <section className="island-grid">
        {visible.map((island) => {
          const isOwned = island.ownerId === pirate.id;
          return (
            <button className={`island-card selectable ${selected?.id === island.id ? 'selected' : ''} ${isOwned ? 'owned' : ''}`} type="button" key={island.id} onClick={() => setSelectedId(island.id)}>
              <div className="island-banner">
                <Castle size={40} />
                <span>{isOwned ? 'МОЙ ОСТРОВ' : 'ЦЕЛЬ'}</span>
              </div>
              <div className="ship-body">
                <h3>{island.name}</h3>
                <p>{island.nickname || LOCATION_NAMES[island.location] || island.location}</p>
                <div className="spec-grid">
                  <div><small>Уровень</small><b>{ISLAND_LEVELS[island.level] || island.level}</b></div>
                  <div><small>Налог</small><b>{island.taxPercentage}%</b></div>
                  <div><small>X</small><b>{island.coordinateX}</b></div>
                  <div><small>Y</small><b>{island.coordinateY}</b></div>
                </div>
              </div>
            </button>
          );
        })}
      </section>
    </>
  );
}
