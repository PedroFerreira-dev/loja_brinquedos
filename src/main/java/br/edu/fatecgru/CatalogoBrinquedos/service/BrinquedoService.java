package br.edu.fatecgru.CatalogoBrinquedos.service;

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

    public BrinquedoResponseDTO salvarBrinquedo(BrinquedoRequestDTO request) {
        Brinquedo entidade = new Brinquedo();
        mapearDtoParaEntidade(request, entidade);
        entidade.setVendas(0);
        return converterParaResponse(repository.save(entidade));
    }

    public BrinquedoResponseDTO atualizarBrinquedo(Long id, BrinquedoRequestDTO request) {
        Brinquedo entidadeExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brinquedo não encontrado."));
        mapearDtoParaEntidade(request, entidadeExistente);
        return converterParaResponse(repository.save(entidadeExistente));
    }

    public List<BrinquedoResponseDTO> listarTodos() {
        return repository.findAll().stream().map(this::converterParaResponse).toList();
    }

    public void excluirBrinquedo(Long id) {
        repository.deleteById(id);
    }

    public BrinquedoRequestDTO buscarPorId(Long id) {
        Brinquedo b = repository.findById(id).orElseThrow(() -> new RuntimeException("Não encontrado"));
        BrinquedoRequestDTO dto = new BrinquedoRequestDTO();
        dto.setId(b.getId());
        dto.setNome(b.getNome());
        dto.setMarca(b.getMarca());
        dto.setPreco(b.getPreco());
        dto.setCategoria(b.getCategoria());
        dto.setCaminhoImagem(b.getCaminhoImagem());
        dto.setDesconto(b.getDesconto());
        dto.setQuantidade(b.getQuantidade());
        dto.setDescricao(b.getDescricao());
        return dto;
    }

    public List<BrinquedoResponseDTO> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome).stream().map(this::converterParaResponse).toList();
    }

    public List<BrinquedoResponseDTO> buscarPorCategoria(String categoria) {
        return repository.findByCategoria(categoria).stream().map(this::converterParaResponse).toList();
    }

    public List<String> listarCategorias() {
        return repository.buscarCategoriasUnicas();
    }

    public List<String> listarMarcas() {
        return repository.buscarMarcasUnicas();
    }

    private void mapearDtoParaEntidade(BrinquedoRequestDTO request, Brinquedo entidade) {
        entidade.setNome(request.getNome());
        entidade.setMarca(request.getMarca());
        entidade.setPreco(request.getPreco());
        entidade.setCategoria(request.getCategoria());
        entidade.setCaminhoImagem(request.getCaminhoImagem());
        entidade.setDesconto(request.getDesconto());
        entidade.setQuantidade(request.getQuantidade());
        entidade.setDescricao(request.getDescricao());
    }

    private BrinquedoResponseDTO converterParaResponse(Brinquedo b) {
        return new BrinquedoResponseDTO(
            b.getId(), b.getNome(), b.getMarca(), b.getPreco(),
            b.getCategoria(), b.getCaminhoImagem(), b.getDesconto(),
            b.getQuantidade(), b.getDescricao()
        );
    }

    public List<BrinquedoResponseDTO> listarPorPreco(String sentido) {
        List<Brinquedo> lista = sentido.equalsIgnoreCase("desc") ? repository.findAllByOrderByPrecoDesc() : repository.findAllByOrderByPrecoAsc();
        return lista.stream().map(this::converterParaResponse).toList();
    }

    public List<BrinquedoResponseDTO> listarNovidades() {
        return repository.findTop3ByOrderByIdDesc().stream().map(this::converterParaResponse).toList();
    }

    public List<BrinquedoResponseDTO> listarDestaques() {
        return repository.findTop3ByOrderByVendasDesc().stream().map(this::converterParaResponse).toList();
    }

    public BrinquedoResponseDTO encomendarBrinquedo(Long id) {
        Brinquedo b = repository.findById(id).orElseThrow(() -> new RuntimeException("Não encontrado"));
        if (b.getQuantidade() > 0) {
            b.setQuantidade(b.getQuantidade() - 1);
            b.setVendas(b.getVendas() == null ? 1 : b.getVendas() + 1);
        }
        return converterParaResponse(repository.save(b));
    }

    public BrinquedoResponseDTO reporEstoque(Long id, Integer qtd) {
        Brinquedo b = repository.findById(id).orElseThrow(() -> new RuntimeException("Não encontrado"));
        b.setQuantidade(b.getQuantidade() + qtd);
        return converterParaResponse(repository.save(b));
    }
}