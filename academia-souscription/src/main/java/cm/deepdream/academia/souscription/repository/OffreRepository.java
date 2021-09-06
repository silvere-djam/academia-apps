package cm.deepdream.academia.souscription.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.data.Offre;
@Repository
public interface OffreRepository extends CrudRepository<Offre, Long>{
	public List<Offre> findByMaxElevesGreaterThanEqual(Integer nbEleves) throws Exception;
	public List<Offre> findByMinElevesLessThanEqualAndMaxElevesGreaterThanEqualOrderByMinElevesAsc(Integer minEleves, Integer maxEleves) throws Exception;
}
