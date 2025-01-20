package Repositories;

import Entities.UsuarioCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UsuarioCategoriaRepository extends JpaRepository<UsuarioCategoria, Long>, JpaSpecificationExecutor<UsuarioCategoria> {

}