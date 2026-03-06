package br.edu.fatecgru.CatalogoBrinquedos.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Brinquedo;

public interface BrinquedoRepository extends JpaRepository<Brinquedo, Long> {

    // Busca por parte do nome
    List<Brinquedo> findByNomeContainingIgnoreCase(String nome);

    // Busca por categoria exata
    List<Brinquedo> findByCategoria(String categoria);

    // Lista as categorias únicas para o filtro do front
    @Query("SELECT DISTINCT b.categoria FROM Brinquedo b")
    List<String> buscarCategoriasUnicas();

    // Ordenações por preço
    List<Brinquedo> findAllByOrderByPrecoAsc();
    List<Brinquedo> findAllByOrderByPrecoDesc();

    // Pega os 3 últimos (Novidades)
    List<Brinquedo> findTop3ByOrderByIdDesc();
    
    List<Brinquedo> findTop3ByOrderByVendasDesc();
}