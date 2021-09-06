package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Candidature;
@Repository
public interface CandidatureRepository extends CrudRepository<Candidature, Long>{
	public List<Candidature> findByEtablissement(Etablissement etablissement) ;
	public Candidature findByIdAndEtablissement(Long id, Etablissement etablissement) ;
	public List<Candidature> findByEtablissementAndAnneeScolaire(Etablissement etablissement, AnneeScolaire anneeScolaire) ;
	
}
