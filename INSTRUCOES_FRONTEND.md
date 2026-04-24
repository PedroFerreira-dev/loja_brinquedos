# 🛠️ Guia Prático de Integração (Frontend -> Backend)

Este guia contém os formatos exatos de JSON e os cabeçalhos necessários para o seu Frontend (HTML/Tailwind/JS) conversar com o Backend.

---

## 🔐 1. Autenticação (Basic Auth)
O sistema exige login para **Encomendar** e ver **Histórico**.
- **Como enviar:** Adicione o cabeçalho `Authorization` em todas as chamadas protegidas.
- **Formato:** `Basic <base64(login:senha)>`

**Exemplo em Javascript (Fetch):**
```javascript
const authHeader = 'Basic ' + btoa('seu_login:sua_senha');

fetch('/api/brinquedos/encomendar/1', {
    method: 'POST',
    headers: {
        'Authorization': authHeader,
        'Content-Type': 'application/json'
    }
});
```

---

## 📝 2. Fluxo de Usuário (Conta e Login)

### Criar Nova Conta (Público)
**URL:** `POST /api/usuarios/registrar`
**JSON:**
```json
{
  "login": "joao_silva",
  "senha": "123",
  "nome": "João da Silva",
  "cpf": "123.456.789-00",
  "email": "joao@email.com",
  "endereco": "Rua das Flores, 100",
  "perfil": "ROLE_USER"
}
```

### Validar Login (Exige Auth)
**URL:** `POST /api/usuarios/login`
- **Dica:** Chame este endpoint ao fazer login no Front. Se retornar `200 OK`, as credenciais estão certas. Ele retorna os dados do usuário logado.

---

## 🧸 3. Fluxo de Compras (Encomendas)

### Fazer um Pedido (Exige Auth)
**URL:** `POST /api/brinquedos/encomendar/{id}`
- **Retorno:** JSON com `id` da encomenda e `status: "PROCESSANDO"`.

### Ver Meus Pedidos (Exige Auth)
**URL:** `GET /api/brinquedos/historico`
- **Retorno:** Lista de encomendas. Use o campo `status` para mudar a cor no Tailwind.

### Confirmar Recebimento (Exige Auth)
**URL:** `POST /api/brinquedos/encomendar/{id}/confirmar`
- **Ação:** O usuário clica no botão "Já recebi" no histórico. O status muda para `ENTREGUE`.

### Admin: Ver todas as encomendas do sistema (Exige ROLE_ADMIN)
**URL:** `GET /api/brinquedos/encomendas/todas`
- **Ação:** Retorna a lista completa de pedidos de todos os clientes.

### Admin: Atualizar status do pedido (Exige ROLE_ADMIN)
**URL:** `PATCH /api/brinquedos/encomendar/{id}/status?novoStatus=ENVIADO`
- **Ação:** Admin marca o pedido como enviado.

---

## ⚠️ 4. Tratamento de Erros
Se o CPF ou Login já existirem, o backend retornará um erro `400 Bad Request` com este JSON:
```json
{
  "status": 400,
  "mensagem": "Erro: Já existe um cadastro com este Login ou CPF.",
  "timestamp": "..."
}
```
**Dica:** Exiba o campo `mensagem` em um `alert()` ou `modal` do Tailwind.

---
*Este arquivo deve ser usado como referência para as chamadas Fetch/Axios no seu projeto Frontend.*
