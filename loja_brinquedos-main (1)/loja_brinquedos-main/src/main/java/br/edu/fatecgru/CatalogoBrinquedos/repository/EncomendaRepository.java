package br.edu.fatecgru.CatalogoBrinquedos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Encomenda;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Usuario;
import java.util.List;

public interface EncomendaRepository extends JpaRepository<Encomenda, Long> {
    List<Encomenda> findByUsuarioOrderByDataPedidoDesc(Usuario usuario);
}
