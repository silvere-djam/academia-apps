package cm.deepdream.academia.security.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.security.data.Localisation;
import cm.deepdream.academia.security.data.Utilisateur;
@Repository
public interface LocalisationRepository extends CrudRepository<Localisation, Long> {
	public List<Localisation> findByUtilisateur (Utilisateur utilisateur) throws Exception ;
}
