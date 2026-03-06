package br.edu.fatecgru.CatalogoBrinquedos.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.edu.fatecgru.CatalogoBrinquedos.dto.BrinquedoRequestDTO;
import br.edu.fatecgru.CatalogoBrinquedos.dto.BrinquedoResponseDTO;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Brinquedo;
import br.edu.fatecgru.CatalogoBrinquedos.repository.BrinquedoRepository;

@Service
public class BrinquedoService {

    @Autowired
    private BrinquedoRepository repository;

    // Método para salvar um novo brinquedo, mapeando os dados do DTO para a entidade e retornando o DTO de resposta
    public BrinquedoResponseDTO salvarBrinquedo(BrinquedoRequestDTO request) {
        Brinquedo entidade = new Brinquedo();
        mapearDtoParaEntidade(request, entidade);
        Brinquedo entidadeSalva = repository.save(entidade);
        return converterParaResponse(entidadeSalva);
    }
    
    // Método para atualizar um brinquedo existente, buscando a entidade pelo ID, mapeando os dados do DTO e salvando as alterações
    public BrinquedoResponseDTO atualizarBrinquedo(Long id, BrinquedoRequestDTO request) {
        Brinquedo entidadeExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Brinquedo com ID " + id + " não encontrado."));

        mapearDtoParaEntidade(request, entidadeExistente);
        Brinquedo entidadeSalva = repository.save(entidadeExistente);
        return converterParaResponse(entidadeSalva);
    }
    
    // Método para listar todos os brinquedos, convertendo cada entidade para DTO de resposta
    public List<BrinquedoResponseDTO> listarTodos() {
        List<Brinquedo> listaEntidades = repository.findAll();
        return listaEntidades.stream().map(this::converterParaResponse).toList();
    }
    
    // Método para excluir um brinquedo por ID
    public void excluirBrinquedo(Long id) {
        repository.deleteById(id);
    }
    
    // Método para buscar um brinquedo por ID e retornar os dados no formato do RequestDTO (para preencher o formulário de edição)
    public BrinquedoRequestDTO buscarPorId(Long id) {
        Brinquedo entidade = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brinquedo não encontrado"));

        BrinquedoRequestDTO dto = new BrinquedoRequestDTO();
        dto.setId(entidade.getId());
        dto.setNome(entidade.getNome());
        dto.setPreco(entidade.getPreco());
        dto.setCategoria(entidade.getCategoria());
        dto.setCaminhoImagem(entidade.getCaminhoImagem());
        dto.setDesconto(entidade.getDesconto());
        // Faltava adicionar a quantidade aqui no RequestDTO!
        dto.setQuantidade(entidade.getQuantidade()); 

        return dto;
    }
    
    // Método para filtrar por nome (busca parcial, case-insensitive) 
    public List<BrinquedoResponseDTO> buscarPorNome(String nome) {
        List<Brinquedo> entidades = repository.findByNomeContainingIgnoreCase(nome);
        return entidades.stream().map(this::converterParaResponse).toList();
    }
    
    // Método para filtrar por categoria
    public List<BrinquedoResponseDTO> buscarPorCategoria(String categoria) {
        List<Brinquedo> entidades = repository.findByCategoria(categoria);
        return entidades.stream().map(this::converterParaResponse).toList();
    }
    
    // Método para listar categorias únicas (para o Front)
    public List<String> listarCategorias() {
        return repository.buscarCategoriasUnicas();
    }
    
    //método auxiliar para mapear os dados do DTO para a entidade
 // No seu BrinquedoService.java, atualize o mapeamento:

    private void mapearDtoParaEntidade(BrinquedoRequestDTO request, Brinquedo entidade) {
        // VALIDAÇÕES DE SEGURANÇA
        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new RuntimeException("O nome do brinquedo é obrigatório.");
        }
        
        if (request.getPreco() == null || request.getPreco() <= 0) {
            throw new RuntimeException("O preço deve ser maior que zero.");
        }
        
        if (request.getQuantidade() != null && request.getQuantidade() < 0) {
            throw new RuntimeException("A quantidade em estoque não pode ser negativa.");
        }

        // Se passar pelas validações, ele segue o fluxo normal
        entidade.setNome(request.getNome());
        entidade.setPreco(request.getPreco());
        entidade.setCategoria(request.getCategoria());
        entidade.setCaminhoImagem(request.getCaminhoImagem());
        entidade.setDesconto(request.getDesconto());
        entidade.setQuantidade(request.getQuantidade());
        entidade.setDescricao(request.getDescricao());
    }
    
    // Método para converter entidade em DTO de resposta
    private BrinquedoResponseDTO converterParaResponse(Brinquedo entidade) {
     
        return new BrinquedoResponseDTO(
                entidade.getId(), 
                entidade.getNome(),
                entidade.getPreco(),
                entidade.getCategoria(),
                entidade.getCaminhoImagem(),
                entidade.getDesconto(), 
                entidade.getQuantidade(),
                entidade.getDescricao()
        );
    }

    //Métodos para ordenação por preço
    public List<BrinquedoResponseDTO> listarPorPreco(String sentido) {
        List<Brinquedo> entidades = sentido.equalsIgnoreCase("desc") 
                ? repository.findAllByOrderByPrecoDesc() 
                : repository.findAllByOrderByPrecoAsc();
        
        return entidades.stream().map(this::converterParaResponse).toList();
    }

    // Método para listar as 3 últimas novidades
    public List<BrinquedoResponseDTO> listarNovidades() {
        return repository.findTop3ByOrderByIdDesc()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }
    
 // Lógica de Encomenda (Usuário Comum)
    public BrinquedoResponseDTO encomendarBrinquedo(Long id) {
        Brinquedo brinquedo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brinquedo não encontrado"));

        if (brinquedo.getQuantidade() <= 0) {
            throw new RuntimeException("Produto esgotado no estoque!");
        }

        // Regra: Diminui 1 do estoque e aumenta 1 nas vendas (destaque)
        brinquedo.setQuantidade(brinquedo.getQuantidade() - 1);
        brinquedo.setVendas(brinquedo.getVendas() + 1);

        return converterParaResponse(repository.save(brinquedo));
    }

    // Lógica de Reposição (Administrador)
    public BrinquedoResponseDTO reporEstoque(Long id, Integer novaQuantidade) {
        Brinquedo brinquedo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brinquedo não encontrado"));

        // Soma a nova quantidade ao que já existe
        brinquedo.setQuantidade(brinquedo.getQuantidade() + novaQuantidade);

        return converterParaResponse(repository.save(brinquedo));
    }

    // Busca de Destaques
    public List<BrinquedoResponseDTO> listarDestaques() {
        return repository.findTop3ByOrderByVendasDesc()
                .stream().map(this::converterParaResponse).toList();
    }
        
}