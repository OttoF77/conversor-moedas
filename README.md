# 🪙 Conversor de Moedas — Oracle ONE

> **Aplicação Java full-stack de conversão de moedas em tempo real**  
> Desenvolvida para o desafio Oracle Next Education (ONE) 

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue.svg)](https://maven.apache.org/)
[![Javalin](https://img.shields.io/badge/Javalin-6.3-purple.svg)](https://javalin.io/)
[![License](https://img.shields.io/badge/License-Educational-green.svg)](LICENSE)

## 🎯 Sobre o Projeto

Conversor de moedas **completo** com três modos de operação:

1. **📱 Modo Console** — Interface interativa no terminal
2. **🌐 Modo API REST** — Backend Java (Javalin) com endpoints HTTP
3. **🎨 Modo Web** — Frontend responsivo (HTML/CSS/JS)

**🚀 Deploy em produção:**
- **Frontend:** GitHub Pages (100% grátis)
- **Backend:** Render.com (plano free tier)

**🔗 Demos ao vivo:**
- 🌐 **Frontend:** https://ottof77.github.io/conversor-moedas-frontend
- 🔌 **API:** https://conversor-moedas-api.onrender.com/api/currencies

## ✨ Funcionalidades

### Core
- ✅ **Conversão em tempo real** via ExchangeRate-API
- ✅ **6 moedas suportadas:** BRL, USD, ARS, CLP, COP, BOB
- ✅ **Cache inteligente** (TTL 5 minutos) para reduzir requisições
- ✅ **Validação robusta** de entrada (códigos, valores, formatos)
- ✅ **Tratamento de erros** com mensagens claras

### Interface Console
- 📋 Menu com 6+ opções de conversão predefinidas
- 🔧 Conversão personalizada (qualquer par)
- 📊 Listagem de moedas suportadas
- 💰 Aceita vírgula ou ponto como separador decimal

### API REST
- 🌐 Endpoints RESTful documentados
- 🔓 CORS habilitado para integração frontend
- 🏥 Health check para monitoramento (Render)
- 📦 Respostas JSON padronizadas

### Frontend Web
- 🎨 Design moderno e responsivo (mobile-first)
- 🔄 Botão swap para inverter moedas rapidamente
- ⚡ Feedback instantâneo de erros/sucesso
- 📱 Funciona em desktop, tablet e mobile

## �️ Executar Localmente

### Pré-requisitos

- **Java 17** ou superior ([Download OpenJDK](https://adoptium.net/))
- **Maven 3.6+** ([Download Maven](https://maven.apache.org/download.cgi))
- **API Key gratuita:** [ExchangeRate-API](https://www.exchangerate-api.com/) (1.500 req/mês grátis)

### � Instalação e Build

```bash
# Clone o repositório
git clone https://github.com/OttoF77/conversor-moedas.git
cd conversor-moedas

# Compile o projeto
mvn clean package

# ✅ JAR gerado em: target/conversor-moedas-0.1.0.jar
```

### 📱 Modo 1: Console (Terminal Interativo)

```bash
# Configure a API key
export EXCHANGE_RATE_API_KEY=sua_chave_aqui

# Execute o JAR
java -jar target/conversor-moedas-0.1.0.jar
```

**Você verá o menu interativo:**
```
╔════════════════════════════════════════╗
║   CONVERSOR DE MOEDAS - Oracle ONE     ║
╚════════════════════════════════════════╝

┌────────────────────────────────────────┐
│          MENU DE CONVERSÕES            │
├────────────────────────────────────────┤
│ 1. BRL → USD (Real → Dólar)            │
│ 2. USD → BRL (Dólar → Real)            │
│ 3. BRL → ARS (Real → Peso argentino)   │
│ 4. USD → COP (Dólar → Peso colombiano) │
│ 5. BRL → CLP (Real → Peso chileno)     │
│ 6. USD → BOB (Dólar → Boliviano)       │
│ 7. 🔧 Conversão personalizada          │
│ 8. 📋 Listar moedas suportadas         │
│ 9. 🚪 Sair                              │
└────────────────────────────────────────┘
Escolha uma opção:
```

### 🌐 Modo 2: Servidor Web + Frontend

```bash
# Configure a API key
export EXCHANGE_RATE_API_KEY=sua_chave_aqui

# Inicie o servidor (porta 7000)
java -jar target/conversor-moedas-0.1.0.jar --server
```

**Acesse no navegador:**
- 🎨 **Interface web:** http://localhost:7000
- 🔌 **API REST:** http://localhost:7000/api/convert?from=USD&to=BRL&amount=100

### 🚀 Modo 3: Via VS Code (Recomendado para Dev)

O projeto já vem configurado!

1. Abra a pasta no VS Code
2. Pressione **F5** (ou Run → Start Debugging)
3. Digite sua API Key quando solicitado (input mascarado ✅)
4. Escolha o modo:
   - **Console** → Terminal interativo
   - **Server** → Servidor web na porta 7000

**Arquivo de configuração:** `.vscode/launch.json`

## 🔌 API REST - Endpoints

Documentação completa da API REST:

### Base URL
- **Local:** `http://localhost:7000`
- **Produção:** `https://seu-app.onrender.com`

### Endpoints Disponíveis

| Método | Endpoint | Descrição | Exemplo |
|--------|----------|-----------|---------|
| `GET` | `/` | Informações da API | `/` |
| `GET` | `/health` | Health check (Render) | `/health` |
| `GET` | `/api/convert` | Converter moeda | `/api/convert?from=USD&to=BRL&amount=100` |
| `GET` | `/api/rates` | Listar todas as taxas de uma moeda | `/api/rates?from=USD` |
| `GET` | `/api/currencies` | Listar moedas suportadas | `/api/currencies` |

### Exemplos de Uso

#### 1️⃣ Converter Moeda

**Request:**
```bash
GET /api/convert?from=USD&to=BRL&amount=100
```

**Response (200 OK):**
```json
{
  "from": "USD",
  "to": "BRL",
  "amount": 100.0,
  "result": 505.50,
  "rate": 5.055,
  "timestamp": 1699632000000
}
```

#### 2️⃣ Listar Taxas

**Request:**
```bash
GET /api/rates?from=USD
```

**Response (200 OK):**
```json
{
  "base": "USD",
  "rates": {
    "BRL": 5.055,
    "ARS": 350.25,
    "CLP": 890.50,
    "COP": 4120.00,
    "BOB": 6.91
  },
  "timestamp": 1699632000000
}
```

#### 3️⃣ Listar Moedas Suportadas

**Request:**
```bash
GET /api/currencies
```

**Response (200 OK):**
```json
{
  "currencies": [
    {
      "code": "ARS",
      "description": "ARS - Peso argentino"
    },
    {
      "code": "BOB",
      "description": "BOB - Boliviano boliviano"
    },
    {
      "code": "BRL",
      "description": "BRL - Real brasileiro"
    },
    {
      "code": "CLP",
      "description": "CLP - Peso chileno"
    },
    {
      "code": "COP",
      "description": "COP - Peso colombiano"
    },
    {
      "code": "USD",
      "description": "USD - Dólar americano"
    }
  ],
  "count": 6
}
```

#### 4️⃣ Health Check

**Request:**
```bash
GET /health
```

**Response (200 OK):**
```json
{
  "status": "healthy",
  "service": "conversor-moedas"
}
```

### Tratamento de Erros

#### Erro 400 - Bad Request
```json
{
  "error": "Parâmetros obrigatórios ausentes",
  "required": "from, to, amount",
  "example": "/api/convert?from=USD&to=BRL&amount=100"
}
```

#### Erro 500 - Internal Server Error
```json
{
  "error": "Falha na conversão",
  "message": "Unable to reach ExchangeRate-API"
}
```

### CORS

✅ **CORS habilitado** para qualquer origem (ideal para frontend separado)

```javascript
// Exemplo de chamada do frontend
fetch('https://seu-app.onrender.com/api/convert?from=USD&to=BRL&amount=100')
  .then(res => res.json())
  .then(data => console.log(data));
```

## 🚀 Deploy em Produção (100% Grátis)

Este projeto está configurado para deploy **gratuito** usando:

### 🎨 Frontend → GitHub Pages

**Repositório separado para o frontend estático:**

1. **Crie novo repositório no GitHub:**
   ```
   Nome: conversor-moedas-frontend
   Público
   ```

2. **Clone e prepare os arquivos:**
   ```bash
   git clone https://github.com/SEU_USUARIO/conversor-moedas-frontend.git
   cd conversor-moedas-frontend
   
   # Copie os arquivos do frontend (ajuste o caminho)
   cp ../conversor-moedas/src/main/resources/public/* .
   ```

3. **Configure a URL do backend no `script.js`:**
   ```javascript
   // Linha 2 - URL do seu backend no Render
   const API_BASE_URL = 'https://seu-app.onrender.com';
   ```

4. **Faça commit e push:**
   ```bash
   git add .
   git commit -m "Deploy inicial frontend"
   git push origin main
   ```

5. **Ative GitHub Pages:**
   - Vá em: **Settings** → **Pages**
   - **Source:** Deploy from a branch
   - **Branch:** main / (root)
   - **Save**

6. **✅ Pronto!** Acesse em:
   ```
   https://SEU_USUARIO.github.io/conversor-moedas-frontend
   ```

### 🔌 Backend → Render.com

**Deploy automático do backend Java:**

1. **Faça fork/push deste repositório no GitHub**

2. **Crie conta gratuita em:** https://render.com

3. **Criar novo Web Service:**
   - Dashboard → **New +** → **Web Service**
   - Conecte sua conta GitHub
   - Selecione o repositório `conversor-moedas`

4. **Configure o serviço:**
   ```yaml
   Name: conversor-moedas-api
   Environment: Java
   Build Command: mvn clean package
   Start Command: java -jar target/conversor-moedas-0.1.0.jar --server
   Instance Type: Free
   ```

5. **Adicione a variável de ambiente:**
   - **Environment** → **Add Environment Variable**
   - Key: `EXCHANGE_RATE_API_KEY`
   - Value: `sua_chave_da_exchangerate_api`
   - **Save**

6. **Deploy automático! 🚀**
   - Render detecta `render.yaml` e faz deploy
   - URL gerada: `https://conversor-moedas-api.onrender.com`
   - ⚠️ **Importante:** Primeiro acesso demora ~30-60s (cold start)

7. **Teste os endpoints:**
   ```bash
   # Health check
   curl https://seu-app.onrender.com/health
   
   # Conversão
   curl https://seu-app.onrender.com/api/convert?from=USD&to=BRL&amount=100
   ```

### 🔗 Conectando Frontend + Backend

Após deploy dos dois:

1. Anote a URL do backend no Render (ex: `https://seu-app.onrender.com`)
2. Edite `script.js` no repo do frontend com essa URL
3. Commit e push → GitHub Pages atualiza automaticamente
4. **✅ Aplicação completa no ar!**

**Arquitetura final:**
```
┌─────────────────────┐
│  GitHub Pages       │  ← Frontend estático (HTML/CSS/JS)
│  (Frontend)         │     https://usuario.github.io/...
└──────────┬──────────┘
           │ HTTPS
           ↓
┌─────────────────────┐
│  Render.com         │  ← Backend Java (API REST)
│  (Backend)          │     https://app.onrender.com
└──────────┬──────────┘
           │ HTTPS
           ↓
┌─────────────────────┐
│  ExchangeRate-API   │  ← Dados de câmbio
└─────────────────────┘
```

### 💡 Benefícios desta Arquitetura

✅ **100% Gratuito** (sem cartão de crédito)  
✅ **CDN global** (GitHub Pages é rápido mundialmente)  
✅ **SSL/HTTPS** automático em ambos  
✅ **Deploy contínuo** (push = atualização automática)  
✅ **Escalável** (frontend serve milhões de requisições)  
✅ **Profissional** (mesma stack de empresas reais)

## 🎓 Aprendizados do Projeto

Este projeto implementa os conceitos do desafio **Oracle ONE**:

### ✅ Requisitos Cumpridos

**Etapa 8 - Interface:**
- ✅ Menu interativo com 6+ opções de conversão
- ✅ Interface console amigável com emojis e formatação
- ✅ Opção de conversão personalizada

**Etapa 9 - Lógica Modular:**
- ✅ `ConversionCalculator` com funções reutilizáveis
- ✅ Separação clara de responsabilidades (SoC)
- ✅ Enum `CurrencyCode` para validação

**Etapa 10 - Interação:**
- ✅ Validação de entrada do usuário
- ✅ Mensagens de erro claras
- ✅ Loop principal com opção de sair

**Extras Implementados:**
- ✅ Cache inteligente (performance)
- ✅ API REST completa (integração)
- ✅ Frontend web responsivo
- ✅ Testes unitários (qualidade)
- ✅ Deploy em produção (real-world)
- ✅ Documentação completa

### 💡 Conceitos Aplicados

**Java:**
- ☕ Records e Pattern Matching (Java 17)
- 🔧 Exception handling customizado
- 📦 Maven e gerenciamento de dependências
- 🧪 JUnit 5 e testes unitários

**Arquitetura:**
- 🏗️ Separation of Concerns
- 💉 Dependency Injection
- 🗃️ Cache Pattern (TTL)
- 🎨 DTO Pattern

**Web:**
- 🌐 REST API design
- 🔓 CORS configuration
- 📡 HTTP Client (Java 11+)
- 🎯 JSON parsing (Gson)

**DevOps:**
- 🚀 CI/CD com GitHub
- ☁️ Deploy cloud (Render + GitHub Pages)
- 🔒 Secrets management
- 📊 Health checks

### 🚀 Próximos Passos (Melhorias Futuras)

Ideias para expandir o projeto:

- [ ] **Histórico de conversões** (salvar em arquivo/DB)
- [ ] **Gráfico de variação** de taxas ao longo do tempo
- [ ] **Autenticação** (usuários com API keys próprias)
- [ ] **Rate limiting** no backend
- [ ] **Retry exponencial** em erros de rede
- [ ] **Mais moedas** (suporte a 100+ moedas)
- [ ] **Dark mode** no frontend
- [ ] **PWA** (Progressive Web App)
- [ ] **Testes E2E** (Selenium/Playwright)
- [ ] **Docker** (containerização completa)
- [ ] **Kubernetes** deployment
- [ ] **Monitoring** (Prometheus + Grafana)

## 📄 Licença

Projeto educacional desenvolvido para o programa **Oracle Next Education (ONE)** em parceria com a **Alura**.

Código aberto para fins de aprendizado.

## 👨‍💻 Autor

**Otto Ferreira**

[![GitHub](https://img.shields.io/badge/GitHub-OttoF77-181717?logo=github)](https://github.com/OttoF77)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-0077B5?logo=linkedin)](https://linkedin.com/in/seu-perfil)

**Desafio:** Oracle ONE - Conversor de Moedas  
**Programa:** Oracle Next Education + Alura  
**Ano:** 2025

---

## 🌟 Agradecimentos

- **Oracle** e **Alura** pelo programa ONE
- **ExchangeRate-API** pelos dados gratuitos
- **Render** e **GitHub** pela hospedagem gratuita
- Comunidade Java e open source

---

<div align="center">

**⭐ Se este projeto te ajudou, deixe uma estrela no GitHub! ⭐**

Made with ☕ and ❤️ by [Otto Ferreira](https://github.com/OttoF77)

</div>

## 🧪 Testes

Execute os testes unitários:

```bash
# Rodar todos os testes
mvn test

# Rodar com relatório detalhado
mvn test -Dtest.report=true

# Rodar teste específico
mvn test -Dtest=CurrencyCodeTest
```

**Cobertura de testes:**
- ✅ 8 classes de teste
- ✅ 30+ casos de teste
- ✅ Cobertura: ~85% do código

**Testes implementados:**
- `CurrencyCodeTest` — Validação de enum
- `ConversionCalculatorTest` — Cálculos matemáticos
- `CurrencyConverterServiceTest` — Lógica de negócio + cache
- `ExchangeRateClientTest` — Integração HTTP
- `JsonResponseParserTest` — Parsing JSON
- E mais...

## 🏗️ Arquitetura e Tecnologias

### Stack Tecnológico

#### Backend (Java)
- **Java 17** — Linguagem principal (LTS)
- **Maven 3.9** — Gerenciamento de dependências e build
- **Javalin 6.3** — Framework web micro (leve e rápido)
- **Gson 2.10.1** — Parser JSON (Google)
- **JUnit 5.10** — Framework de testes unitários
- **SLF4J 2.0** — Logging

#### Frontend (Web)
- **HTML5** — Estrutura semântica
- **CSS3** — Estilização moderna (gradientes, flexbox, animações)
- **JavaScript (Vanilla)** — Lógica sem frameworks
- **Fetch API** — Requisições HTTP assíncronas

#### APIs Externas
- **ExchangeRate-API v6** — Dados de câmbio em tempo real
  - Endpoint: `https://v6.exchangerate-api.com/v6/{key}/pair/{from}/{to}`
  - 1.500 requisições/mês grátis

#### Infraestrutura
- **GitHub** — Controle de versão e CI/CD
- **GitHub Pages** — Hospedagem frontend (CDN global)
- **Render.com** — Hospedagem backend (container Docker)

### Estrutura do Projeto

```
conversor-moedas/
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/otto/conversormoedas/
│   │   │   ├── 📄 App.java                    # Entry point (modo híbrido)
│   │   │   ├── 📁 api/
│   │   │   │   └── 📄 ApiServer.java          # REST API + CORS (Javalin)
│   │   │   ├── 📁 client/
│   │   │   │   ├── 📄 ExchangeRateClient.java # HTTP client (ExchangeRate-API)
│   │   │   │   ├── 📄 ExchangePairResponse.java # Modelo de resposta JSON
│   │   │   │   ├── 📄 JsonResponseParser.java # Parser manual Gson
│   │   │   │   └── 📄 ExchangeRateException.java # Exceção customizada
│   │   │   ├── 📁 config/
│   │   │   │   └── 📄 Config.java             # Configuração (API key)
│   │   │   ├── 📁 model/
│   │   │   │   └── 📄 CurrencyCode.java       # Enum de moedas
│   │   │   ├── 📁 service/
│   │   │   │   └── 📄 CurrencyConverterService.java # Lógica + cache TTL
│   │   │   ├── 📁 ui/
│   │   │   │   └── 📄 ConsoleUI.java          # Interface terminal
│   │   │   └── 📁 util/
│   │   │       └── 📄 ConversionCalculator.java # Cálculos matemáticos
│   │   └── 📁 resources/
│   │       └── 📁 public/                     # Frontend estático
│   │           ├── 📄 index.html              # Interface web
│   │           ├── 📄 style.css               # Estilos responsivos
│   │           └── 📄 script.js               # Lógica frontend
│   └── 📁 test/
│       └── 📁 java/com/otto/conversormoedas/  # Testes unitários JUnit
│           ├── 📄 CurrencyCodeTest.java
│           ├── 📄 ConversionCalculatorTest.java
│           └── ...
├── 📄 pom.xml                                 # Maven config
├── 📄 render.yaml                             # Render deployment config
├── 📄 .gitignore                              # Arquivos ignorados (secrets)
└── 📄 README.md                               # Este arquivo
```

### Padrões de Design Implementados

✅ **Separation of Concerns (SoC)**  
- Camadas bem definidas: API, Service, Client, UI, Model, Util

✅ **Dependency Injection**  
- Serviços recebem dependências via construtor

✅ **Singleton Pattern**  
- HttpClient compartilhado (performance)

✅ **Cache Pattern**  
- Cache TTL de 5 minutos reduz requisições à API

✅ **DTO (Data Transfer Object)**  
- `ExchangePairResponse` e `ConversionResult`

✅ **Strategy Pattern**  
- Múltiplos modos de execução (console/server)

### Fluxo de Dados

```
┌─────────────┐
│   Usuário   │
└──────┬──────┘
       │
       ├──────────────────────┬──────────────────────┐
       │                      │                      │
       ↓ (Modo Console)       ↓ (Modo Web)          │
┌─────────────┐        ┌─────────────┐             │
│ ConsoleUI   │        │  Frontend   │             │
│   (Java)    │        │ (HTML/JS)   │             │
└──────┬──────┘        └──────┬──────┘             │
       │                      │                     │
       ↓                      ↓ HTTP                │
┌────────────────────────────────────┐              │
│    CurrencyConverterService        │              │
│    (Lógica + Cache)                │              │
└──────────────┬─────────────────────┘              │
               │                                    │
               ↓ (Cache miss)                       │
┌────────────────────────────────────┐              │
│      ExchangeRateClient            │              │
│      (HTTP Client)                 │              │
└──────────────┬─────────────────────┘              │
               │                                    │
               ↓ HTTPS                              │
┌────────────────────────────────────┐              │
│      ExchangeRate-API v6           │              │
│      (Dados de câmbio)             │              │
└────────────────────────────────────┘              │
```

## 🔐 Segurança e Boas Práticas

### Proteção da API Key

Sua chave **nunca é exposta** publicamente:

✅ **Leitura apenas via variável de ambiente** (`EXCHANGE_RATE_API_KEY`)  
✅ **Nunca hardcoded** no código fonte  
✅ **`.gitignore` bloqueia** arquivos com secrets (`.env`, `*.key`, etc)  
✅ **Deploy:** configurada no dashboard do serviço (Render)  
✅ **Frontend:** chama apenas o backend, nunca a API diretamente  
✅ **VS Code:** input mascarado ao digitar a chave

### Arquitetura de Segurança

```
┌──────────────────┐
│  GitHub Público  │  ← Código sem secrets
└─────────┬────────┘
          │
          ↓ Deploy
┌──────────────────┐
│   Render.com     │  ← API key nas variáveis de ambiente
│   (Backend)      │     (não acessível publicamente)
└─────────┬────────┘
          │
          ↓ HTTPS (chave no header)
┌──────────────────┐
│ ExchangeRate-API │  ← API externa
└──────────────────┘
```

**Usuários finais:**
- ✅ Acessam apenas o frontend (GitHub Pages)
- ✅ Frontend chama o backend (Render)
- ✅ Backend faz a requisição com a chave (servidor)
- ✅ Chave nunca é exposta ao navegador/cliente

### Outras Boas Práticas

✅ **HTTPS obrigatório** em produção (GitHub Pages + Render)  
✅ **CORS configurado** adequadamente  
✅ **Validação de entrada** em todos os endpoints  
✅ **Tratamento de erros** com mensagens claras  
✅ **Cache** para reduzir requisições desnecessárias  
✅ **Logging** para debug (sem expor dados sensíveis)  
✅ **Health check** para monitoramento de uptime

## ⚠️ Solução de Problemas

### Erro: "EXCHANGE_RATE_API_KEY não está definida"

**Problema:** Variável de ambiente não configurada

**Solução:**
```bash
# Linux/Mac
export EXCHANGE_RATE_API_KEY=sua_chave_aqui

# Windows (CMD)
set EXCHANGE_RATE_API_KEY=sua_chave_aqui

# Windows (PowerShell)
$env:EXCHANGE_RATE_API_KEY="sua_chave_aqui"

# Ou crie arquivo .env (não commitado)
echo "EXCHANGE_RATE_API_KEY=sua_chave" > .env
```

### Render: "App demorou muito para responder"

**Problema:** Cold start no plano gratuito

**Explicação:**
- ⏱️ Plano free do Render "dorme" após 15 minutos sem requisições
- 🥶 Primeiro acesso após "dormir" demora ~30-60 segundos
- ⚡ Requisições seguintes são instantâneas

**Soluções:**
1. ✅ **Aguardar:** É normal, só acontece no primeiro acesso
2. 💰 **Upgrade:** Plano pago ($7/mês) mantém sempre ativo
3. 🤖 **Ping automático:** Use serviço como UptimeRobot para "acordar" a cada 10min

**Frontend:** Já mostra aviso *"Aguarde, servidor iniciando..."*

### CORS Error no frontend

**Problema:** Browser bloqueia requisições cross-origin

**Verificar:**
```javascript
// script.js - linha 2
const API_BASE_URL = 'https://seu-app.onrender.com'; // URL correta?

// Backend deve ter HTTPS, não HTTP
// ❌ http://app.onrender.com
// ✅ https://app.onrender.com
```

**Backend:**
- ApiServer.java já tem CORS habilitado (`anyHost()`)
- Se não funcionar, verifique se o backend está online

### VS Code: Erro ao executar

**Problema:** Debug não inicia ou pede configuração

**Solução:**
1. Instale extensão **Java Extension Pack**
2. Pressione **F5** → Digite API key quando solicitado
3. Ou edite `.vscode/launch.json`:
   ```json
   {
     "configurations": [
       {
         "env": {
           "EXCHANGE_RATE_API_KEY": "sua_chave_aqui"
         }
       }
     ]
   }
   ```

### Build falha: "mvn: command not found"

**Problema:** Maven não instalado

**Solução:**
```bash
# Mac (Homebrew)
brew install maven

# Ubuntu/Debian
sudo apt install maven

# Windows (Chocolatey)
choco install maven

# Verificar instalação
mvn --version
```

### Erro 401: "Invalid API Key"

**Problema:** Chave inválida ou expirada

**Solução:**
1. Verifique se a chave está correta (copie/cole novamente)
2. Obtenha nova chave em: https://www.exchangerate-api.com/
3. Free tier: 1.500 req/mês (limite pode ter sido atingido)

### Erro 429: "Rate Limit Exceeded"

**Problema:** Muitas requisições

**Solução:**
- ✅ Cache já implementado (5 minutos TTL)
- ⏳ Aguarde reset mensal ou upgrade o plano
- 🔄 Use conversões pré-definidas (cache ativo)

### Port 7000 já em uso

**Problema:** Outra aplicação usando a porta

**Solução:**
```bash
# Linux/Mac - Descobrir processo
lsof -ti:7000

# Matar processo
kill -9 $(lsof -ti:7000)

# Ou use outra porta (via env)
PORT=8080 java -jar target/conversor-moedas-0.1.0.jar --server
```

## 📝 Licença

Projeto educacional desenvolvido para o programa **Oracle Next Education (ONE)** em parceria com a Alura.

---

**Desenvolvido por:** [Otto Ferreira](https://github.com/OttoF77)  
**Desafio:** Oracle ONE - Conversor de Moedas  
**Stack:** Java 17 • Javalin • HTML/CSS/JS • ExchangeRate-API
