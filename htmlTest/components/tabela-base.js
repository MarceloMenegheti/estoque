import { apiFetch } from '../api/api.js'; // função genérica de fetch

/**
 * Função genérica para carregar uma tabela
 * @param {string} endpoint - API do backend (ex: '/produtos')
 * @param {Array} colunas - array de objetos {campo, titulo}
 * @param {HTMLElement} container - div onde a tabela será renderizada
 * @param {string} tipo - nome do recurso para ações (ex: 'produtos')
 */
export async function carregarTabelaBase(endpoint, colunas, container, tipo) {
  try {
    // 1️⃣ Buscar dados do backend
    const dados = await apiFetch(endpoint);

    // 2️⃣ Montar cabeçalho
    const theadHTML = colunas.map(c => `<th>${c.titulo}</th>`).join('') + '<th>Ações</th>';
    container.querySelector('#tabela-head').innerHTML = theadHTML;

    // 3️⃣ Montar corpo da tabela
    const tbodyHTML = dados.map(item => {
      const cells = colunas.map(c => `<td>${item[c.campo] ?? ''}</td>`).join('');
      return `
        <tr>
          ${cells}
          <td>
            <button class="editar" data-id="${item.id}" data-tipo="${tipo}">Editar</button>
            <button class="excluir" data-id="${item.id}" data-tipo="${tipo}">Excluir</button>
          </td>
        </tr>
      `;
    }).join('');

    container.querySelector('#tabela-body').innerHTML = tbodyHTML;

    // 4️⃣ Adicionar eventos de exclusão
    container.querySelectorAll('.excluir').forEach(btn => {
      btn.addEventListener('click', async e => {
        const id = e.target.dataset.id;
        if (confirm('Deseja realmente excluir?')) {
          await apiFetch(`/${tipo}/${id}`, 'DELETE');
          // Recarrega tabela após exclusão
          carregarTabelaBase(endpoint, colunas, container, tipo);
        }
      });
    });

    // 5️⃣ Adicionar eventos de edição (chama modal genérico)
    container.querySelectorAll('.editar').forEach(btn => {
      btn.addEventListener('click', e => {
        const id = e.target.dataset.id;
        const item = dados.find(p => p.id == id);

        // Dispatch de evento customizado para modal-base.js
        const event = new CustomEvent('editarRegistro', {
          detail: { tipo, dados: item }
        });
        document.dispatchEvent(event);
      });
    });

  } catch (err) {
    console.error('Erro ao carregar tabela:', err);
    container.innerHTML = `<p>Erro ao carregar dados.</p>`;
  }
}
