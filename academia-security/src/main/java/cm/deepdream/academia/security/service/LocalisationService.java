package cm.deepdream.academia.security.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.security.dao.SequenceDAO;
import cm.deepdream.academia.security.repository.LocalisationRepository;
import cm.deepdream.academia.security.repository.UtilisateurRepository;
import cm.deepdream.academia.security.data.Localisation;
import cm.deepdream.academia.security.data.Session;
import cm.deepdream.academia.security.data.Utilisateur;
@Transactional
@Service
public class LocalisationService {
	private Logger logger = Logger.getLogger(LocalisationService.class.getName()) ;
	@Autowired
	private LocalisationRepository localisationRepository ;
	@Autowired
	private UtilisateurRepository utilisateurRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	public Localisation creer (Localisation localisation) throws Exception {
		try {
			localisation.setId(sequenceDAO.nextGlobalId(Localisation.class.getName())) ;
			localisation.setNum(sequenceDAO.nextId(localisation.getEtablissement(), Localisation.class.getName())) ;
			localisationRepository.save(localisation) ;
			return localisation ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}	
	
	public Localisation modifier (Localisation localisation) throws Exception {
		try {
			localisationRepository.save(localisation) ;
			return localisation ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (Localisation localisation) throws Exception {
		try {
			localisationRepository.delete(localisation) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (long idLocalisation) throws Exception {
		try {
			Optional<Localisation> optLocalisation = localisationRepository.findById(idLocalisation) ;
			if(optLocalisation.isPresent())
				localisationRepository.delete(optLocalisation.get()) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public List<Localisation> rechercher(Utilisateur utilisateur) throws Exception {
		try {
			List<Localisation> listeLocalisations = localisationRepository.findByUtilisateur(utilisateur) ;
			return listeLocalisations ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Localisation> rechercher (Localisation localisation) throws Exception {
		try {
			Iterable<Localisation> itLocalisations = localisationRepository.findAll() ;
			List<Localisation> listeLocalisations = new ArrayList() ;
			itLocalisations.forEach(listeLocalisations::add);
			return listeLocalisations ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
}
