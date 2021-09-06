package cm.deepdream.academia.programmation.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Filiere;
@Repository
public interface FiliereRepository extends CrudRepository<Filiere, Long>{
	public List<Filiere> findByEtablissement (Etablissement etablissement) ;
	public Filiere findByIdAndEtablissement(Long id, Etablissement etablissement) ;
}
