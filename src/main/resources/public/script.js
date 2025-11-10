// Conversor de Moedas - Lógica Frontend
// Funções: carregar moedas, validar entrada, chamar API e exibir resultado.

// Detecta ambiente (localhost ou produção)
const API_BASE_URL = window.location.hostname === 'localhost'
  ? 'http://localhost:7000'
  : window.location.origin;

// Estado simples
let currencies = [];
let isFirstRequest = true;

// Referências DOM
const form = document.getElementById('converterForm');
const amountInput = document.getElementById('amount');
const fromCurrencySelect = document.getElementById('fromCurrency');
const toCurrencySelect = document.getElementById('toCurrency');
const swapBtn = document.getElementById('swapBtn');
const convertBtn = document.getElementById('convertBtn');
const resultDiv = document.getElementById('result');
const loadingDiv = document.getElementById('loading');
const errorDiv = document.getElementById('error');
const resultAmount = document.getElementById('resultAmount');
const resultRate = document.getElementById('resultRate');
const resultDate = document.getElementById('resultDate');
const errorMessage = document.getElementById('errorMessage');
const coldStartWarning = document.querySelector('.cold-start-warning');

/** Inicializa a aplicação: carrega moedas e registra eventos. */
async function init() {
  try {
    await loadCurrencies();
    setupEventListeners();
    // Valores padrão iniciais
    fromCurrencySelect.value = 'USD';
    toCurrencySelect.value = 'BRL';
    amountInput.value = '100';
  } catch (error) {
    showError('Erro ao carregar moedas: ' + error.message);
  }
}

/** Carrega lista de moedas da API e preenche selects. */
async function loadCurrencies() {
  const response = await fetch(`${API_BASE_URL}/api/currencies`);
  if (!response.ok) throw new Error('Falha ao carregar moedas');

  const data = await response.json();
  currencies = data.currencies;

  // Preenche os dois selects
  currencies.forEach(currency => {
    const label = `${currency.code} - ${currency.description}`;
    fromCurrencySelect.add(new Option(label, currency.code));
    toCurrencySelect.add(new Option(label, currency.code));
  });
}

/** Registra listeners do formulário e formata entrada do valor. */
function setupEventListeners() {
  form.addEventListener('submit', handleConvert);
  swapBtn.addEventListener('click', swapCurrencies);

  // Permite apenas números, ponto ou vírgula
  amountInput.addEventListener('input', e => {
    e.target.value = e.target.value.replace(/[^0-9.,]/g, '');
  });
}

/** Processa conversão chamando o endpoint /api/convert. */
async function handleConvert(e) {
  e.preventDefault();

  const amount = amountInput.value.replace(',', '.');
  const from = fromCurrencySelect.value;
  const to = toCurrencySelect.value;

  if (!amount || !from || !to) {
    showError('Preencha todos os campos');
    return;
  }
  if (from === to) {
    showError('Selecione moedas diferentes');
    return;
  }

  hideAll();
  showLoading();

  try {
    const response = await fetch(`${API_BASE_URL}/api/convert?from=${from}&to=${to}&amount=${amount}`);
    if (!response.ok) {
      const err = await response.json();
      throw new Error(err.message || 'Erro na conversão');
    }
    const data = await response.json();
    showResult(data);
    isFirstRequest = false;
  } catch (error) {
    showError(error.message);
  }
}

/** Troca rapidamente as moedas dos selects. */
function swapCurrencies() {
  const temp = fromCurrencySelect.value;
  fromCurrencySelect.value = toCurrencySelect.value;
  toCurrencySelect.value = temp;
}

/** Exibe resultado formatado de uma conversão. */
function showResult(data) {
  hideAll();
  resultAmount.textContent = `${data.amount.toFixed(2)} ${data.from} = ${data.result.toFixed(2)} ${data.to}`;
  resultRate.textContent = `1 ${data.from} = ${data.rate.toFixed(4)} ${data.to}`;
  resultDate.textContent = new Date(data.timestamp).toLocaleString('pt-BR');
  resultDiv.classList.remove('hidden');
}

/** Mostra indicador de carregamento (com aviso na 1ª chamada). */
function showLoading() {
  loadingDiv.classList.remove('hidden');
  convertBtn.disabled = true;
  if (isFirstRequest) {
    coldStartWarning.classList.remove('hidden');
  }
}

/** Exibe mensagem de erro amigável. */
function showError(message) {
  hideAll();
  errorMessage.textContent = message;
  errorDiv.classList.remove('hidden');
  convertBtn.disabled = false;
}

/** Esconde todas as áreas (resultado, erro, loading). */
function hideAll() {
  resultDiv.classList.add('hidden');
  loadingDiv.classList.add('hidden');
  errorDiv.classList.add('hidden');
  coldStartWarning.classList.add('hidden');
  convertBtn.disabled = false;
}

// Inicia quando o DOM está pronto
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', init);
} else {
  init();
}

