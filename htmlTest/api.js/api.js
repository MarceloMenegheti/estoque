const API_BASE = "http://localhost:8081/api";

export async function apiFetch(endpoint, method = 'GET', body = null) {
  const options = {
    method,
    headers: { 'Content-Type': 'application/json' },
  };

  if (body) options.body = JSON.stringify(body);

  const response = await fetch(`${API_BASE}${endpoint}`, options);
  if (!response.ok) {
    const msg = await response.text();
    throw new Error(`Erro ${response.status}: ${msg}`);
  }

  return response.json();
}