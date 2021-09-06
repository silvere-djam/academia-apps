package cm.deepdream.academia.viescolaire.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.data.Localite;
@Repository
public interface VilleRepository extends CrudRepository<Localite, Long>{

}
