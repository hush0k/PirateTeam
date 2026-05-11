import { useMemo, useState } from 'react';
import { Anchor, Crosshair, Gem, MapPinned, MousePointer2, Swords } from 'lucide-react';
import { EmptyState } from '../components/Chrome.jsx';
import { fmtMoney, ISLAND_LEVELS } from '../utils/gameMeta.js';

const MAP_SIZE = 1000;

export function FleetPage({ pirate, fleet, fleetStats, fleets, islands, onCreateFleet, onMove, onFindTreasure, onAttack, onCapture }) {
  const [name, setName] = useState(`${pirate.firstName || 'Pirate'} Fleet`);
  const [coordinateX, setCoordinateX] = useState(fleet?.coordinateX || 0);
  const [coordinateY, setCoordinateY] = useState(fleet?.coordinateY || 0);
  const [mode, setMode] = useState('explore');
  const [enemyFleetId, setEnemyFleetId] = useState('');
  const [islandId, setIslandId] = useState('');
  const [selectedIslandId, setSelectedIslandId] = useState('');
  const [lastResult, setLastResult] = useState(null);

  const enemies = useMemo(() => fleets.filter((item) => item.id !== fleet?.id), [fleet?.id, fleets]);
  const targetIslands = useMemo(() => islands.filter((item) => item.ownerId !== pirate.id), [islands, pirate.id]);
  const selectedIsland = islands.find((item) => item.id === selectedIslandId) || null;

  async function run(action) {
    try {
      const result = await action();
      setLastResult(result || { ok: true });
    } catch {
      setLastResult(null);
    }
  }

  function pickPoint(event) {
    if (mode !== 'move') return;
    const rect = event.currentTarget.getBoundingClientRect();
    const x = Math.round(((event.clientX - rect.left) / rect.width) * MAP_SIZE);
    const y = Math.round(((event.clientY - rect.top) / rect.height) * MAP_SIZE);
    setCoordinateX(x);
    setCoordinateY(y);
  }

  if (!fleet) {
    return (
      <>
        <div className="crumbs"><span>море</span><span>/</span><b>флот</b></div>
        <div className="page-h">
          <div>
            <h1>ФЛОТ · <span>КАРТА</span></h1>
            <p>Создай флот вручную или купи первый корабль в маркете.</p>
          </div>
        </div>
        <section className="card create-team">
          <EmptyState title="Флота пока нет">Флот нужен для движения, поиска сокровищ, атак и захвата островов.</EmptyState>
          <form onSubmit={(event) => { event.preventDefault(); onCreateFleet({ ownerId: pirate.id, name, coordinateX: 0, coordinateY: 0 }); }}>
            <label>
              <span>Название флота</span>
              <input value={name} onChange={(event) => setName(event.target.value)} minLength={3} required />
            </label>
            <button className="btn primary" type="submit"><Anchor size={18} /> Создать флот</button>
          </form>
        </section>
      </>
    );
  }

  return (
    <>
      <div className="crumbs"><span>море</span><span>/</span><b>карта · {fleet.name}</b></div>
      <div className="page-h">
        <div>
          <h1>КАРИБЫ · <span>КАРТА</span></h1>
          <p>{islands.length} островов · {enemies.length} чужих флотов · координаты {fleet.coordinateX}:{fleet.coordinateY}.</p>
        </div>
        <div className="action-row">
          <button className="btn ghost" type="button" onClick={() => setMode('move')}><MapPinned size={16} /> Переместить</button>
          <button className="btn primary" type="button" onClick={() => run(onFindTreasure)}><Gem size={16} /> Искать сокровище</button>
        </div>
      </div>

      <section className="fleet-stats">
        <div className="stat"><span>Порох</span><b>{fleet.ammo}</b><small>ammo</small></div>
        <div className="stat"><span>Провизия</span><b>{fleet.provision}</b><small>provision</small></div>
        <div className="stat"><span>Военная мощь</span><b>{fleetStats?.militaryPower ?? '-'}</b><small>GET /stats</small></div>
        <div className="stat"><span>Абордаж</span><b>{fleetStats?.boardingPower ?? '-'}</b><small>boarding</small></div>
        <div className="stat"><span>Манёвр</span><b>{fleetStats?.manoeuvrability ?? '-'}</b><small>speed</small></div>
        <div className="stat"><span>Лут</span><b>{fleetStats?.lootMultiplier ?? '-'}</b><small>multiplier</small></div>
      </section>

      <section className="fleet-stage">
        <div className="fleet-map" onClick={pickPoint}>
          <div className="map-toolbar">
            <button className="tb" aria-selected={mode === 'explore'} type="button" onClick={(event) => { event.stopPropagation(); setMode('explore'); }} title="Обзор"><MousePointer2 size={16} /></button>
            <button className="tb" aria-selected={mode === 'move'} type="button" onClick={(event) => { event.stopPropagation(); setMode('move'); }} title="Курс"><MapPinned size={16} /></button>
            <button className="tb" aria-selected={mode === 'attack'} type="button" onClick={(event) => { event.stopPropagation(); setMode('attack'); }} title="Атака"><Swords size={16} /></button>
            <button className="tb" aria-selected={mode === 'capture'} type="button" onClick={(event) => { event.stopPropagation(); setMode('capture'); }} title="Захват"><Crosshair size={16} /></button>
          </div>
          <div className="map-coords">флот <b>{fleet.coordinateX}:{fleet.coordinateY}</b>{mode === 'move' ? ` · курс ${coordinateX}:${coordinateY}` : ''}</div>
          <div className={`mode-banner ${mode}`}>{modeText(mode)}</div>

          <span className="fleet-range" style={{ left: `${toPct(fleet.coordinateX)}%`, top: `${toPct(fleet.coordinateY)}%` }} />
          <span className="map-fleet mine" style={{ left: `${toPct(fleet.coordinateX)}%`, top: `${toPct(fleet.coordinateY)}%` }}><i /></span>
          {mode === 'move' ? <span className="map-target" style={{ left: `${toPct(coordinateX)}%`, top: `${toPct(coordinateY)}%` }} /> : null}

          {islands.map((island) => {
            const mine = island.ownerId === pirate.id;
            const selected = selectedIsland?.id === island.id;
            return (
              <button
                className={`map-island ${mine ? 'mine' : 'enemy'} ${selected ? 'selected' : ''}`}
                type="button"
                key={island.id}
                style={{ left: `${toPct(island.coordinateX)}%`, top: `${toPct(island.coordinateY)}%` }}
                onClick={(event) => {
                  event.stopPropagation();
                  setSelectedIslandId(island.id);
                  setIslandId(island.id);
                  if (mode === 'capture' && !mine) run(() => onCapture(island.id));
                }}
                title={island.name}
              >
                <span />
                <small>{island.name}</small>
              </button>
            );
          })}

          {enemies.map((item) => (
            <button
              className="map-fleet enemy"
              type="button"
              key={item.id}
              style={{ left: `${toPct(item.coordinateX)}%`, top: `${toPct(item.coordinateY)}%` }}
              onClick={(event) => {
                event.stopPropagation();
                setEnemyFleetId(item.id);
                if (mode === 'attack') run(() => onAttack(item.id));
              }}
              title={item.name}
            >
              <i />
            </button>
          ))}
        </div>

        <aside className="fleet-side">
          <section className="card">
            <div className="card-h"><h3>Курс</h3><span>POST /move</span></div>
            <form className="card-b inline-form" onSubmit={(event) => { event.preventDefault(); run(() => onMove({ coordinateX: Number(coordinateX), coordinateY: Number(coordinateY) })); }}>
              <label><span>X</span><input type="number" value={coordinateX} onChange={(event) => setCoordinateX(event.target.value)} /></label>
              <label><span>Y</span><input type="number" value={coordinateY} onChange={(event) => setCoordinateY(event.target.value)} /></label>
              <button className="btn primary" type="submit"><MapPinned size={16} /> Идти</button>
            </form>
          </section>

          <section className="card">
            <div className="card-h"><h3>Бой и захват</h3><span>gameplay</span></div>
            <div className="card-b action-stack">
              <select value={enemyFleetId} onChange={(event) => setEnemyFleetId(event.target.value)}>
                <option value="">Вражеский флот</option>
                {enemies.map((item) => <option value={item.id} key={item.id}>{item.name} · {item.coordinateX}:{item.coordinateY}</option>)}
              </select>
              <button className="btn ghost" type="button" disabled={!enemyFleetId} onClick={() => run(() => onAttack(enemyFleetId))}><Swords size={16} /> Атаковать</button>
              <select value={islandId} onChange={(event) => setIslandId(event.target.value)}>
                <option value="">Остров для захвата</option>
                {targetIslands.map((island) => <option value={island.id} key={island.id}>{island.name}</option>)}
              </select>
              <button className="btn primary" type="button" disabled={!islandId} onClick={() => run(() => onCapture(islandId))}><Crosshair size={16} /> Захватить</button>
            </div>
          </section>

          {selectedIsland ? (
            <section className="card">
              <div className="card-h"><h3>{selectedIsland.name}</h3><span>{ISLAND_LEVELS[selectedIsland.level] || selectedIsland.level}</span></div>
              <div className="card-b spec-grid">
                <div><small>Оборот</small><b>{fmtMoney(selectedIsland.goldTurnover)}</b></div>
                <div><small>Налог</small><b>{selectedIsland.taxPercentage}%</b></div>
                <div><small>Население</small><b>{Number(selectedIsland.population || 0).toLocaleString('ru-RU')}</b></div>
                <div><small>Владелец</small><b>{selectedIsland.ownerId === pirate.id ? 'ты' : selectedIsland.ownerId || 'нет'}</b></div>
              </div>
            </section>
          ) : null}
        </aside>
      </section>

      {lastResult ? <pre className="result-box">{JSON.stringify(lastResult, null, 2)}</pre> : null}
    </>
  );
}

function toPct(value) {
  return Math.max(2, Math.min(98, (Number(value || 0) / MAP_SIZE) * 100));
}

function modeText(mode) {
  if (mode === 'move') return 'Кликни по карте, чтобы выбрать курс';
  if (mode === 'attack') return 'Выбери чужой флот для атаки';
  if (mode === 'capture') return 'Выбери остров для захвата';
  return 'Обзор сектора';
}
