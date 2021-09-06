package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Semestre;
@Repository
public interface SemestreRepository extends CrudRepository<Semestre, Long>{
	public Semestre findByIdAndEtablissement (Long id, Etablissement etablissement) ;
	public List<Semestre> findByEtablissementAndAnneeScolaire (Etablissement etablissement, AnneeScolaire anneeScolaire) ;
	public Semestre findByEtablissementAndAnneeScolaireAndCourant (Etablissement etablissement, AnneeScolaire anneeScolaire, int courant) ;
}
