# ToyToy - Catálogo Fullstack de Brinquedos

Este projeto foi desenvolvido como parte da disciplina **Laboratório de Engenharia de Software**. Trata-se de um ecossistema completo para gestão e visualização de um catálogo de brinquedos, com foco em usabilidade, integridade de dados e arquitetura moderna.

## 🚀 Tecnologias Utilizadas

### Backend
*   **Java 17** com **Spring Boot 3**
*   **Spring Data JPA** para persistência de dados
*   **MySQL** como banco de dados relacional
*   **Bean Validation** para regras de integridade
*   **Spring Security** (Basic Auth)

### Frontend
*   **HTML5** Semântico
*   **JavaScript (Vanilla)** para lógica dinâmica
*   **Tailwind CSS** para estilização moderna e responsiva
*   **Lucide Icons** para ícones de interface

## ✨ Funcionalidades Principais

*   **Catálogo Interativo:** Visualização de brinquedos com filtros dinâmicos por categoria e pesquisa inteligente.
*   **Seções Especiais:** Áreas automáticas para "Novidades", "Itens em Promoção" e "Dia do Herói".
*   **Painel Administrativo:**
    *   Gestão completa (CRUD) de brinquedos.
    *   Sistema de categorias permanentes no banco de dados.
    *   **Upload de Imagens:** Suporte para carregar imagens diretamente do computador (Base64).
    *   Gestão de pedidos e alteração de status de envio.
*   **Perfil do Usuário:** Histórico de pedidos e confirmação de recebimento.
*   **Validações Avançadas:** Feedback visual em tempo real em todos os formulários.

## 🛠️ Como Executar

### 1. Backend
1. Certifique-se de ter o **MySQL** rodando (configurações em `application.properties`).
2. Importe o projeto na sua IDE favorita (Eclipse/STS/IntelliJ).
3. Execute a classe principal `CatalogoBrinquedosApplication.java`.
4. O servidor iniciará em `http://localhost:8080`.

### 2. Frontend
1. Navegue até a pasta do frontend.
2. Abra o arquivo `index.html` em seu navegador (recomenda-se o uso de extensões como *Live Server* no VS Code).

## 👥 Equipe de Desenvolvimento
*   **Pedro Ferreira** - Desenvolvimento Backend e API.
*   **José Edglê** - Arquitetura de Dados e Segurança.
*   **Davi Publio** - Liderança de Frontend e UI/UX.

---
*Projeto desenvolvido para fins acadêmicos - 2026*
