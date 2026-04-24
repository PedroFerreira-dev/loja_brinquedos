package br.edu.fatecgru.CatalogoBrinquedos.repository;

import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNome(String nome);
    
    @Transactional
    void deleteByNome(String nome);
}
