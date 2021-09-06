package cm.deepdream.academia.viescolaire.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.viescolaire.data.GrilleAppreciation;
@Repository
public interface GrilleAppreciationRepository extends CrudRepository<GrilleAppreciation, Long>{

}
