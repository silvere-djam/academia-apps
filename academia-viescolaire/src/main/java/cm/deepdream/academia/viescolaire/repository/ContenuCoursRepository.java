package cm.deepdream.academia.viescolaire.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.viescolaire.data.ContenuCours;
import cm.deepdream.academia.viescolaire.data.Cours;
@Repository
public interface ContenuCoursRepository extends CrudRepository<ContenuCours, Long>{
	public List<ContenuCours> findByEtablissement(Etablissement etablissement) ;
	public ContenuCours findByIdAndEtablissement(Long id, Etablissement etablissement) ;
	public List<ContenuCours> findByCours(Cours  cours) ;
}
