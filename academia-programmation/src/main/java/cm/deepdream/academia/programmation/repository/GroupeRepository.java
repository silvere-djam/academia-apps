package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Groupe;
@Repository
public interface GroupeRepository extends CrudRepository<Groupe, Long>{
	public List<Groupe> findByEtablissement (Etablissement etablissement) ;
	public Groupe findByIdAndEtablissement (Long id, Etablissement etablissement) ;
}
