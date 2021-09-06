package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Programme;
import cm.deepdream.academia.programmation.data.UE;
@Repository
public interface ProgrammeRepository extends CrudRepository<Programme, Long>{
	public List<Programme> findByEtablissement (Etablissement etablissement) ;
	public List<Programme> findByEtablissementAndAnneeScolaire (Etablissement etablissement, AnneeScolaire anneeScolaire) ;
	public Programme findByEtablissementAndAnneeScolaireAndUe (Etablissement etablissement, 
			AnneeScolaire anneeScolaire, UE ue) ;
	public Programme findByIdAndEtablissement (Long id, Etablissement etablissement) ;
}
