package cm.deepdream.academia.viescolaire.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.viescolaire.repository.PresenceRepository;
import cm.deepdream.academia.viescolaire.dao.SequenceDAO;
import cm.deepdream.academia.viescolaire.data.Activite;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.viescolaire.data.Evaluation;
import cm.deepdream.academia.viescolaire.data.Presence;
@Transactional
@Service
public class PresenceService {
	private Logger logger = Logger.getLogger(PresenceService.class.getName()) ;
	@Autowired
	private PresenceRepository presenceRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	
	public Presence creer (Presence presence) throws Exception{
		try {
			logger.log(Level.INFO, "Lancement de l'ajout du presence") ;
			presence.setId(sequenceDAO.nextGlobalId(Activite.class.getName()));
			presence.setNum(sequenceDAO.nextId(presence.getEtablissement(), Activite.class.getName()));
			presence.setDateCreation(LocalDateTime.now());
			presence.setDateDernMaj(LocalDateTime.now());
			presenceRepository.save(presence) ;
			return presence ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Presence creerEtVerifier (Presence presence, Cours cours) throws Exception{
		try {
			logger.log(Level.INFO, "Lancement de l'ajout du presence") ;
			presence.setId(sequenceDAO.nextGlobalId(Activite.class.getName()));
			presence.setNum(sequenceDAO.nextId(presence.getEtablissement(), Activite.class.getName()));
			presence.setDateCreation(LocalDateTime.now());
			presence.setDateDernMaj(LocalDateTime.now());
			presenceRepository.save(presence) ;
			return presence ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Presence creerEtVerifier (Presence presence, Evaluation evaluation) throws Exception{
		try {
			logger.log(Level.INFO, "Lancement de l'ajout du presence") ;
			presence.setId(sequenceDAO.nextGlobalId(Activite.class.getName()));
			presence.setNum(sequenceDAO.nextId(presence.getEtablissement(), Activite.class.getName()));
			presence.setDateCreation(LocalDateTime.now());
			presence.setDateDernMaj(LocalDateTime.now());
			presenceRepository.save(presence) ;
			return presence ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Presence modifier (Presence presence) throws Exception{
		try {
			presence.setDateDernMaj(LocalDateTime.now());
			presenceRepository.save(presence) ;
			return presence ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idPresence) throws Exception {
		try {
			Presence presence = presenceRepository.findById(idPresence).orElseThrow(Exception::new) ;
			presenceRepository.delete(presence) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Presence presence) throws Exception{
		try {
			presenceRepository.delete(presence) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Presence rechercher (Cours cours, Long idPresence) throws Exception {
		try {
			Presence presence = presenceRepository.findByIdAndCours(idPresence, cours) ;
			return presence ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Presence rechercher (Evaluation evaluation, Long idPresence) throws Exception {
		try {
			Presence presence = presenceRepository.findByIdAndEvaluation(idPresence, evaluation) ;
			return presence ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Presence> rechercher (Presence presence) throws Exception {
		try {
			Iterable<Presence> source = presenceRepository.findAll() ;
			List<Presence> cible = new ArrayList<Presence>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Presence> rechercher (Cours  cours) throws Exception {
		try {
			List<Presence> liste = presenceRepository.findByCours(cours) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Presence> rechercher (Evaluation evaluation) throws Exception {
		try {
			List<Presence> liste = presenceRepository.findByEvaluation(evaluation) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
}
