package cm.deepdream.academia.souscription.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.data.Etablissement;
@Repository
public interface EtablissementRepository extends CrudRepository<Etablissement, Long>{
	@Query(value = "select count (e) > 0 from Etablissement e where e.contactChef.telephone =:telephone")
	public boolean existsByTelephoneChef (@Param("telephone") String telephone) ;
	
	@Query(value = "select count (e) > 0 from Etablissement e where e.contactInformaticien.telephone =:telephone")
	public boolean existsByTelephoneInformaticien (@Param("telephone") String telephone) ;
}
