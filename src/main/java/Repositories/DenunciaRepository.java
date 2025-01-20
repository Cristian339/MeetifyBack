package Repositories;

import Entities.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DenunciaRepository extends JpaRepository<Denuncia, Long>, JpaSpecificationExecutor<Denuncia> {

}