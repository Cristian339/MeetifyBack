package Repositories;

import Entities.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long>, JpaSpecificationExecutor<Calificacion> {

}