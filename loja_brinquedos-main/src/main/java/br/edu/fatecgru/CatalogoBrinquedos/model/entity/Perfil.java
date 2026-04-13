package br.edu.fatecgru.CatalogoBrinquedos.model.entity;

public enum Perfil {
		//O prefixo "ROLE_" é uma convenção do Spring Security para identificar os perfis de acesso.
		ROLE_ADMIN, // Perfil de administrador com acesso total (gerenciar brinquedos, usuários, pedidos, etc)
	ROLE_USER // Perfil de usuário comum com acesso limitado (ver brinquedos e fazer pedidos)
}
