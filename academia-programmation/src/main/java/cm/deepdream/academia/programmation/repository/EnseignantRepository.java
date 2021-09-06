package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;
@Repository
public interface EnseignantRepository extends CrudRepository<Enseignant, Long>{
	public List<Enseignant> findByEtablissement(Etablissement etablissement) ;
	public Enseignant findByIdAndEtablissement(Long id, Etablissement etablissement) ;
	public List<Enseignant> findByEtablissementAndDomaine(Etablissement etablissement, Domaine domaine) ;
	public List<Enseignant> findByEtablissementAndSituation (Etablissement etablissement, String situation) ;
}
