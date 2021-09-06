package cm.deepdream.academia.viescolaire.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.souscription.data.Etablissement;
@Repository
public interface EleveRepository extends CrudRepository<Eleve, Long>{
	public Eleve findByMatricule(String matricule) ;
	public List<Eleve> findByEtablissement(Etablissement etablissement) ;
	public Eleve findByIdAndEtablissement(Long id, Etablissement etablissement) ;
	public List<Eleve> findByEtablissementAndClasse(Etablissement etablissement, Classe classe) ;
	public List<Eleve> findByEtablissementAndStatut(Etablissement etablissement, String statut) ;
	public long countByEtablissement (Etablissement etablissement) ;
}
