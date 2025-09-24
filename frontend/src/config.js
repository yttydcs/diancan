// 前端全局配置与工具方法

// 允许三种方式配置后端基础地址：
// 1) .env.* 中的 VITE_BACKEND_BASE_URL
// 2) window.__BACKEND_BASE_URL__（可在 index.html 里动态注入）
// 3) 回退默认 http://localhost:8080
export const BACKEND_BASE_URL =
  import.meta?.env?.VITE_BACKEND_BASE_URL ||
  (typeof window !== 'undefined' && window.__BACKEND_BASE_URL__) ||
  'http://localhost:8080';

function isAbsoluteUrl(url) {
  return /^(?:[a-z]+:)?\/\//i.test(url);
}

function joinUrl(base, path) {
  if (!path) return '';
  if (isAbsoluteUrl(path)) return path;
  const baseTrim = String(base || '').replace(/\/+$/, '');
  const pathTrim = String(path || '').replace(/^\/+/, '');
  // 若 path 以 / 开头，前面已去掉一个 /，拼接时加回去
  return `${baseTrim}/${pathTrim}`;
}

// 将后端返回的图片路径（可能是 /images/xxx 或 images/xxx 或完整 http 链接）
// 统一转换为可访问的完整 URL
export function toImageUrl(path) {
  if (!path) return '';
  if (isAbsoluteUrl(path)) return path; // 已是完整地址
  // 兼容以 /images 开头或不带斜杠的情况
  return joinUrl(BACKEND_BASE_URL, path);
}

