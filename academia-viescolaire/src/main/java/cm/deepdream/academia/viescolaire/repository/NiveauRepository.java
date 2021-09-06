package cm.deepdream.academia.viescolaire.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.Niveau;
@Repository
public interface NiveauRepository extends CrudRepository<Niveau, Long>{

}
