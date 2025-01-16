package Repositories;

import Entities.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long>, JpaSpecificationExecutor<Publicacion> {

}