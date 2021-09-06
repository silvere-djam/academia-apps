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
import cm.deepdream.academia.viescolaire.repository.SemestreRepository;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Semestre;
@Transactional
@Service
public class SemestreService {
	private Logger logger = Logger.getLogger(SemestreService.class.getName()) ;
	@Autowired
	private SemestreRepository semestreRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	
	public Semestre creer (Semestre semestre) throws Exception{
		try {
			semestre.setId(System.currentTimeMillis());
			semestre.setNum(sequenceDAO.nextId(semestre.getEtablissement(), Semestre.class.getName()));
			semestre.setDateCreation(LocalDateTime.now());
			semestre.setDateDernMaj(LocalDateTime.now());
			semestreRepository.save(semestre) ;
			return semestre ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Semestre modifier (Semestre semestre) throws Exception{
		try {
			semestre.setDateDernMaj(LocalDateTime.now());
			semestreRepository.save(semestre) ;
			return semestre ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idSemestre) throws Exception {
		try {
			Semestre semestre = semestreRepository.findById(idSemestre).orElseThrow(Exception::new) ;
			semestreRepository.delete(semestre) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Semestre semestre) throws Exception{
		try {
			semestreRepository.delete(semestre) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Semestre rechercher (long id) throws Exception {
		try {
			Semestre semestre = semestreRepository.findById(id).get();
			return semestre ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Semestre rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Semestre semestre = semestreRepository.findByIdAndEtablissement(id, etablissement);
			return semestre ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Semestre> rechercher (Semestre semestre) throws Exception {
		try {
			Iterable<Semestre> source = semestreRepository.findAll() ;
			List<Semestre> cible = new ArrayList<Semestre>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Semestre> rechercher (Etablissement etablissement, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<Semestre> liste = semestreRepository.findByEtablissementAndAnneeScolaire(etablissement, anneeScolaire) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
}
