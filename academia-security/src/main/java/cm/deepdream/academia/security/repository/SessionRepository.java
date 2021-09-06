package cm.deepdream.academia.security.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.security.data.Session;
import cm.deepdream.academia.security.data.Utilisateur;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long>{
	public List<Session> findByUtilisateur(Utilisateur utilisateur) ;
	public List<Session> findByEtablissement (Etablissement etablissement) ;
	public Session findByEtablissementAndId (Etablissement etablissement, Long id) ;
}
