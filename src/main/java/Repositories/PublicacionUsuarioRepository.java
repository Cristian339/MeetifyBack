package Repositories;

import Entities.PublicacionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PublicacionUsuarioRepository extends JpaRepository<PublicacionUsuario, Long>, JpaSpecificationExecutor<PublicacionUsuario> {

}