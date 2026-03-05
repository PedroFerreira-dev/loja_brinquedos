package br.edu.fatecgru.CatalogoBrinquedos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.fatecgru.CatalogoBrinquedos.dto.BrinquedoRequestDTO;
import br.edu.fatecgru.CatalogoBrinquedos.dto.BrinquedoResponseDTO;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Brinquedo;
import br.edu.fatecgru.CatalogoBrinquedos.repository.BrinquedoRepository;

@Service // Avisa o Spring que esta classe contém as regras de negócio
public class BrinquedoService {

	@Autowired // Pede para o Spring injetar o nosso Repository de 3 linhas aqui
			   // automaticamente. Uma injeção de dependência
	private BrinquedoRepository repository;

	// Método para Salvar um Brinquedo
	public BrinquedoResponseDTO salvarBrinquedo(BrinquedoRequestDTO request) {

		// Padrões de segurança. Nunca devemos mandar o DTO diretamente para o banco,
		// nem o entity direto para tela

		// 1. CONVERSÃO DE ENTRADA: Pega o DTO (agora com 5 itens) e transforma na Entidade
		Brinquedo entidade = new Brinquedo();
		entidade.setNome(request.getNome());
		entidade.setPreco(request.getPreco());
		entidade.setCategoria(request.getCategoria());
		entidade.setCaminhoImagem(request.getCaminhoImagem());
		entidade.setDesconto(request.getDesconto());

		// 2. AÇÃO NO BANCO: Manda o Repository salvar no Docker.
		// Aqui o banco gera o ID sozinho e devolve a entidade completa salva.
		Brinquedo entidadeSalva = repository.save(entidade);

		// 3. CONVERSÃO DE SAÍDA: Pega a Entidade salva (agora com ID) e transforma no
		// Response DTO passando as 6 informações no construtor!
		BrinquedoResponseDTO response = new BrinquedoResponseDTO(
				entidadeSalva.getId(), 
				entidadeSalva.getNome(),
				entidadeSalva.getPreco(),
				entidadeSalva.getCategoria(),
				entidadeSalva.getCaminhoImagem(),
				entidadeSalva.getDesconto()
		);

		// Devolve a caixa de saída já pronta
		return response;
	}

	// Método para Listar todos os Brinquedos
	public List<BrinquedoResponseDTO> listarTodos() {
        // 1. O Repository faz o "SELECT * FROM" automático e traz as Entidades
        List<Brinquedo> listaEntidades = repository.findAll();

        // 2. Criamos uma lista vazia de DTOs (Caixas de Saída)
        List<BrinquedoResponseDTO> listaDTOs = new ArrayList<>();

        // 3. Pegamos cada Entidade do banco e empacotamos no DTO
        for (Brinquedo entidade : listaEntidades) {
			
			// Aqui também atualizamos para passar as 6 informações!
            BrinquedoResponseDTO dto = new BrinquedoResponseDTO(
                entidade.getId(), 
                entidade.getNome(), 
                entidade.getPreco(),
				entidade.getCategoria(),
				entidade.getCaminhoImagem(),
				entidade.getDesconto()
            );
            listaDTOs.add(dto);
        }

        // 4. Devolvemos a lista pronta para o Controller
        return listaDTOs;
    }
	
	public void excluirBrinquedo(Long id) {
	    repository.deleteById(id);
	}
	
	public BrinquedoRequestDTO buscarPorId(Long id) {
	    // Buscamos a entidade no banco
	    Brinquedo entidade = repository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Brinquedo não encontrado"));

	    // Convertemos a Entidade para RequestDTO para o formulário conseguir ler
	    BrinquedoRequestDTO dto = new BrinquedoRequestDTO();
	    // Se o seu RequestDTO ainda não tem ID, você vai precisar adicionar o campo 'id' nele!
	    dto.setNome(entidade.getNome());
	    dto.setPreco(entidade.getPreco());
	    dto.setCategoria(entidade.getCategoria());
	    dto.setCaminhoImagem(entidade.getCaminhoImagem());
	    dto.setDesconto(entidade.getDesconto());

	    return dto;
	}
	
}