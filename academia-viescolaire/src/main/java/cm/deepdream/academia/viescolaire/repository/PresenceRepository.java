package cm.deepdream.academia.viescolaire.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.viescolaire.data.Evaluation;
import cm.deepdream.academia.viescolaire.data.Presence;
@Repository
public interface PresenceRepository extends CrudRepository<Presence, Long>{
	public Presence findByIdAndCours (Long idPresence, Cours cours) ;
	public Presence findByIdAndEvaluation (Long idPresence, Evaluation evaluation) ;
	public List<Presence> findByCours (Cours cours) ;
	public List<Presence> findByEvaluation (Evaluation evaluation) ;
}
