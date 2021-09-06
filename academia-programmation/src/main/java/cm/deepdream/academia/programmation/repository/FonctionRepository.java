package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Fonction;
@Repository
public interface FonctionRepository extends CrudRepository<Fonction, Long>{
	public List<Fonction> findByEtablissement (Etablissement etablissement) ;
	public Fonction findByIdAndEtablissement(Long id, Etablissement etablissement) ;
}
