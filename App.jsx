import { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Bar,
  BarChart,
  CartesianGrid,
  Cell,
  Line,
  LineChart,
  Pie,
  PieChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts';
import { createSale, deleteSale, loadDashboard, updateSale } from '../api.js';

const colors = ['#137c8b', '#c2410c', '#4d7c0f', '#7c3aed', '#be123c'];
const emptyForm = {
  orderId: '',
  orderDate: '',
  city: '',
  product: '',
  category: '',
  quantity: '',
  price: '',
};

function money(value) {
  return Number(value || 0).toLocaleString('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 });
}

export default function App() {
  const [dashboard, setDashboard] = useState(null);
  const [city, setCity] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [form, setForm] = useState(emptyForm);
  const [editingOrderId, setEditingOrderId] = useState(null);
  const [saving, setSaving] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    refreshDashboard();
  }, [city]);

  const cities = useMemo(() => dashboard?.cities?.map((item) => item.label) || [], [dashboard]);

  async function refreshDashboard() {
    try {
      setError('');
      const data = await loadDashboard(city);
      setDashboard(data);
    } catch (err) {
      setError('Could not load sales data. Please start the Spring Boot backend.');
    }
  }

  function logout() {
    localStorage.clear();
    navigate('/login');
  }

  function updateForm(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  function startEdit(sale) {
    setEditingOrderId(sale.orderId);
    setForm({
      orderId: String(sale.orderId),
      orderDate: sale.orderDate,
      city: sale.city,
      product: sale.product,
      category: sale.category,
      quantity: String(sale.quantity),
      price: String(sale.price),
    });
    setSuccess('');
    setError('');
  }

  function resetForm() {
    setForm(emptyForm);
    setEditingOrderId(null);
  }

  function salePayload() {
    return {
      orderId: Number(form.orderId),
      orderDate: form.orderDate,
      city: form.city.trim(),
      product: form.product.trim(),
      category: form.category.trim(),
      quantity: Number(form.quantity),
      price: Number(form.price),
    };
  }

  function validateForm() {
    if (!form.orderId || !form.orderDate || !form.city || !form.product || !form.category || !form.quantity || !form.price) {
      return 'Please fill all sale fields.';
    }
    if (Number(form.orderId) <= 0 || Number(form.quantity) <= 0 || Number(form.price) < 0) {
      return 'Order ID, quantity, and price must be valid positive numbers.';
    }
    return '';
  }

  async function handleSubmit(event) {
    event.preventDefault();
    const validationError = validateForm();
    if (validationError) {
      setError(validationError);
      setSuccess('');
      return;
    }

    setSaving(true);
    setError('');
    setSuccess('');
    try {
      const payload = salePayload();
      if (editingOrderId) {
        await updateSale(editingOrderId, payload);
        setSuccess(`Order #${editingOrderId} updated successfully.`);
      } else {
        await createSale(payload);
        setSuccess(`Order #${payload.orderId} added successfully.`);
      }
      resetForm();
      await refreshDashboard();
    } catch (err) {
      setError('Could not save the sale. Use a unique order ID and check all fields.');
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete(orderId) {
    const confirmed = window.confirm(`Delete order #${orderId}?`);
    if (!confirmed) {
      return;
    }

    setError('');
    setSuccess('');
    try {
      await deleteSale(orderId);
      if (editingOrderId === orderId) {
        resetForm();
      }
      setSuccess(`Order #${orderId} deleted successfully.`);
      await refreshDashboard();
    } catch (err) {
      setError(`Could not delete order #${orderId}.`);
    }
  }

  if (!dashboard && !error) {
    return <main className="loading">Loading sales insights...</main>;
  }

  return (
    <main>
      <header className="topbar">
        <div>
          <p className="eyebrow">Retail Store Sales Analysis</p>
          <h1>Revenue, products, and city performance</h1>
        </div>
        <button className="ghost" onClick={logout}>Logout</button>
      </header>

      {error && <div className="alert">{error}</div>}
      {success && <div className="notice">{success}</div>}

      {dashboard && (
        <>
          <section className="summary-grid">
            <Metric title="Total Revenue" value={money(dashboard.summary.totalRevenue)} />
            <Metric title="Orders" value={dashboard.summary.totalOrders} />
            <Metric title="Top Product" value={dashboard.summary.topRevenueProduct.label} note={money(dashboard.summary.topRevenueProduct.value)} />
            <Metric title="Best City" value={dashboard.summary.topCity.label} note={money(dashboard.summary.topCity.value)} />
          </section>

          <section className="toolbar">
            <label>
              Filter by city
              <select value={city} onChange={(event) => setCity(event.target.value)}>
                <option value="">All cities</option>
                {cities.map((name) => <option key={name} value={name}>{name}</option>)}
              </select>
            </label>
            <span>Highest order: #{dashboard.summary.highestValueOrder.label} ({money(dashboard.summary.highestValueOrder.value)})</span>
          </section>

          <section className="sale-editor">
            <div>
              <p className="eyebrow">{editingOrderId ? 'Edit Sale' : 'Add Sale'}</p>
              <h2>{editingOrderId ? `Update order #${editingOrderId}` : 'Record a new customer purchase'}</h2>
            </div>
            <form onSubmit={handleSubmit} className="sale-form">
              <label>
                Order ID
                <input
                  type="number"
                  min="1"
                  value={form.orderId}
                  disabled={Boolean(editingOrderId)}
                  onChange={(event) => updateForm('orderId', event.target.value)}
                />
              </label>
              <label>
                Order Date
                <input type="date" value={form.orderDate} onChange={(event) => updateForm('orderDate', event.target.value)} />
              </label>
              <label>
                City
                <input value={form.city} placeholder="Pune" onChange={(event) => updateForm('city', event.target.value)} />
              </label>
              <label>
                Product
                <input value={form.product} placeholder="Laptop" onChange={(event) => updateForm('product', event.target.value)} />
              </label>
              <label>
                Category
                <input value={form.category} placeholder="Electronics" onChange={(event) => updateForm('category', event.target.value)} />
              </label>
              <label>
                Quantity
                <input type="number" min="1" value={form.quantity} onChange={(event) => updateForm('quantity', event.target.value)} />
              </label>
              <label>
                Price
                <input type="number" min="0" step="0.01" value={form.price} onChange={(event) => updateForm('price', event.target.value)} />
              </label>
              <div className="form-actions">
                <button disabled={saving}>{saving ? 'Saving...' : editingOrderId ? 'Update Sale' : 'Add Sale'}</button>
                {editingOrderId && <button type="button" className="ghost" onClick={resetForm}>Cancel</button>}
              </div>
            </form>
          </section>

          <section className="chart-grid">
            <ChartPanel title="Product vs Total Sales">
              <ResponsiveContainer width="100%" height={280}>
                <BarChart data={dashboard.products}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="label" />
                  <YAxis />
                  <Tooltip formatter={(value) => money(value)} />
                  <Bar dataKey="value" fill="#137c8b" radius={[6, 6, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </ChartPanel>

            <ChartPanel title="Category Sales Distribution">
              <ResponsiveContainer width="100%" height={280}>
                <PieChart>
                  <Pie data={dashboard.categories} dataKey="value" nameKey="label" outerRadius={95} label>
                    {dashboard.categories.map((entry, index) => <Cell key={entry.label} fill={colors[index % colors.length]} />)}
                  </Pie>
                  <Tooltip formatter={(value) => money(value)} />
                </PieChart>
              </ResponsiveContainer>
            </ChartPanel>

            <ChartPanel title="Sales by Date">
              <ResponsiveContainer width="100%" height={280}>
                <LineChart data={dashboard.dates}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="label" />
                  <YAxis />
                  <Tooltip formatter={(value) => money(value)} />
                  <Line type="monotone" dataKey="value" stroke="#c2410c" strokeWidth={3} dot={{ r: 4 }} />
                </LineChart>
              </ResponsiveContainer>
            </ChartPanel>
          </section>

          <section className="table-section">
            <h2>Sales Records</h2>
            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Order</th>
                    <th>Date</th>
                    <th>City</th>
                    <th>Product</th>
                    <th>Category</th>
                    <th>Qty</th>
                    <th>Price</th>
                    <th>Total Sales</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {dashboard.sales.map((sale) => (
                    <tr key={sale.orderId}>
                      <td>{sale.orderId}</td>
                      <td>{sale.orderDate}</td>
                      <td>{sale.city}</td>
                      <td>{sale.product}</td>
                      <td>{sale.category}</td>
                      <td>{sale.quantity}</td>
                      <td>{money(sale.price)}</td>
                      <td>{money(sale.totalSales)}</td>
                      <td>
                        <div className="row-actions">
                          <button type="button" className="small-button" onClick={() => startEdit(sale)}>Edit</button>
                          <button type="button" className="small-button danger" onClick={() => handleDelete(sale.orderId)}>Delete</button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>
        </>
      )}
    </main>
  );
}

function Metric({ title, value, note }) {
  return (
    <article className="metric">
      <p>{title}</p>
      <strong>{value}</strong>
      {note && <span>{note}</span>}
    </article>
  );
}

function ChartPanel({ title, children }) {
  return (
    <article className="chart-panel">
      <h2>{title}</h2>
      {children}
    </article>
  );
}
