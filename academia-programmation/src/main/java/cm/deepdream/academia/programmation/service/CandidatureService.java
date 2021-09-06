package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.CandidatureRepository;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Candidature;
import cm.deepdream.academia.souscription.data.Etablissement;

@Service
@Transactional
public class CandidatureService {
	private Logger logger = Logger.getLogger(CandidatureService.class.getName()) ;
	@Autowired
	private CandidatureRepository candidatureRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	public Candidature creer (Candidature candidature) throws Exception {
		try {
			candidature.setId(sequenceDAO.nextGlobalId(Candidature.class.getName()));
			candidature.setId(sequenceDAO.nextId(candidature.getEtablissement(), Candidature.class.getName())) ;
			candidature.setDateCreation(LocalDateTime.now());
			candidature.setDateDernMaj(LocalDateTime.now());
			candidatureRepository.save(candidature) ;
			return candidature ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}	
	
	
	
	public Candidature modifier (Candidature candidature) throws Exception {
		try {
			candidature.setDateDernMaj(LocalDateTime.now());
			candidatureRepository.save(candidature) ;
			return candidature ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (Candidature candidature) throws Exception {
		try {
			candidatureRepository.delete(candidature) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (Long idCandidature) throws Exception {
		try {
			Optional<Candidature> optCandidature = candidatureRepository.findById(idCandidature) ;
			if(optCandidature.isPresent())
				candidatureRepository.delete(optCandidature.get()) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	
	public Candidature rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Candidature candidature = candidatureRepository.findByIdAndEtablissement(id, etablissement) ;
			return candidature ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public List<Candidature> rechercher(Candidature candidature) throws Exception {
		try {
			Iterable<Candidature> itCandidatures = candidatureRepository.findAll() ;
			List<Candidature> listeCandidatures = new ArrayList() ;
			itCandidatures.forEach(listeCandidatures::add);
			return listeCandidatures ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Candidature> rechercher(Etablissement etablissement) throws Exception {
		try {
			List<Candidature> liste = candidatureRepository.findByEtablissement(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Candidature> rechercher(Etablissement etablissement, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<Candidature> liste = candidatureRepository.findByEtablissementAndAnneeScolaire(etablissement, anneeScolaire) ;
			return liste ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Candidature> rechercherTout (Candidature candidature) throws Exception {
		try {
			Iterable<Candidature> itCandidatures = candidatureRepository.findAll() ;
			List<Candidature> listeCandidatures = new ArrayList() ;
			itCandidatures.forEach(listeCandidatures::add);
			return listeCandidatures ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
}
