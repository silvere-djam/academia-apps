package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.deepdream.academia.programmation.repository.DetailEmploiTempsRepository;
import cm.deepdream.academia.programmation.repository.EmploiTempsRepository;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.DetailEmploiTemps;
import cm.deepdream.academia.programmation.data.EmploiTemps;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class EmploiTempsService {
	private Logger logger = Logger.getLogger(EmploiTempsService.class.getName()) ;
	@Autowired
	private EmploiTempsRepository emploiTempsRepository ;
	@Autowired
	private DetailEmploiTempsRepository detailEmploiTempsRepository ;
	@Autowired
	private DocumentProducer producer ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	
	public EmploiTemps creer (EmploiTemps emploiTemps) throws Exception{
		try {
			emploiTemps.setId(sequenceDAO.nextGlobalId(EmploiTemps.class.getName()));
			emploiTemps.setNum(sequenceDAO.nextId(emploiTemps.getEtablissement(), EmploiTemps.class.getName()));
			emploiTemps.setDateCreation(LocalDateTime.now());
			emploiTemps.setDateDernMaj(LocalDateTime.now());
			EmploiTemps emploiTempsCree = emploiTempsRepository.save(emploiTemps) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(emploiTempsCree), 
					EmploiTemps.class.getName()));
			return emploiTemps ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public EmploiTemps modifier (EmploiTemps emploiTemps) throws Exception{
		try {
			emploiTemps.setDateDernMaj(LocalDateTime.now());
			EmploiTemps emploiTempsModifie = emploiTempsRepository.save(emploiTemps) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(emploiTempsModifie), 
					EmploiTemps.class.getName()));
			return emploiTemps ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idEmploiTemps) throws Exception {
		try {
			EmploiTemps emploiTemps = emploiTempsRepository.findById(idEmploiTemps).orElseThrow(Exception::new) ;
			emploiTempsRepository.delete(emploiTemps) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (EmploiTemps emploiTemps) throws Exception{
		try {
			emploiTempsRepository.delete(emploiTemps) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public EmploiTemps rechercher (Etablissement etablissement, long id) throws Exception {
		try {
			EmploiTemps emploiTemps = emploiTempsRepository.findByIdAndEtablissement(id, etablissement) ;
			return emploiTemps ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<EmploiTemps> rechercher (EmploiTemps emploiTemps) throws Exception {
		try {
			Iterable<EmploiTemps> source = emploiTempsRepository.findAll() ;
			List<EmploiTemps> cible = new ArrayList<EmploiTemps>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<EmploiTemps> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<EmploiTemps> liste = emploiTempsRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<DetailEmploiTemps> rechercherDetails (Etablissement etablissement, EmploiTemps emploiTemps) throws Exception {
		try {
			List<DetailEmploiTemps> listeDetails = detailEmploiTempsRepository.findByEtablissementAndEmploiTemps(etablissement, emploiTemps) ;
			return listeDetails ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<EmploiTemps> rechercher (Etablissement etablissement, Classe classe) throws Exception {
		try {
			List<EmploiTemps> liste = emploiTempsRepository.findByEtablissementAndClasse(etablissement, classe) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<EmploiTemps> rechercher (Etablissement etablissement, Classe classe, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<EmploiTemps> liste = emploiTempsRepository.findByEtablissementAndClasseAndAnneeScolaire(etablissement, classe, anneeScolaire) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<EmploiTemps> rechercher (Etablissement etablissement, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<EmploiTemps> liste = emploiTempsRepository.findByEtablissementAndAnneeScolaire(etablissement, anneeScolaire) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<EmploiTemps> rechercher (Etablissement etablissement, Enseignant enseignant) throws Exception {
		try {
			List<EmploiTemps> liste = emploiTempsRepository.findByEtablissementAndEnseignantPrincipal(etablissement, enseignant) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<EmploiTemps> rechercher (Etablissement etablissement, Classe classe, Enseignant enseignant) throws Exception {
		try {
			List<EmploiTemps> liste = emploiTempsRepository.findByEtablissementAndClasseAndEnseignantPrincipal(etablissement, classe, enseignant) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
}
