package cm.deepdream.academia.souscription.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.souscription.model.Etablissement;
@Repository
public interface EtablissementRepository extends CrudRepository<Etablissement, Long>{
	
}
