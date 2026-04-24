package br.edu.fatecgru.CatalogoBrinquedos.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.edu.fatecgru.CatalogoBrinquedos.dto.BrinquedoRequestDTO;
import br.edu.fatecgru.CatalogoBrinquedos.dto.BrinquedoResponseDTO;
import br.edu.fatecgru.CatalogoBrinquedos.dto.EncomendaResponseDTO;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Brinquedo;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Encomenda;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.StatusEncomenda;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Usuario;
import br.edu.fatecgru.CatalogoBrinquedos.repository.BrinquedoRepository;
import br.edu.fatecgru.CatalogoBrinquedos.repository.EncomendaRepository;

@Service
public class BrinquedoService {

    @Autowired
    private BrinquedoRepository repository;

    @Autowired
    private EncomendaRepository encomendaRepository;

    // Salva um novo registro de brinquedo no banco de dados
    public BrinquedoResponseDTO salvarBrinquedo(BrinquedoRequestDTO request) {
        Brinquedo entidade = new Brinquedo();
        mapearDtoParaEntidade(request, entidade);
        Brinquedo entidadeSalva = repository.save(entidade);
        return converterParaResponse(entidadeSalva);
    }
    
    // Atualiza um brinquedo existente a partir do ID
    public BrinquedoResponseDTO atualizarBrinquedo(Long id, BrinquedoRequestDTO request) {
        Brinquedo entidadeExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Brinquedo com ID " + id + " não encontrado."));

        mapearDtoParaEntidade(request, entidadeExistente);
        Brinquedo entidadeSalva = repository.save(entidadeExistente);
        return converterParaResponse(entidadeSalva);
    }
    
    // Retorna todos os brinquedos cadastrados
    public List<BrinquedoResponseDTO> listarTodos() {
        List<Brinquedo> listaEntidades = repository.findAll();
        return listaEntidades.stream().map(this::converterParaResponse).toList();
    }
    
    // Deleta um brinquedo por ID
    public void excluirBrinquedo(Long id) {
        repository.deleteById(id);
    }

    // Deleta todos os brinquedos vinculados a uma categoria e suas encomendas
    public void excluirPorCategoria(String categoria) {
        repository.deleteByCategoria(categoria);
    }
    
    // Retorna o DTO de um brinquedo específico por ID
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
        dto.setQuantidade(entidade.getQuantidade()); 
        dto.setDescricao(entidade.getDescricao());

        return dto;
    }
    
    // Busca brinquedos filtrados por categoria
    public List<BrinquedoResponseDTO> buscarPorCategoria(String categoria) {
        List<Brinquedo> entidades = repository.findByCategoria(categoria);
        return entidades.stream().map(this::converterParaResponse).toList();
    }
    
    // Retorna a lista de nomes de categorias únicas presentes no banco
    public List<String> listarCategorias() {
        return repository.buscarCategoriasUnicas();
    }
    
    // Transfere dados do DTO de requisição para a entidade JPA
    private void mapearDtoParaEntidade(BrinquedoRequestDTO request, Brinquedo entidade) {
        entidade.setNome(request.getNome());
        entidade.setPreco(request.getPreco());
        entidade.setCategoria(request.getCategoria());
        entidade.setCaminhoImagem(request.getCaminhoImagem());
        entidade.setDesconto(request.getDesconto());
        entidade.setQuantidade(request.getQuantidade());
        entidade.setDescricao(request.getDescricao());
    }
    
    // Converte a entidade persistida em um DTO de resposta
    private BrinquedoResponseDTO converterParaResponse(Brinquedo entidade) {
        return new BrinquedoResponseDTO(
                entidade.getId(), 
                entidade.getNome(),
                entidade.getPreco(),
                entidade.getCategoria(),
                entidade.getCaminhoImagem(),
                entidade.getDesconto(), 
                entidade.getQuantidade(),
                entidade.getDescricao(),
                entidade.getVendas()
        );
    }

    // Retorna os 3 brinquedos mais recentes cadastrados
    public List<BrinquedoResponseDTO> listarNovidades() {
        return repository.findTop3ByOrderByIdDesc()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    // Lógica para criação de novo pedido com baixa de estoque
    public EncomendaResponseDTO encomendarBrinquedo(Long id, Usuario usuario) {
        Brinquedo brinquedo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brinquedo não encontrado"));

        if (brinquedo.getQuantidade() <= 0) {
            throw new RuntimeException("Produto esgotado no estoque!");
        }

        brinquedo.setQuantidade(brinquedo.getQuantidade() - 1);
        brinquedo.setVendas(brinquedo.getVendas() + 1);
        repository.save(brinquedo);

        Encomenda encomenda = new Encomenda(usuario, brinquedo, brinquedo.getPreco());
        Encomenda salva = encomendaRepository.save(encomenda);

        return converterParaEncomendaResponse(salva);
    }

    // Retorna o histórico de pedidos de um usuário específico
    public List<EncomendaResponseDTO> listarHistorico(Usuario usuario) {
        return encomendaRepository.findByUsuarioOrderByDataPedidoDesc(usuario)
            .stream()
            .map(this::converterParaEncomendaResponse).toList();
    }

    // Lista todos os pedidos registrados no sistema (Visão Admin)
    public List<EncomendaResponseDTO> listarTodas() {
        return encomendaRepository.findAll()
            .stream()
            .sorted((e1, e2) -> e2.getDataPedido().compareTo(e1.getDataPedido()))
            .map(this::converterParaEncomendaResponse).toList();
    }

    // Permite que o Admin altere o status de processamento do pedido
    public EncomendaResponseDTO adminAlterarStatus(Long encomendaId, StatusEncomenda novoStatus) {
        Encomenda e = encomendaRepository.findById(encomendaId)
                .orElseThrow(() -> new RuntimeException("Encomenda não encontrada"));
        
        if (novoStatus == StatusEncomenda.ENTREGUE) {
            throw new RuntimeException("Apenas o cliente pode confirmar a entrega final.");
        }
        
        e.setStatus(novoStatus);
        return converterParaEncomendaResponse(encomendaRepository.save(e));
    }

    // Permite que o cliente confirme o recebimento do produto
    public EncomendaResponseDTO clienteConfirmarEntrega(Long encomendaId, Usuario usuario) {
        Encomenda e = encomendaRepository.findById(encomendaId)
                .orElseThrow(() -> new RuntimeException("Encomenda não encontrada"));
        
        if (!e.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você só pode confirmar suas próprias encomendas.");
        }
        
        e.setStatus(StatusEncomenda.ENTREGUE);
        return converterParaEncomendaResponse(encomendaRepository.save(e));
    }

    // Converte entidade de pedido para DTO de resposta
    private EncomendaResponseDTO converterParaEncomendaResponse(Encomenda e) {
        return new EncomendaResponseDTO(
            e.getId(),
            e.getBrinquedo().getNome(),
            e.getBrinquedo().getCategoria(),
            e.getPrecoNaData(),
            e.getDataPedido(),
            e.getStatus().name()
        );
    }
}
