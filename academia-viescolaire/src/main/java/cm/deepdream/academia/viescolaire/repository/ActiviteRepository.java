package cm.deepdream.academia.viescolaire.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.viescolaire.data.Activite;
@Repository
public interface ActiviteRepository extends CrudRepository<Activite, Long>{
	public List<Activite> findByEtablissement (Etablissement etablissement) ;
	public Activite findByIdAndEtablissement (Long id, Etablissement etablissement) ;
}
