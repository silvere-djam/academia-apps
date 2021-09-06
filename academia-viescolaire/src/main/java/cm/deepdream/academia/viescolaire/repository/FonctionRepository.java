package cm.deepdream.academia.viescolaire.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.Fonction;
@Repository
public interface FonctionRepository extends CrudRepository<Fonction, Long>{

}
