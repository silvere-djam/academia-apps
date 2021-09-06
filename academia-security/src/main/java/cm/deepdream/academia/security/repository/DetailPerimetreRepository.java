package cm.deepdream.academia.security.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.security.data.DetailPerimetre;
import cm.deepdream.academia.security.data.Utilisateur;
@Repository
public interface DetailPerimetreRepository extends CrudRepository<DetailPerimetre, Long>{
	public List<DetailPerimetre> findByUtilisateur (Utilisateur utilisateur ) throws Exception ;
}
