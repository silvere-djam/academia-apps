package cm.deepdream.academia.viescolaire.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.souscription.data.Etablissement;
@Repository
public interface CentreExamenRepository extends CrudRepository<CentreExamen, Long>{
	public CentreExamen findByIdAndEtablissement (Long id, Etablissement etablissement) ;
	public List<CentreExamen> findByEtablissementAndExamen (Etablissement etablissement, Examen examen) ;
	public List<CentreExamen> findByEtablissementAndAnneeScolaire (Etablissement etablissement, AnneeScolaire anneeScolaire) ;
}
