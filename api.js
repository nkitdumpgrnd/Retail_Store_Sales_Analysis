import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export async function login(username, password) {
  const response = await api.post('/auth/login', { username, password });
  return response.data;
}

export async function loadDashboard(city = '') {
  const cityQuery = city ? `?city=${encodeURIComponent(city)}` : '';
  const [summary, sales, products, categories, dates, cities] = await Promise.all([
    api.get('/analytics/summary'),
    api.get(`/sales${cityQuery}`),
    api.get('/analytics/products/revenue'),
    api.get('/analytics/categories/revenue'),
    api.get('/analytics/dates/revenue'),
    api.get('/analytics/cities/revenue'),
  ]);
  return {
    summary: summary.data,
    sales: sales.data,
    products: products.data,
    categories: categories.data,
    dates: dates.data,
    cities: cities.data,
  };
}

export async function createSale(sale) {
  const response = await api.post('/sales', sale);
  return response.data;
}

export async function updateSale(orderId, sale) {
  const response = await api.put(`/sales/${orderId}`, sale);
  return response.data;
}

export async function deleteSale(orderId) {
  await api.delete(`/sales/${orderId}`);
}

export default api;
