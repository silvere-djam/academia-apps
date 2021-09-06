package cm.deepdream.academia.viescolaire.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.viescolaire.data.Absence;
import cm.deepdream.academia.viescolaire.data.Cours;
@Repository
public interface AbsenceRepository extends CrudRepository<Absence, Long>{
	public List<Absence> findByEtablissement(Etablissement etablissement) ;
	public Absence findByIdAndEtablissement(Long id, Etablissement etablissement) ;
	public List<Absence> findByCours(Cours cours) ;
}
