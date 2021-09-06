package cm.deepdream.academia.security.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.security.data.Utilisateur;
@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long>{
	public Utilisateur findByEmail(String email) ;
	public Utilisateur findByTelephone(String telephone) ;
	public Utilisateur findByEtablissementAndId(Etablissement etablissement, Long id) ;
	public List<Utilisateur> findByEtablissement(Etablissement etablissement) ;
	public Utilisateur findByIdAndCodeActivationAndStatut(Long id, String codeActivation, String statut);
}
