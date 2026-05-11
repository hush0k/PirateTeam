import { useMemo, useState } from 'react';
import { BadgeDollarSign, Boxes, Plus, Sailboat, UserCheck } from 'lucide-react';
import { COUNTRIES, COUNTRY_NAMES, fmtMoney, RANKS, SHIP_TYPES } from '../utils/gameMeta.js';

const defaultsByType = {
  SLOOP: { price: 10000, cargoCapacity: 300, maxCrew: 15, maxSpeed: 14 },
  BRIGANTINE: { price: 22000, cargoCapacity: 500, maxCrew: 30, maxSpeed: 12 },
  SCHOONER: { price: 35000, cargoCapacity: 600, maxCrew: 40, maxSpeed: 11 },
  BARQUE: { price: 60000, cargoCapacity: 900, maxCrew: 50, maxSpeed: 8 },
  FRIGATE: { price: 90000, cargoCapacity: 700, maxCrew: 80, maxSpeed: 10 },
  GALLEON: { price: 160000, cargoCapacity: 1400, maxCrew: 120, maxSpeed: 7 },
  MAN_OF_WAR: { price: 300000, cargoCapacity: 1000, maxCrew: 150, maxSpeed: 6 },
};

const initialShip = {
  name: '',
  constructionDate: '1700-01-01',
  shipType: 'SLOOP',
  builderCountry: 'ENGLAND',
  ownership: 'AVAILABLE_FOR_SALE',
  ...defaultsByType.SLOOP,
};

