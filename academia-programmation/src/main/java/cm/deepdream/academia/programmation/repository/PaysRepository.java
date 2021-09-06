package cm.deepdream.academia.programmation.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.data.Pays;
@Repository
public interface PaysRepository extends CrudRepository<Pays, Long>{
	
	public Pays findByLibelle(String libelle) ;

}
