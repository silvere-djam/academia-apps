package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Groupe;
import cm.deepdream.academia.programmation.data.UE;
@Repository
public interface UERepository extends CrudRepository<UE, Long>{
	public Boolean existsByEtablissementAndClasseAndDomaine(Etablissement etablissement, 
			Classe classe, Domaine domaine) ;
	public List<UE> findByEtablissement (Etablissement etablissement) ;
	public List<UE> findByDomaine (Domaine domaine) ;
	public UE findByIdAndEtablissement (Long id, Etablissement etablissement) ;
	public List<UE> findByClasseAndGroupe (Classe classe, Groupe groupe) ;
	public List<UE> findByClasse (Classe classe) ;
	
}
