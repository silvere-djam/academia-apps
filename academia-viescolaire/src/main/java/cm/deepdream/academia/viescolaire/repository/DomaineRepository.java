package cm.deepdream.academia.viescolaire.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.souscription.data.Etablissement;
@Repository
public interface DomaineRepository extends CrudRepository<Domaine, Long>{
	public List<Domaine> findByEtablissement(Etablissement etablissement) ;
	public Domaine findByIdAndEtablissement(Long id, Etablissement etablissement) ;
}
