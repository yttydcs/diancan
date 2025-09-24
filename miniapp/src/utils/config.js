// 全局配置与工具方法（uni-app）

// 后端基础地址：
// 1) 优先用 .env.* 中的 VITE_BACKEND_BASE_URL
// 2) 回退到本地开发后端 http://localhost:8080
export const BACKEND_BASE_URL =
  (typeof import.meta !== 'undefined' && import.meta.env && import.meta.env.VITE_BACKEND_BASE_URL) ||
  'http://localhost:8080';

// H5 开发环境是否启用 Vite 代理（默认启用，可通过 VITE_USE_PROXY=false 关闭）
const IS_DEV = typeof import.meta !== 'undefined' && !!import.meta.env && !!import.meta.env.DEV;
const USE_PROXY = !import.meta?.env?.VITE_USE_PROXY || import.meta.env.VITE_USE_PROXY !== 'false';

function isAbsoluteUrl(url) {
  return /^(?:[a-z]+:)?\/\//i.test(url);
}

function joinUrl(base, path) {
  if (!path) return '';
  if (isAbsoluteUrl(path)) return path;
  const baseTrim = String(base || '').replace(/\/+$/, '');
  const pathTrim = String(path || '').replace(/^\/+/, '');
  return `${baseTrim}/${pathTrim}`;
}

// 将后端返回的图片路径（/images/xxx 或 images/xxx 或完整 http 链接）
// 统一转换为可访问的 URL：
// - 开发(H5)且启用代理：返回相对路径，以便经由 Vite 代理
// - 其他环境：拼成完整后端地址
export function toImageUrl(path) {
  if (!path) return '';
  if (isAbsoluteUrl(path)) return path;
  const p = String(path).startsWith('/') ? path : `/${path}`;
  if (IS_DEV && USE_PROXY && p.startsWith('/images/')) {
    return p; // 使用代理
  }
  return joinUrl(BACKEND_BASE_URL, p);
}

// 拼接 API 地址：
// - 开发(H5)且启用代理：对于 /api 前缀直接返回相对路径
// - 其他环境：拼成完整后端地址
export function apiUrl(path) {
  if (!path) return '';
  if (isAbsoluteUrl(path)) return path;
  const p = String(path).startsWith('/') ? path : `/${path}`;
  if (IS_DEV && USE_PROXY && p.startsWith('/api')) {
    return p; // 使用代理
  }
  return joinUrl(BACKEND_BASE_URL, p);
}
