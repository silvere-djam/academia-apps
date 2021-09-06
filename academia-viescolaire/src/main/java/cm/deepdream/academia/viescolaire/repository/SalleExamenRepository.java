package cm.deepdream.academia.viescolaire.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.data.SalleExamen;
import cm.deepdream.academia.souscription.data.Etablissement;
@Repository
public interface SalleExamenRepository extends CrudRepository<SalleExamen, Long>{
	public SalleExamen findByIdAndEtablissement (Long id, Etablissement etablissement) ;
	public List<SalleExamen> findByEtablissementAndExamen (Etablissement etablissement, Examen examen) ;
	public List<SalleExamen> findByEtablissementAndCentreExamen (Etablissement etablissement, CentreExamen centreExamen) ;
}
