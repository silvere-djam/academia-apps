package cm.deepdream.academia.viescolaire.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.viescolaire.data.Evaluation;
@Repository
public interface EvaluationRepository extends CrudRepository<Evaluation, Long>{
	public Evaluation findByIdAndEtablissement (Long id, Etablissement etablissement) ;
	public List<Evaluation> findByEtablissement(Etablissement etablissement) ;
	public List<Evaluation> findByEtablissementAndClasseAndAnneeScolaire(Etablissement etablissement, Classe classe, AnneeScolaire anneeScolaire) ;
}
