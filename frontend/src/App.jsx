import { useCallback, useEffect, useMemo, useState } from 'react';
import { api } from './services/api.js';
import { PageShell } from './components/Chrome.jsx';
import { AuthPage } from './pages/AuthPage.jsx';
import { DashboardPage } from './pages/DashboardPage.jsx';
import { FleetPage } from './pages/FleetPage.jsx';
import { IslandsPage } from './pages/IslandsPage.jsx';
import { MarketPage } from './pages/MarketPage.jsx';
import { ShipsPage } from './pages/ShipsPage.jsx';
import { TeamPage } from './pages/TeamPage.jsx';
import { getErrorMessage } from './utils/gameMeta.js';

export default function App() {
  const [session, setSession] = useState(api.session);
  const [pirate, setPirate] = useState(null);
  const [pirates, setPirates] = useState([]);
  const [teams, setTeams] = useState([]);
  const [fleet, setFleet] = useState(null);
  const [fleetStats, setFleetStats] = useState(null);
  const [shipStats, setShipStats] = useState(null);
  const [fleets, setFleets] = useState([]);
  const [islands, setIslands] = useState([]);
  const [ships, setShips] = useState([]);
  const [marketShips, setMarketShips] = useState([]);
  const [activePage, setActivePage] = useState('dashboard');
  const [loading, setLoading] = useState(Boolean(api.session));
  const [toast, setToast] = useState(null);

  const team = useMemo(() => {
    if (!pirate?.teamId) return null;
    return teams.find((item) => item.id === pirate.teamId) || null;
  }, [pirate, teams]);

  const showToast = useCallback((message, tone = 'ok') => {
    setToast({ message, tone });
    window.setTimeout(() => setToast(null), 3200);
  }, []);

  const loadData = useCallback(async () => {
    if (!api.session) return;
    setLoading(true);
    try {
      const currentPirate = await api.me();
      setPirate(currentPirate);
      const [pirateList, teamList, marketShipList, shipList, fleetList, islandList, ownerFleet] = await Promise.all([
        api.pirates.list(),
        api.teams.list(),
        api.market.ships(),
        api.ships.list(),
        api.fleets.list(),
        api.islands.list(),
        currentPirate?.id ? api.fleets.byOwner(currentPirate.id).catch(() => null) : Promise.resolve(null),
      ]);
      setPirates(pirateList || []);
      setTeams(teamList || []);
      setMarketShips(marketShipList || []);
      setShips(shipList || []);
      setFleets(fleetList || []);
      setIslands(islandList || []);
      setFleet(ownerFleet || null);
      if (ownerFleet?.id) {
        const [nextFleetStats, nextShipStats] = await Promise.all([
          api.fleets.stats(ownerFleet.id).catch(() => null),
          api.ships.statsByFleet(ownerFleet.id).catch(() => null),
        ]);
        setFleetStats(nextFleetStats);
        setShipStats(nextShipStats);
      } else {
        setFleetStats(null);
        setShipStats(null);
      }
    } catch (err) {
      showToast(getErrorMessage(err), 'err');
      if (err.status === 401 || /401|Unauthorized/i.test(err.message)) {
        api.logout();
        setSession(null);
      }
    } finally {
      setLoading(false);
    }
  }, [showToast]);

  useEffect(() => {
    loadData();
  }, [loadData, session]);

  async function handleLogin(credentials) {
    const nextSession = await api.login(credentials);
    setSession(nextSession);
    showToast('Добро пожаловать на борт');
  }

  async function handleRegister(payload) {
    await api.register(payload);
    showToast('Пират создан');
  }

  function handleLogout() {
    api.logout();
    setSession(null);
    setPirate(null);
  }

  async function refreshWith(action, successMessage) {
    try {
      const result = await action();
      await loadData();
      showToast(successMessage);
      return result;
    } catch (err) {
      showToast(getErrorMessage(err), 'err');
      throw err;
    }
  }

  if (!session) {
    return <AuthPage onLogin={handleLogin} onRegister={handleRegister} />;
  }

  if (loading && !pirate) {
    return <div className="loading-screen">Поднимаем паруса...</div>;
  }

  return (
    <PageShell pirate={pirate} activePage={activePage} setActivePage={setActivePage} onLogout={handleLogout}>
      {activePage === 'dashboard' && pirate ? (
        <DashboardPage
          pirate={pirate}
          team={team}
          fleet={fleet}
          fleetStats={fleetStats}
          shipStats={shipStats}
          onNavigate={setActivePage}
          onUpgrade={(stat) => refreshWith(() => api.pirates.upgrade(pirate.id, stat, 1), 'Характеристика улучшена')}
          onUpgradeRank={() => refreshWith(() => api.pirates.upgradeRank(pirate.id), 'Ранг повышен')}
        />
      ) : null}

      {activePage === 'team' && pirate ? (
        <TeamPage
          pirate={pirate}
          team={team}
          pirates={pirates}
          onCreateTeam={(name) => refreshWith(() => api.teams.create({ name, capitanId: pirate.id }), 'Команда создана')}
          onRemovePirate={(pirateId) => refreshWith(() => api.teams.removePirates(team.id, [pirateId]), 'Пират изгнан из команды')}
          onCoup={(pirateId) => refreshWith(() => api.teams.coup(team.id, pirateId), 'Мятеж завершён')}
        />
      ) : null}

      {activePage === 'market' && pirate ? (
        <MarketPage
          pirate={pirate}
          fleet={fleet}
          ships={marketShips}
          onBuyShip={(shipId) => refreshWith(() => api.market.buyShip(shipId, pirate.id), 'Корабль куплен')}
          onBuySupply={(kind, quantity) => refreshWith(() => api.market[kind === 'ammo' ? 'buyAmmo' : 'buyProvision'](fleet.id, quantity), 'Снабжение погружено')}
        />
      ) : null}

      {activePage === 'fleet' && pirate ? (
        <FleetPage
          pirate={pirate}
          fleet={fleet}
          fleetStats={fleetStats}
          fleets={fleets}
          islands={islands}
          onCreateFleet={(payload) => refreshWith(() => api.fleets.create(pirate.id, payload), 'Флот готов')}
          onMove={(payload) => refreshWith(() => api.fleets.move(fleet.id, payload), 'Флот сменил курс')}
          onFindTreasure={() => refreshWith(() => api.fleets.findTreasure(fleet.id), 'Сокровища найдены')}
          onAttack={(enemyFleetId) => refreshWith(() => api.fleets.attack(fleet.id, enemyFleetId), 'Бой завершён')}
          onCapture={(islandId) => refreshWith(() => api.fleets.captureIsland(fleet.id, islandId), 'Остров атакован')}
        />
      ) : null}

      {activePage === 'islands' && pirate ? (
        <IslandsPage
          pirate={pirate}
          islands={islands}
          fleet={fleet}
          onAddTax={(islandId, amount) => refreshWith(() => api.islands.addTax(islandId, pirate.id, amount), 'Налог повышен')}
          onWithdrawTax={(islandId, amount) => refreshWith(() => api.islands.withdrawTax(islandId, pirate.id, amount), 'Налог снижен')}
          onUpgrade={(islandId, level) => refreshWith(() => api.islands.upgrade(islandId, pirate.id, level), 'Остров улучшен')}
          onTakeProfit={(islandId) => refreshWith(() => api.islands.takeProfit(islandId, pirate.id), 'Доход забран')}
          onUpgradeMarket={(islandId) => refreshWith(() => api.islands.upgradeMarket(islandId, pirate.id), 'Рынок улучшен')}
        />
      ) : null}

      {activePage === 'ships' && pirate ? (
        <ShipsPage
          pirate={pirate}
          fleet={fleet}
          ships={ships}
          pirates={pirates}
          shipStats={shipStats}
          onCreateShip={(payload) => refreshWith(() => api.ships.create(payload), 'Корабль создан')}
          onAssignCaptain={(shipId, captainId) => refreshWith(() => api.ships.assignCaptain(shipId, captainId), 'Капитан назначен')}
          onLoadCargo={(amount) => refreshWith(() => api.ships.loadCargo(fleet.id, amount), 'Груз распределён')}
          onOfferForSale={(shipId) => refreshWith(() => api.ships.offerForSale(shipId, pirate.id), 'Корабль выставлен на продажу')}
        />
      ) : null}

      {toast ? <div className={`toast ${toast.tone}`}>{toast.message}</div> : null}
    </PageShell>
  );
}