export function ShipsPage({ pirate, fleet, ships, pirates, shipStats, onCreateShip, onAssignCaptain, onLoadCargo, onOfferForSale }) {
  const [query, setQuery] = useState('');
  const [filter, setFilter] = useState('ALL');
  const [sort, setSort] = useState('tier');
  const [showCreate, setShowCreate] = useState(false);
  const [shipForm, setShipForm] = useState(initialShip);
  const [cargo, setCargo] = useState(50);
  const [captainId, setCaptainId] = useState(pirate.id);

  const captainOptions = useMemo(() => {
    const candidates = pirates.filter((item) => item.rank === 'CAPTAIN' || item.id === pirate.id);
    return candidates.length ? candidates : [pirate];
  }, [pirate, pirates]);

  const visible = useMemo(() => {
    return [...ships]
      .filter((ship) => filter === 'ALL' || ship.shipType === filter)
      .filter((ship) => !query || `${ship.name} ${ship.shipType} ${ship.ownership}`.toLowerCase().includes(query.toLowerCase()))
      .sort((a, b) => {
        if (sort === 'name') return a.name.localeCompare(b.name);
        if (sort === 'speed') return Number(b.maxSpeed || 0) - Number(a.maxSpeed || 0);
        if (sort === 'cargo') return Number(b.cargoCapacity || 0) - Number(a.cargoCapacity || 0);
        return (SHIP_TYPES[b.shipType]?.tier || 0) - (SHIP_TYPES[a.shipType]?.tier || 0);
      });
  }, [filter, query, ships, sort]);

  const totals = useMemo(() => {
    const owned = ships.filter((ship) => ship.ownerId === pirate.id || ship.capitanId === pirate.id || ship.fleetId === fleet?.id);
    const value = owned.reduce((sum, ship) => sum + Number(ship.price || 0), 0);
    const crew = owned.reduce((sum, ship) => sum + Number(ship.maxCrew || 0), 0);
    const cargoCap = shipStats?.maxCargo ?? owned.reduce((sum, ship) => sum + Number(ship.cargoCapacity || 0), 0);
    const filled = shipStats?.filledCargoSpace ?? 0;
    const avgSpeed = shipStats?.avgSpeed ?? (owned.length ? owned.reduce((sum, ship) => sum + Number(ship.maxSpeed || 0), 0) / owned.length : 0);
    const power = shipStats?.totalPower ?? owned.reduce((sum, ship) => sum + (SHIP_TYPES[ship.shipType]?.cannons || 0) * 10, 0);
    return { ownedCount: owned.length, value, crew: shipStats?.maxCrew ?? crew, cargoCap, filled, avgSpeed, power };
  }, [fleet?.id, pirate.id, shipStats, ships]);

  function setType(shipType) {
    setShipForm((value) => ({ ...value, shipType, ...defaultsByType[shipType] }));
  }

  return (
    <>
      <div className="crumbs"><span>порт</span><span>/</span><b>мои корабли</b></div>
      <div className="page-h">
        <div>
          <h1>МОИ <span>КОРАБЛИ</span></h1>
          <p>{totals.ownedCount} судов во флоте · {totals.crew} мест экипажа · реальные данные из `/api/ships`.</p>
        </div>
        <button className="btn primary" type="button" onClick={() => setShowCreate((value) => !value)}><Plus size={16} /> Новый корабль</button>
      </div>

      <section className="fleet-stats">
        <div className="stat"><span>Кораблей</span><b>{totals.ownedCount}</b><small>в твоём флоте</small></div>
        <div className="stat"><span>Сила</span><b>{totals.power}</b><small>combat power</small></div>
        <div className="stat"><span>Скорость</span><b>{Number(totals.avgSpeed || 0).toFixed(1)}</b><small>средняя</small></div>
        <div className="stat"><span>Экипаж</span><b>{totals.crew}</b><small>max crew</small></div>
        <div className="stat"><span>Трюм</span><b>{totals.filled}/{totals.cargoCap}</b><small>{totals.cargoCap ? Math.round((totals.filled / totals.cargoCap) * 100) : 0}% занят</small></div>
        <div className="stat"><span>Стоимость</span><b>{fmtMoney(totals.value)}</b><small>по цене кораблей</small></div>
      </section>

      {showCreate ? (
        <section className="card">
          <div className="card-h"><h3>Создать корабль</h3><span>POST /api/ships</span></div>
          <form className="card-b ship-form" onSubmit={(event) => { event.preventDefault(); onCreateShip(shipForm); }}>
            <label><span>Название</span><input value={shipForm.name} onChange={(event) => setShipForm({ ...shipForm, name: event.target.value })} minLength={3} required /></label>
            <label><span>Тип</span><select value={shipForm.shipType} onChange={(event) => setType(event.target.value)}>{Object.keys(SHIP_TYPES).map((type) => <option value={type} key={type}>{SHIP_TYPES[type].ru}</option>)}</select></label>
            <label><span>Страна</span><select value={shipForm.builderCountry} onChange={(event) => setShipForm({ ...shipForm, builderCountry: event.target.value })}>{COUNTRIES.map((country) => <option value={country.code} key={country.code}>{country.name}</option>)}</select></label>
            <label><span>Дата постройки</span><input type="date" value={shipForm.constructionDate} onChange={(event) => setShipForm({ ...shipForm, constructionDate: event.target.value })} required /></label>
            <label><span>Экипаж</span><input type="number" min="1" value={shipForm.maxCrew} onChange={(event) => setShipForm({ ...shipForm, maxCrew: Number(event.target.value) })} /></label>
            <label><span>Скорость</span><input type="number" min="1" value={shipForm.maxSpeed} onChange={(event) => setShipForm({ ...shipForm, maxSpeed: Number(event.target.value) })} /></label>
            <label><span>Трюм</span><input type="number" min="1" value={shipForm.cargoCapacity} onChange={(event) => setShipForm({ ...shipForm, cargoCapacity: Number(event.target.value) })} /></label>
            <label><span>Цена</span><input type="number" min="0" value={shipForm.price} onChange={(event) => setShipForm({ ...shipForm, price: Number(event.target.value) })} /></label>
            <button className="btn primary" type="submit"><Sailboat size={16} /> Создать</button>
          </form>
        </section>
      ) : null}

      <section className="cargo-card">
        <div>
          <h3>Распределить груз по флоту</h3>
          <p>Отправляет <code>PATCH /api/ships/by-fleet/:fleetId/load-cargo?amount=...</code> и после этого обновляет статистику трюма.</p>
          <div className="cargo-preview">
            <span style={{ width: `${totals.cargoCap ? Math.min(100, (totals.filled / totals.cargoCap) * 100) : 0}%` }} />
          </div>
        </div>
        <div className="cargo-controls">
          <div className="qty">
            <button type="button" onClick={() => setCargo(Math.max(1, cargo - 50))}>-</button>
            <input type="number" min="1" value={cargo} onChange={(event) => setCargo(Number(event.target.value))} />
            <button type="button" onClick={() => setCargo(cargo + 50)}>+</button>
          </div>
          <button className="btn primary" type="button" disabled={!fleet} onClick={() => onLoadCargo(cargo)}><Boxes size={16} /> Загрузить</button>
        </div>
      </section>

      <div className="toolbar">
        <input placeholder="Поиск по имени корабля..." value={query} onChange={(event) => setQuery(event.target.value)} />
        <div className="seg">
          <button type="button" aria-selected={filter === 'ALL'} onClick={() => setFilter('ALL')}>Все</button>
          {Object.keys(SHIP_TYPES).slice(0, 4).map((type) => <button type="button" aria-selected={filter === type} onClick={() => setFilter(type)} key={type}>{SHIP_TYPES[type].ru}</button>)}
        </div>
        <select className="compact-select" value={sort} onChange={(event) => setSort(event.target.value)}>
          <option value="tier">Тир</option>
          <option value="name">Имя</option>
          <option value="cargo">Трюм</option>
          <option value="speed">Скорость</option>
        </select>
        <select className="compact-select" value={captainId} onChange={(event) => setCaptainId(event.target.value)}>
          {captainOptions.map((item) => <option value={item.id} key={item.id}>{item.login} · {RANKS[item.rank] || item.rank}</option>)}
        </select>
      </div>

      <section className="ships-table">
        <div className="ship-row head">
          <span />
          <span>Название</span>
          <span>Тип</span>
          <span>Капитан</span>
          <span>Экипаж</span>
          <span>Скорость</span>
          <span>Трюм</span>
          <span>Цена</span>
          <span />
        </div>
        {visible.map((ship) => {
          const meta = SHIP_TYPES[ship.shipType] || { ru: ship.shipType, tier: 1, cannons: 0 };
          const captain = pirates.find((item) => item.id === ship.capitanId);
          const mine = ship.ownerId === pirate.id || ship.capitanId === pirate.id || ship.fleetId === fleet?.id;
          return (
            <article className={`ship-row ${mine ? 'owned' : ''}`} key={ship.id}>
              <Silhouette type={ship.shipType} />
              <div className="ship-name-cell"><b>«{ship.name}»</b><small>{ship.ownership} · {COUNTRY_NAMES[ship.builderCountry] || ship.builderCountry}</small></div>
              <div><b>{meta.ru}</b><small>T{meta.tier} · {meta.cannons} пушек</small></div>
              <div><b>{captain?.login || 'не назначен'}</b><small>{captain ? RANKS[captain.rank] || captain.rank : 'PATCH /capitan'}</small></div>
              <b>{ship.maxCrew}</b>
              <b>{ship.maxSpeed}</b>
              <b>{ship.cargoCapacity}</b>
              <b>{fmtMoney(ship.price)}</b>
              <div className="ship-row-actions">
                <button className="icon-button" type="button" title="Назначить выбранного капитана" onClick={() => onAssignCaptain(ship.id, captainId)}><UserCheck size={16} /></button>
                {mine ? <button className="icon-button" type="button" title="Выставить на продажу" onClick={() => onOfferForSale(ship.id)}><BadgeDollarSign size={16} /></button> : null}
              </div>
            </article>
          );
        })}
      </section>
    </>
  );
}

function Silhouette({ type }) {
  return (
    <div className={`silhouette ${type || 'SLOOP'}`} aria-hidden="true">
      <span className="hull" />
      <span className="mast m1" />
      <span className="mast m2" />
      <span className="mast m3" />
      <span className="sail s1" />
      <span className="sail s2" />
      <span className="sail s3" />
    </div>
  );
}
