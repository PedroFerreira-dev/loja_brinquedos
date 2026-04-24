# 🧸 CatalogoBrinquedos - Visão Geral do Projeto (Backend)

Este documento serve como um guia de referência para o desenvolvimento do Frontend, detalhando a estrutura do banco de dados, os endpoints da API e as regras de segurança.

## 🚀 Tecnologias Utilizadas
- **Linguagem:** Java 17
- **Framework:** Spring Boot 3+
- **Banco de Dados:** MySQL (JPA/Hibernate)
- **Segurança:** Spring Security (Basic Auth)
- **Comunicação:** JSON via REST API

---

## 🏗️ Modelo de Dados (Entidades)

### 1. Brinquedo (`Brinquedo`)
- Atributos padrão: `id`, `nome`, `preco`, `categoria`, `caminhoImagem`, `desconto`, `quantidade`, `vendas`, `descricao`.

### 2. Usuário (`Usuario`)
- `login`, `senha`, `nome`, `cpf` (Único), `email`, `endereco`, `perfil`.

### 3. Encomenda (`Encomenda`)
- `status`: Pode ser `PROCESSANDO`, `ENVIADO` ou `ENTREGUE`.

---

## 📡 Endpoints da API

### 🔓 Públicos
- `GET /api/brinquedos/**` (Catálogo)
- `POST /api/usuarios/registrar` (Cadastro)

### 🔑 Exigem LOGIN (ROLE_USER ou ROLE_ADMIN)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **POST** | `/api/brinquedos/encomendar/{id}` | Cria novo pedido (Status: `PROCESSANDO`). |
| **GET** | `/api/brinquedos/historico` | Lista pedidos do usuário. |
| **POST** | `/api/brinquedos/encomendar/{id}/confirmar` | **Cliente confirma que recebeu** (Muda p/ `ENTREGUE`). |
| **GET** | `/api/usuarios/me` | Dados do perfil. |

### 🔐 Apenas ADMIN
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **PATCH** | `/api/brinquedos/encomendar/{id}/status?novoStatus=ENVIADO` | **Admin despacha o pedido**. |
| **GET** | `/api/brinquedos/encomendas/todas` | **Admin lista todas as encomendas de todos os clientes.** |
| **POST/PUT/DELETE** | `/api/brinquedos/**` | Gerenciar o catálogo. |

---

## 🛡️ Regras de Status da Encomenda
1.  Todo pedido nasce como `PROCESSANDO`.
2.  O **Administrador** altera para `ENVIADO` quando posta o produto.
3.  O **Cliente** (e somente ele) altera para `ENTREGUE` ao clicar em um botão "Confirmar Recebimento" no seu histórico.
4.  O Admin **não pode** forçar o status `ENTREGUE`.

## 🛠️ Notas para o Frontend
- **Mensagens de Erro:** O backend agora retorna mensagens amigáveis em caso de CPF ou Login duplicado.
- **Tailwind/CSS:** Todas as rotas de arquivos estáticos estão liberadas.
- **Login:** Use o endpoint `/api/usuarios/login` para validar se as credenciais digitadas estão corretas.

---
*Versão 2.0 - Adicionado controle de status e fluxo de entrega.*
