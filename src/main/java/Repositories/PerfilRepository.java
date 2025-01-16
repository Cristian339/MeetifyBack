package Repositories;

import Entities.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PerfilRepository extends JpaRepository<Perfil, Long>, JpaSpecificationExecutor<Perfil> {

}