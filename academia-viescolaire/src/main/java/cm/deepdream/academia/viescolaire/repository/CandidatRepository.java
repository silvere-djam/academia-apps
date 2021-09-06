package cm.deepdream.academia.viescolaire.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Candidat;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.data.SalleExamen;
@Repository
public interface CandidatRepository extends CrudRepository<Candidat, Long>{
	public List<Candidat> findByEtablissement(Etablissement etablissement) ;
	public Candidat findByIdAndEtablissement(Long id, Etablissement etablissement) ;
	public List<Candidat> findByEtablissementAndExamen(Etablissement etablissement, Examen examen) ;
	public List<Candidat> findByEtablissementAndCentreExamen(Etablissement etablissement, CentreExamen centreExamen) ;
	public List<Candidat> findByEtablissementAndSalleExamen(Etablissement etablissement, SalleExamen salleExamen) ;
}
