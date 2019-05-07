export const REQUEST_PREFIX = process.buildEnv.apiPrefix || '/api';
export const DATETIME_FORMATE = 'YYYY-MM-DD HH:mm:ss';
export const DATE_FORMATE = 'YYYY-MM-DD';
export const isDev = process.buildEnv.env === 'dev';
export const DEV_IMG_PATH = 'http://127.0.0.1';
export const PROD_IMG_PATH= 'https://127.0.0.1';
export const IMG_PATH = isDev ? DEV_IMG_PATH : PROD_IMG_PATH;
