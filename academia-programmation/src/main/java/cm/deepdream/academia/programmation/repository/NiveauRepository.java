package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Niveau;
@Repository
public interface NiveauRepository extends CrudRepository<Niveau, Long>{
	public List<Niveau> findByEtablissement (Etablissement etablissement) ;
	public Niveau findByIdAndEtablissement (Long id, Etablissement etablissement) ;
}
