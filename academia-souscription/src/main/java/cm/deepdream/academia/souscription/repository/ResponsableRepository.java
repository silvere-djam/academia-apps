package cm.deepdream.academia.souscription.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.model.Responsable;
@Repository
public interface ResponsableRepository extends CrudRepository<Responsable, Long>{
	public Boolean existsByMatricule (String matricule) ;
}
