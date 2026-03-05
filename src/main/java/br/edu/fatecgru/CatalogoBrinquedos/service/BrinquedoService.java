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
	    Brinquedo entidade;

	    // 1. LÓGICA DE DECISÃO: O JPA precisa saber se é um novo ou um existente
	    if (request.getId() != null) {
	        // Se tem ID, buscamos o que já existe no Docker para atualizar
	        entidade = repository.findById(request.getId())
	                .orElseThrow(() -> new RuntimeException("Brinquedo não encontrado"));
	    } else {
	        // Se não tem ID, aí sim criamos um 'new' para o banco fazer o INSERT
	        entidade = new Brinquedo();
	    }

	    // 2. CONVERSÃO DE ENTRADA: Atualizamos os campos (com Double para o desconto!)
	    entidade.setNome(request.getNome());
	    entidade.setPreco(request.getPreco());
	    entidade.setCategoria(request.getCategoria());
	    entidade.setCaminhoImagem(request.getCaminhoImagem());
	    entidade.setDesconto(request.getDesconto());

	    // 3. AÇÃO NO BANCO: O .save() decide entre INSERT ou UPDATE baseado no ID
	    Brinquedo entidadeSalva = repository.save(entidade);

	    // 4. CONVERSÃO DE SAÍDA: Devolvemos o DTO completo para a tela
	    //Foi feita uma mudança aqui. Ao invés de instanciar o BrinquedoResponseDTO e depois
	    //retornar ele, agora já instanciamos e retornamos na mesma linha. O resultado é o mesmo, 
	    //mas ficou mais enxuto.
	    return new BrinquedoResponseDTO(
	            entidadeSalva.getId(), 
	            entidadeSalva.getNome(),
	            entidadeSalva.getPreco(),
	            entidadeSalva.getCategoria(),
	            entidadeSalva.getCaminhoImagem(),
	            entidadeSalva.getDesconto()
	    );
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
	    dto.setId(entidade.getId());
	    // Se o seu RequestDTO ainda não tem ID, você vai precisar adicionar o campo 'id' nele!
	    dto.setNome(entidade.getNome());
	    dto.setPreco(entidade.getPreco());
	    dto.setCategoria(entidade.getCategoria());
	    dto.setCaminhoImagem(entidade.getCaminhoImagem());
	    dto.setDesconto(entidade.getDesconto());

	    return dto;
	}
	
}