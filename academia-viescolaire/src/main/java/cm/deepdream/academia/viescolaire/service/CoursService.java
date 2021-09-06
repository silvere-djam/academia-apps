package cm.deepdream.academia.viescolaire.service;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.viescolaire.repository.CoursRepository;
import cm.deepdream.academia.viescolaire.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.viescolaire.data.Activite;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.viescolaire.enums.StatutC;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.viescolaire.util.SerializerToolkit;
import cm.deepdream.academia.viescolaire.producer.DocumentProducer;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Trimestre;
@Transactional
@Service
public class CoursService {
	private Logger logger = Logger.getLogger(CoursService.class.getName()) ;
	@Autowired
	private CoursRepository coursRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private DocumentProducer producer ;
	
	
	
	public Cours creer (Cours cours) throws Exception{
		try {
			logger.log(Level.INFO, "Lancement de l'ajout du cours") ;
			cours.setId(sequenceDAO.nextGlobalId(Activite.class.getName()));
			cours.setNum(sequenceDAO.nextId(cours.getEtablissement(), Activite.class.getName()));
			cours.setDateCreation(LocalDateTime.now());
			cours.setDateDernMaj(LocalDateTime.now());
			cours.setStatut(StatutC.En_Cours.name());
			Cours coursCree = coursRepository.save(cours) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(coursCree), 
					Cours.class.getName()));
			return cours ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Cours modifier (Cours cours) throws Exception{
		try {
			cours.setDateDernMaj(LocalDateTime.now());
			
			Cours coursMaj = coursRepository.save(cours) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(coursMaj), 
					Cours.class.getName()));
			return coursMaj ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idCours) throws Exception {
		try {
			Cours cours = coursRepository.findById(idCours).orElseThrow(Exception::new) ;
			coursRepository.delete(cours) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Cours cours) throws Exception{
		try {
			coursRepository.delete(cours) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Cours rechercher (Long idCours) throws Exception {
		try {
			Cours cours = coursRepository.findById(idCours).orElseThrow(Exception::new) ;
			return cours ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Cours rechercher (Etablissement etablissement, Long idCours) throws Exception {
		try {
			Cours cours = coursRepository.findByIdAndEtablissement(idCours, etablissement) ;
			return cours ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Cours> rechercher (Cours cours) throws Exception {
		try {
			Iterable<Cours> source = coursRepository.findAll() ;
			List<Cours> cible = new ArrayList<Cours>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Cours> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Cours> liste = coursRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Cours> rechercher (Etablissement etablissement, Classe classe, Trimestre trimestre) throws Exception {
		try {
			List<Cours> liste = coursRepository.findByEtablissementAndClasseAndTrimestre(etablissement, classe, trimestre) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Cours> rechercher (Etablissement etablissement, Classe classe, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<Cours> liste = coursRepository.findByEtablissementAndClasseAndAnneeScolaire(etablissement, classe, anneeScolaire) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Cours> rechercher (Etablissement etablissement, UE ue, Trimestre trimestre) throws Exception {
		try {
			List<Cours> liste = coursRepository.findByEtablissementAndUeAndTrimestre(etablissement, ue, trimestre) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
