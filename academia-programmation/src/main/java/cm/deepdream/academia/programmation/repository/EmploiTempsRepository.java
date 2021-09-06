package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.EmploiTemps;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;
@Repository
public interface EmploiTempsRepository extends CrudRepository<EmploiTemps, Long>{
	public EmploiTemps findByIdAndEtablissement (Long id, Etablissement etablissement) ;
	public List<EmploiTemps> findByEtablissement (Etablissement etablissement) ;
	public List<EmploiTemps> findByEtablissementAndClasse(Etablissement etablissement, Classe classe) ;
	public List<EmploiTemps> findByEtablissementAndEnseignantPrincipal(Etablissement etablissement, Enseignant enseignant) ;
	public List<EmploiTemps> findByEtablissementAndClasseAndEnseignantPrincipal(Etablissement etablissement, Classe classe, Enseignant enseignant) ;
	public List<EmploiTemps> findByEtablissementAndAnneeScolaire(Etablissement etablissement, AnneeScolaire anneeScolaire) ;
	public List<EmploiTemps> findByEtablissementAndClasseAndAnneeScolaire(Etablissement etablissement, Classe classe, AnneeScolaire anneeScolaire) ;
}
