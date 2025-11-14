import { apiFetch } from '../api/api.js';

/**
 * Carrega o modal genérico com campos dinâmicos
 * @param {HTMLElement} container - div do modal
 * @param {Array} campos - array de objetos {nome, label, tipo='text', options=[]}
 * @param {string} endpoint - API para criar ou atualizar
 * @param {object} registro - registro para edição (opcional)
 */
export function carregarModalBase(container, campos, endpoint, registro = null) {
  // Mostrar overlay
  const overlay = document.getElementById('modal-overlay');
  overlay.style.display = 'flex';

  const modalBody = document.getElementById('modal-body');
  const modalTitle = document.getElementById('modal-title');
  modalTitle.textContent = registro ? 'Editar Registro' : 'Novo Registro';

  // Limpar modal
  modalBody.innerHTML = '';

  // Criar campos
  campos.forEach(c => {
    const label = document.createElement('label');
    label.textContent = c.label;

    let input;
    if (c.tipo === 'select') {
      input = document.createElement('select');
      c.options.forEach(opt => {
        const option = document.createElement('option');
        option.value = opt.value;
        option.textContent = opt.label;
        input.appendChild(option);
      });
    } else if (c.tipo === 'textarea') {
      input = document.createElement('textarea');
    } else {
      input = document.createElement('input');
      input.type = c.tipo || 'text';
    }

    input.name = c.nome;
    input.value = registro ? registro[c.nome] ?? '' : '';

    modalBody.appendChild(label);
    modalBody.appendChild(input);
  });

  // Fechar modal
  const btnClose = document.getElementById('modal-close');
  const btnCancel = document.getElementById('modal-cancel');

  btnClose.onclick = btnCancel.onclick = () => {
    overlay.style.display = 'none';
  };

  // Salvar dados
  const btnSave = document.getElementById('modal-save');
  btnSave.onclick = async () => {
    const data = {};
    campos.forEach(c => {
      const el = modalBody.querySelector(`[name="${c.nome}"]`);
      data[c.nome] = el.value;
    });

    try {
      if (registro && registro.id) {
        await apiFetch(`${endpoint}/${registro.id}`, 'PUT', data);
      } else {
        await apiFetch(endpoint, 'POST', data);
      }
      overlay.style.display = 'none';
      // Dispara evento para atualizar tabela
      document.dispatchEvent(new CustomEvent('atualizarTabela'));
    } catch (err) {
      alert('Erro ao salvar: ' + err.message);
      console.error(err);
    }
  };
}
