package cm.deepdream.academia.viescolaire.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.Filiere;
@Repository
public interface FiliereRepository extends CrudRepository<Filiere, Long>{

}
