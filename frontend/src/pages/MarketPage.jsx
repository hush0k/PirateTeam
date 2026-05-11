import { useMemo, useState } from 'react';
import { PackagePlus, Sailboat, Search, ShoppingCart } from 'lucide-react';
import { EmptyState } from '../components/Chrome.jsx';
import { fmtMoney, SHIP_TYPES } from '../utils/gameMeta.js';

export function MarketPage({ pirate, fleet, ships, onBuyShip, onBuySupply }) {
  const [query, setQuery] = useState('');
  const [sort, setSort] = useState('tier');
  const [selected, setSelected] = useState(null);
  const [ammo, setAmmo] = useState(10);
  const [provision, setProvision] = useState(10);

  const visibleShips = useMemo(() => {
    return [...ships]
      .filter((ship) => !query || `${ship.name} ${ship.shipType}`.toLowerCase().includes(query.toLowerCase()))
      .sort((a, b) => {
        if (sort === 'price') return Number(a.price || 0) - Number(b.price || 0);
        if (sort === 'speed') return Number(b.maxSpeed || 0) - Number(a.maxSpeed || 0);
        return (SHIP_TYPES[b.shipType]?.tier || 0) - (SHIP_TYPES[a.shipType]?.tier || 0);
      });
  }, [query, ships, sort]);

  return (
    <>
      <div className="crumbs"><span>порт</span><span>/</span><b>маркет</b></div>
      <div className="page-h">
        <div>
          <h1>ПИРАТСКИЙ <span>МАРКЕТ</span></h1>
          <p>Корабли приходят из `/api/market/ships`, покупки идут через market endpoints.</p>
        </div>
        <div className="wallet">Казна: <b>{fmtMoney(pirate.treasury)}</b></div>
      </div>

      <div className="toolbar">
        <div className="search-field"><Search size={16} /><input placeholder="Найти корабль..." value={query} onChange={(event) => setQuery(event.target.value)} /></div>
        <div className="seg">
          <button type="button" aria-selected={sort === 'tier'} onClick={() => setSort('tier')}>Тир</button>
          <button type="button" aria-selected={sort === 'price'} onClick={() => setSort('price')}>Цена</button>
          <button type="button" aria-selected={sort === 'speed'} onClick={() => setSort('speed')}>Скорость</button>
        </div>
      </div>

      {visibleShips.length ? (
        <section className="ships-grid">
          {visibleShips.map((ship) => {
            const meta = SHIP_TYPES[ship.shipType] || { ru: ship.shipType, tier: 1, cannons: 0 };
            const canBuy = Number(ship.price || 0) <= Number(pirate.treasury || 0);
            return (
              <article className={`ship-card ${!canBuy ? 'locked' : ''}`} key={ship.id} onClick={() => setSelected(ship)}>
                <div className="ship-illu">
                  <Sailboat size={58} />
                  <span>T{meta.tier}</span>
                </div>
                <div className="ship-body">
                  <h3>«{ship.name}»</h3>
                  <p>{meta.ru} · {ship.builderCountry}</p>
                  <div className="spec-grid">
                    <div><small>Пушки</small><b>{meta.cannons}</b></div>
                    <div><small>Команда</small><b>{ship.maxCrew}</b></div>
                    <div><small>Скорость</small><b>{ship.maxSpeed}</b></div>
                    <div><small>Трюм</small><b>{ship.cargoCapacity}</b></div>
                  </div>
                </div>
                <div className="ship-foot">
                  <b>{fmtMoney(ship.price)}</b>
                  <button className="btn primary sm" type="button" disabled={!canBuy} onClick={(event) => { event.stopPropagation(); onBuyShip(ship.id); }}>
                    <ShoppingCart size={15} /> Купить
                  </button>
                </div>
              </article>
            );
          })}
        </section>
      ) : (
        <EmptyState title="Кораблей в продаже нет">Создай корабли через `/api/ships`, и они появятся здесь после обновления.</EmptyState>
      )}

      <section className="supplies">
        <Supply title="Боеприпасы" value={ammo} setValue={setAmmo} disabled={!fleet} onBuy={() => onBuySupply('ammo', ammo)} />
        <Supply title="Провизия" value={provision} setValue={setProvision} disabled={!fleet} onBuy={() => onBuySupply('provision', provision)} />
      </section>

      {selected ? (
        <div className="drawer-bg" onClick={() => setSelected(null)}>
          <aside className="drawer" onClick={(event) => event.stopPropagation()}>
            <button className="drawer-close" type="button" onClick={() => setSelected(null)}>Закрыть</button>
            <div className="ship-hero"><Sailboat size={92} /></div>
            <h2>«{selected.name}»</h2>
            <p>{SHIP_TYPES[selected.shipType]?.desc || selected.shipType}</p>
            <div className="spec-grid">
              <div><small>Тип</small><b>{SHIP_TYPES[selected.shipType]?.ru || selected.shipType}</b></div>
              <div><small>Цена</small><b>{fmtMoney(selected.price)}</b></div>
              <div><small>Построен</small><b>{selected.constructionDate}</b></div>
              <div><small>Владелец</small><b>{selected.ownerId || 'рынок'}</b></div>
            </div>
            <button className="btn primary" type="button" onClick={() => { onBuyShip(selected.id); setSelected(null); }}>Купить корабль</button>
          </aside>
        </div>
      ) : null}
    </>
  );
}

function Supply({ title, value, setValue, disabled, onBuy }) {
  return (
    <article className="supply-card">
      <div className="supply-head"><PackagePlus size={24} /><h3>{title}</h3></div>
      <div className="qty">
        <button type="button" onClick={() => setValue(Math.max(1, value - 5))}>-</button>
        <input type="number" min="1" value={value} onChange={(event) => setValue(Number(event.target.value))} />
        <button type="button" onClick={() => setValue(value + 5)}>+</button>
      </div>
      <button className="btn ghost" type="button" disabled={disabled} onClick={onBuy}>
        {disabled ? 'Нужен флот' : 'Купить'}
      </button>
    </article>
  );
}
