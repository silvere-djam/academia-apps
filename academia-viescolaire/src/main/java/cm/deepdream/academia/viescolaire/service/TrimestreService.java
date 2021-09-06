package cm.deepdream.academia.viescolaire.service;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.viescolaire.dao.SequenceDAO;
import cm.deepdream.academia.viescolaire.repository.TrimestreRepository;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Trimestre;
@Transactional
@Service
public class TrimestreService {
	private Logger logger = Logger.getLogger(TrimestreService.class.getName()) ;
	@Autowired
	private TrimestreRepository trimestreRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	
	public Trimestre creer (Trimestre trimestre) throws Exception{
		try {
			trimestre.setId(System.currentTimeMillis());
			trimestre.setNum(sequenceDAO.nextId(trimestre.getEtablissement(), Trimestre.class.getName()));
			trimestre.setDateCreation(LocalDateTime.now());
			trimestre.setDateDernMaj(LocalDateTime.now());
			trimestreRepository.save(trimestre) ;
			return trimestre ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Trimestre modifier (Trimestre trimestre) throws Exception{
		try {
			trimestre.setDateDernMaj(LocalDateTime.now());
			trimestreRepository.save(trimestre) ;
			return trimestre ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idTrimestre) throws Exception {
		try {
			Trimestre trimestre = trimestreRepository.findById(idTrimestre).orElseThrow(Exception::new) ;
			trimestreRepository.delete(trimestre) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Trimestre trimestre) throws Exception{
		try {
			trimestreRepository.delete(trimestre) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Trimestre rechercher (long id) throws Exception {
		try {
			Trimestre trimestre = trimestreRepository.findById(id).get();
			return trimestre ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Trimestre rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Trimestre trimestre = trimestreRepository.findByIdAndEtablissement(id, etablissement);
			return trimestre ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Trimestre> rechercher (Trimestre trimestre) throws Exception {
		try {
			Iterable<Trimestre> source = trimestreRepository.findAll() ;
			List<Trimestre> cible = new ArrayList<Trimestre>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Trimestre> rechercher (Etablissement etablissement, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<Trimestre> liste = trimestreRepository.findByEtablissementAndAnneeScolaire(etablissement, anneeScolaire) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
}
