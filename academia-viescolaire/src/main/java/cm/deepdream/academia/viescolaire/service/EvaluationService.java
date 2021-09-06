package cm.deepdream.academia.viescolaire.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.deepdream.academia.viescolaire.repository.EvaluationRepository;
import cm.deepdream.academia.viescolaire.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.viescolaire.util.SerializerToolkit;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.viescolaire.data.Activite;
import cm.deepdream.academia.viescolaire.data.Evaluation;
import cm.deepdream.academia.viescolaire.enums.StatutC;
import cm.deepdream.academia.viescolaire.producer.DocumentProducer;
@Transactional
@Service
public class EvaluationService {
	private Logger logger = Logger.getLogger(EvaluationService.class.getName()) ;
	@Autowired
	private EvaluationRepository evaluationRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private DocumentProducer producer ;
	
	
	
	public Evaluation creer (Evaluation evaluation) throws Exception{
		try {
			evaluation.setId(sequenceDAO.nextGlobalId(Activite.class.getName()));
			evaluation.setNum(sequenceDAO.nextId(evaluation.getEtablissement(), Activite.class.getName()));
			evaluation.setDateCreation(LocalDateTime.now());
			evaluation.setDateDernMaj(LocalDateTime.now());
			evaluation.setStatut(StatutC.En_Cours.name());
			Evaluation evaluationCree = evaluationRepository.save(evaluation) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(evaluationCree), 
					Evaluation.class.getName()));
			return evaluation ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Evaluation modifier (Evaluation evaluation) throws Exception{
		try {
			evaluation.setDateDernMaj(LocalDateTime.now());
			Evaluation evaluationMaj = evaluationRepository.save(evaluation) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(evaluationMaj), 
					Evaluation.class.getName()));
			return evaluationMaj ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public void supprimer (Long idEvaluation) throws Exception {
		try {
			Evaluation evaluation = evaluationRepository.findById(idEvaluation).orElseThrow(Exception::new) ;
			evaluationRepository.delete(evaluation) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Evaluation evaluation) throws Exception{
		try {
			evaluationRepository.delete(evaluation) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Evaluation rechercher (Etablissement etablissement, long idEvaluation) throws Exception {
		try {
			Evaluation evaluation = evaluationRepository.findByIdAndEtablissement(idEvaluation, etablissement) ;
			return evaluation ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Evaluation rechercher (long id) throws Exception {
		try {
			Evaluation evaluation = evaluationRepository.findById(id).get() ;
			return evaluation ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Evaluation> rechercher (Etablissement etablissement, Classe classe, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<Evaluation> liste = evaluationRepository.findByEtablissementAndClasseAndAnneeScolaire(etablissement, classe, anneeScolaire) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Evaluation> rechercher (Evaluation evaluation) throws Exception {
		try {
			Iterable<Evaluation> source = evaluationRepository.findAll() ;
			List<Evaluation> cible = new ArrayList<Evaluation>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Evaluation> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Evaluation> liste = evaluationRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
