package br.edu.fatecgru.CatalogoBrinquedos.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Brinquedo;

public interface BrinquedoRepository extends JpaRepository<Brinquedo, Long> {

    // Busca brinquedos por categoria exata
    List<Brinquedo> findByCategoria(String categoria);

    // Remove todos os registros vinculados a uma categoria específica
    @Transactional
    void deleteByCategoria(String categoria);

    // Consulta customizada para retornar lista de categorias únicas
    @Query("SELECT DISTINCT b.categoria FROM Brinquedo b")
    List<String> buscarCategoriasUnicas();

    // Retorna os 3 registros com maior ID (os últimos inseridos)
    List<Brinquedo> findTop3ByOrderByIdDesc();
}
