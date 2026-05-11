import { decodeJwt } from '../utils/gameMeta.js';

const STORAGE_KEY = 'pirateTeam.auth';

let session = readSession();

function readSession() {
  try {
    return JSON.parse(localStorage.getItem(STORAGE_KEY) || 'null');
  } catch {
    return null;
  }
}

function saveSession(nextSession) {
  session = nextSession;
  if (nextSession) localStorage.setItem(STORAGE_KEY, JSON.stringify(nextSession));
  else localStorage.removeItem(STORAGE_KEY);
}

async function request(path, options = {}) {
  const headers = new Headers(options.headers);
  const hasBody = options.body !== undefined && options.body !== null;

  if (hasBody && !headers.has('Content-Type')) headers.set('Content-Type', 'application/json');
  if (session?.accessToken && !headers.has('Authorization')) {
    headers.set('Authorization', `Bearer ${session.accessToken}`);
  }

  const response = await fetch(path, {
    ...options,
    headers,
    body: hasBody && typeof options.body !== 'string' ? JSON.stringify(options.body) : options.body,
  });

  if (response.status === 204) return null;

  const text = await response.text();
  const data = text ? JSON.parse(text) : null;

  if (!response.ok) {
    const message = data?.message || data?.error || `Ошибка сервера ${response.status}`;
    const error = new Error(message);
    error.status = response.status;
    throw error;
  }

  return data;
}

export const api = {
  get session() {
    return session;
  },
  setSession: saveSession,
  logout() {
    saveSession(null);
  },
  async login(credentials) {
    const tokens = await request('/auth/login', { method: 'POST', body: credentials });
    const claims = decodeJwt(tokens.accessToken);
    const nextSession = { ...tokens, pirateId: claims?.pirateId, login: claims?.sub || credentials.login };
    saveSession(nextSession);
    return nextSession;
  },
  async register(payload) {
    return request('/api/pirates', { method: 'POST', body: payload });
  },
  async me() {
    if (!session?.pirateId) return null;
    return request(`/api/pirates/${session.pirateId}`);
  },
  pirates: {
    list: () => request('/api/pirates'),
    update: (id, payload) => request(`/api/pirates/${id}`, { method: 'PATCH', body: payload }),
    upgrade: (id, stat, amount = 1) => request(`/api/pirates/${id}/upgrade/${stat}?amount=${amount}`, { method: 'PATCH' }),
    upgradeRank: (id) => request(`/api/pirates/${id}/upgrade/rank`, { method: 'PATCH' }),
  },
  teams: {
    list: () => request('/api/teams'),
    get: (id) => request(`/api/teams/${id}`),
    create: (payload) => request('/api/teams', { method: 'POST', body: payload }),
    addPirates: (id, pirateIds) => request(`/api/teams/${id}/pirates/add`, { method: 'PATCH', body: { pirates: pirateIds } }),
    removePirates: (id, pirateIds) => request(`/api/teams/${id}/pirates/remove`, { method: 'PATCH', body: { pirates: pirateIds } }),
    coup: (id, rebelId) => request(`/api/teams/${id}/coup/${rebelId}`, { method: 'POST' }),
  },
  fleets: {
    list: () => request('/api/fleets'),
    get: (id) => request(`/api/fleets/${id}`),
    byOwner: (ownerId) => request(`/api/fleets/by-owner/${ownerId}`),
    stats: (id) => request(`/api/fleets/${id}/stats`),
    create: (ownerId, payload) => request(`/api/fleets/owners/${ownerId}/ensure`, { method: 'POST', body: payload }),
    move: (id, payload) => request(`/api/fleets/${id}/move`, { method: 'POST', body: payload }),
    attack: (id, enemyId) => request(`/api/fleets/${id}/attack/${enemyId}`, { method: 'POST' }),
    findTreasure: (id) => request(`/api/fleets/${id}/treasure/find`, { method: 'POST' }),
    captureIsland: (id, islandId) => request(`/api/fleets/${id}/capture/${islandId}`, { method: 'POST' }),
  },
  islands: {
    list: () => request('/api/islands'),
    create: (payload) => request('/api/islands', { method: 'POST', body: payload }),
    addTax: (id, ownerId, amount) => request(`/api/islands/${id}/tax/add/${ownerId}`, { method: 'PATCH', body: { amount } }),
    withdrawTax: (id, ownerId, amount) => request(`/api/islands/${id}/tax/withdraw/${ownerId}`, { method: 'PATCH', body: { amount } }),
    upgrade: (id, ownerId, level) => request(`/api/islands/${id}/upgrade/${ownerId}?level=${level}`, { method: 'PATCH' }),
    takeProfit: (id, ownerId) => request(`/api/islands/${id}/profit/${ownerId}`, { method: 'POST' }),
    upgradeMarket: (id, ownerId) => request(`/api/islands/${id}/market/upgrade/${ownerId}`, { method: 'PATCH' }),
  },
  ships: {
    list: () => request('/api/ships'),
    create: (payload) => request('/api/ships', { method: 'POST', body: payload }),
    assignCaptain: (shipId, captainId) => request(`/api/ships/${shipId}/capitan/${captainId}`, { method: 'PATCH' }),
    loadCargo: (fleetId, amount) => request(`/api/ships/by-fleet/${fleetId}/load-cargo?amount=${amount}`, { method: 'PATCH' }),
    statsByFleet: (fleetId) => request(`/api/ships/by-fleet/${fleetId}/stats`),
    offerForSale: (shipId, captainId) => request(`/api/market/ships/${shipId}/sale/${captainId}`, { method: 'PATCH' }),
  },
  market: {
    ships: () => request('/api/market/ships'),
    buyShip: (shipId, captainId) => request(`/api/market/ships/${shipId}/buy/${captainId}`, { method: 'PATCH' }),
    buyAmmo: (fleetId, quantity) => request(`/api/market/fleets/${fleetId}/ammo/buy?quantity=${quantity}`, { method: 'POST' }),
    buyProvision: (fleetId, quantity) => request(`/api/market/fleets/${fleetId}/provision/buy?quantity=${quantity}`, { method: 'POST' }),
  },
};
