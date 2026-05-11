export const COUNTRIES = [
  { code: 'ENGLAND', name: 'Англия', shortCode: 'GB' },
  { code: 'SPAIN', name: 'Испания', shortCode: 'ES' },
  { code: 'FRANCE', name: 'Франция', shortCode: 'FR' },
  { code: 'NETHERLANDS', name: 'Нидерланды', shortCode: 'NL' },
  { code: 'PORTUGAL', name: 'Португалия', shortCode: 'PT' },
  { code: 'USA', name: 'США', shortCode: 'US' },
  { code: 'BAHAMAS', name: 'Багамы', shortCode: 'BS' },
  { code: 'HAITI', name: 'Гаити', shortCode: 'HT' },
  { code: 'MOROCCO', name: 'Марокко', shortCode: 'MA' },
  { code: 'SOMALIA', name: 'Сомали', shortCode: 'SO' },
  { code: 'OTTOMAN_EMPIRE', name: 'Османская империя', shortCode: 'OM' },
  { code: 'ARABIC', name: 'Аравия', shortCode: 'AR' },
];

export const COUNTRY_NAMES = Object.fromEntries(COUNTRIES.map((country) => [country.code, country.name]));

export const RANKS = {
  CABIN_BOY: 'Юнга',
  SAILOR: 'Матрос',
  LOOKOUT: 'Дозорный',
  BOARDER: 'Абордажник',
  COOK: 'Кок',
  TREASURER: 'Казначей',
  SHIP_SURGEON: 'Лекарь',
  GUNNER: 'Канонир',
  BOATSWAIN: 'Боцман',
  NAVIGATOR: 'Штурман',
  QUARTERMASTER: 'Квартирмейстер',
  CAPTAIN: 'Капитан',
  LEGEND: 'Легенда',
  PIRATE_KING: 'Король пиратов',
};

export const RANK_ORDER = Object.keys(RANKS);

export const ISLAND_LEVELS = {
  WILD_SHORE: 'Дикий берег',
  BAY: 'Бухта',
  FISHING_VILLAGE: 'Рыбацкая деревня',
  TRADE_POST: 'Торговый пост',
  HARBOR: 'Гавань',
  FREE_PORT: 'Свободный порт',
  FORTRESS_ISLAND: 'Остров-крепость',
  SMUGGLER_DEN: 'Логово контрабандистов',
  PIRATE_BAY: 'Пиратская бухта',
  PROSPEROUS_CITY: 'Процветающий город',
};

export const LOCATION_NAMES = {
  CARIBBEAN_SEA: 'Карибское море',
  MEDITERRANEAN_SEA: 'Средиземное море',
  BLACK_SEA: 'Чёрное море',
  ATLANTIC_OCEAN: 'Атлантический океан',
  PACIFIC_OCEAN: 'Тихий океан',
  INDIAN_OCEAN: 'Индийский океан',
  NORTH_SEA: 'Северное море',
  BALTIC_SEA: 'Балтийское море',
  RED_SEA: 'Красное море',
  ARABIAN_SEA: 'Аравийское море',
  SOUTH_CHINA_SEA: 'Южно-Китайское море',
  GULF_OF_MEXICO: 'Мексиканский залив',
  STRAIT_OF_GIBRALTAR: 'Гибралтарский пролив',
  ENGLISH_CHANNEL: 'Ла-Манш',
  STRAIT_OF_MALACCA: 'Малаккский пролив',
};

export const SHIP_TYPES = {
  SLOOP: { ru: 'Шлюп', tier: 1, cannons: 2, desc: 'Лёгкий разведчик для быстрых рейдов' },
  BRIGANTINE: { ru: 'Бригантина', tier: 2, cannons: 4, desc: 'Средний корабль для торговли и боя' },
  SCHOONER: { ru: 'Шхуна', tier: 3, cannons: 5, desc: 'Манёвренный корабль с хорошей скоростью' },
  BARQUE: { ru: 'Барк', tier: 4, cannons: 10, desc: 'Большой трюм и уверенная дальность' },
  FRIGATE: { ru: 'Фрегат', tier: 4, cannons: 12, desc: 'Боевой корабль для серьёзных стычек' },
  GALLEON: { ru: 'Галеон', tier: 5, cannons: 20, desc: 'Огневая мощь и вместимость' },
  MAN_OF_WAR: { ru: 'Линейный', tier: 7, cannons: 35, desc: 'Флагманский корабль максимальной силы' },
};

export const fmtMoney = (value = 0) => `${Number(value || 0).toLocaleString('ru-RU')} ₽`;

export function decodeJwt(token) {
  if (!token) return null;
  try {
    const payload = token.split('.')[1];
    const normalized = payload.replace(/-/g, '+').replace(/_/g, '/');
    return JSON.parse(window.atob(normalized));
  } catch {
    return null;
  }
}

export function getErrorMessage(error) {
  if (error?.message) return error.message;
  return 'Не удалось связаться с сервером';
}
