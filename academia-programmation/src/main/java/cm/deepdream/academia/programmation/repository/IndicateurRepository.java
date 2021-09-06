package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Indicateur;
import cm.deepdream.academia.programmation.data.UE;
@Repository
public interface IndicateurRepository extends CrudRepository<Indicateur, Long>{
	public Indicateur findByNumAndEtablissement(Long idIndicateur, Etablissement etablissement) ;
	public List<Indicateur> findByEtablissementAndClasseAndAnneeScolaire(Etablissement etablissement, Classe classe, AnneeScolaire anneeScolaire) ;
	public List<Indicateur> findByEtablissementAndDisciplineAndAnneeScolaire(Etablissement etablissement, UE ue, AnneeScolaire anneeScolaire) ;
}
