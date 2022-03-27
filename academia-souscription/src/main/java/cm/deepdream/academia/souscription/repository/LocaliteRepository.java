package cm.deepdream.academia.souscription.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.souscription.model.Localite;
import cm.deepdream.academia.souscription.model.Region;
@Repository
public interface LocaliteRepository extends CrudRepository<Localite, Long>{
	public List<Localite> findByRegion (Region region) ;
}
