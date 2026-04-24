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

    public BrinquedoResponseDTO salvarBrinquedo(BrinquedoRequestDTO request) {
        Brinquedo entidade = new Brinquedo();
        mapearDtoParaEntidade(request, entidade);
        Brinquedo entidadeSalva = repository.save(entidade);
        return converterParaResponse(entidadeSalva);
    }
    
    public BrinquedoResponseDTO atualizarBrinquedo(Long id, BrinquedoRequestDTO request) {
        Brinquedo entidadeExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Brinquedo com ID " + id + " não encontrado."));

        mapearDtoParaEntidade(request, entidadeExistente);
        Brinquedo entidadeSalva = repository.save(entidadeExistente);
        return converterParaResponse(entidadeSalva);
    }
    
    public List<BrinquedoResponseDTO> listarTodos() {
        List<Brinquedo> listaEntidades = repository.findAll();
        return listaEntidades.stream().map(this::converterParaResponse).toList();
    }
    
    public void excluirBrinquedo(Long id) {
        repository.deleteById(id);
    }
    
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
    
    // BUSCA INTELIGENTE: Pesquisa o termo em nome, categoria e descrição
    public List<BrinquedoResponseDTO> buscarPorTermo(String termo) {
        List<Brinquedo> entidades = repository.findByNomeContainingIgnoreCaseOrCategoriaContainingIgnoreCaseOrDescricaoContainingIgnoreCase(
                termo, termo, termo);
        return entidades.stream().map(this::converterParaResponse).toList();
    }
    
    // ALIAS: Método buscarPorNome chamado pelo HomeController
    public List<BrinquedoResponseDTO> buscarPorNome(String nome) {
        return buscarPorTermo(nome);
    }
    
    public List<BrinquedoResponseDTO> buscarPorCategoria(String categoria) {
        List<Brinquedo> entidades = repository.findByCategoria(categoria);
        return entidades.stream().map(this::converterParaResponse).toList();
    }
    
    public List<String> listarCategorias() {
        return repository.buscarCategoriasUnicas();
    }
    
    private void mapearDtoParaEntidade(BrinquedoRequestDTO request, Brinquedo entidade) {
        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new RuntimeException("O nome do brinquedo é obrigatório.");
        }
        if (request.getPreco() == null || request.getPreco() <= 0) {
            throw new RuntimeException("O preço deve ser maior que zero.");
        }
        if (request.getQuantidade() != null && request.getQuantidade() < 0) {
            throw new RuntimeException("A quantidade em estoque não pode ser negativa.");
        }

        entidade.setNome(request.getNome());
        entidade.setPreco(request.getPreco());
        entidade.setCategoria(request.getCategoria());
        entidade.setCaminhoImagem(request.getCaminhoImagem());
        entidade.setDesconto(request.getDesconto());
        entidade.setQuantidade(request.getQuantidade());
        entidade.setDescricao(request.getDescricao());
    }
    
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

    public List<BrinquedoResponseDTO> listarPorPreco(String sentido) {
        List<Brinquedo> entidades = sentido.equalsIgnoreCase("desc") 
                ? repository.findAllByOrderByPrecoDesc() 
                : repository.findAllByOrderByPrecoAsc();
        
        return entidades.stream().map(this::converterParaResponse).toList();
    }

    public List<BrinquedoResponseDTO> listarNovidades() {
        return repository.findTop3ByOrderByIdDesc()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

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

    public List<EncomendaResponseDTO> listarHistorico(Usuario usuario) {
        return encomendaRepository.findByUsuarioOrderByDataPedidoDesc(usuario)
            .stream()
            .map(this::converterParaEncomendaResponse).toList();
    }

    // NOVA FUNÇÃO: Admin lista todas as encomendas do sistema
    public List<EncomendaResponseDTO> listarTodas() {
        return encomendaRepository.findAll()
            .stream()
            .sorted((e1, e2) -> e2.getDataPedido().compareTo(e1.getDataPedido())) // Mais recentes primeiro
            .map(this::converterParaEncomendaResponse).toList();
    }

    // NOVA FUNÇÃO: Admin altera status para ENVIADO
    public EncomendaResponseDTO adminAlterarStatus(Long encomendaId, StatusEncomenda novoStatus) {
        Encomenda e = encomendaRepository.findById(encomendaId)
                .orElseThrow(() -> new RuntimeException("Encomenda não encontrada"));
        
        if (novoStatus == StatusEncomenda.ENTREGUE) {
            throw new RuntimeException("Apenas o cliente pode confirmar a entrega final.");
        }
        
        e.setStatus(novoStatus);
        return converterParaEncomendaResponse(encomendaRepository.save(e));
    }

    // NOVA FUNÇÃO: Cliente confirma entrega
    public EncomendaResponseDTO clienteConfirmarEntrega(Long encomendaId, Usuario usuario) {
        Encomenda e = encomendaRepository.findById(encomendaId)
                .orElseThrow(() -> new RuntimeException("Encomenda não encontrada"));
        
        if (!e.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você só pode confirmar suas próprias encomendas.");
        }
        
        e.setStatus(StatusEncomenda.ENTREGUE);
        return converterParaEncomendaResponse(encomendaRepository.save(e));
    }

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

    public BrinquedoResponseDTO reporEstoque(Long id, Integer novaQuantidade) {
        Brinquedo brinquedo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brinquedo não encontrado"));

        brinquedo.setQuantidade(brinquedo.getQuantidade() + novaQuantidade);

        return converterParaResponse(repository.save(brinquedo));
    }

    public List<BrinquedoResponseDTO> listarDestaques() {
        return repository.findTop3ByOrderByVendasDesc()
                .stream().map(this::converterParaResponse).toList();
    }
}
