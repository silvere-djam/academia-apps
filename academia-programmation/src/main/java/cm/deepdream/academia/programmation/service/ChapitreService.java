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
import cm.deepdream.academia.programmation.repository.ChapitreRepository;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.Chapitre;
import cm.deepdream.academia.programmation.data.Programme;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Etablissement;
@Transactional
@Service
public class ChapitreService {
	private Logger logger = Logger.getLogger(ChapitreService.class.getName()) ;
	@Autowired
	private ChapitreRepository chapitreRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	public Chapitre creer (Chapitre chapitre) throws Exception {
		try {
			chapitre.setId(sequenceDAO.nextGlobalId(Chapitre.class.getName()));
			chapitre.setNum(sequenceDAO.nextId(chapitre.getEtablissement(), Chapitre.class.getName()));
			chapitre.setDateCreation(LocalDateTime.now());
			chapitre.setDateDernMaj(LocalDateTime.now());
			chapitreRepository.save(chapitre) ;
			return chapitre ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}	
	
	public Chapitre modifier (Chapitre chapitre) throws Exception {
		try {
			chapitre.setDateDernMaj(LocalDateTime.now());
			chapitreRepository.save(chapitre) ;
			return chapitre ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (Chapitre chapitre) throws Exception {
		try {
			chapitreRepository.delete(chapitre) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (Long idChapitre) throws Exception {
		try {
			Optional<Chapitre> optChapitre = chapitreRepository.findById(idChapitre) ;
			if(optChapitre.isPresent())
				chapitreRepository.delete(optChapitre.get()) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public Chapitre rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Chapitre chapitre = chapitreRepository.findByIdAndEtablissement(id, etablissement) ;
			 return chapitre ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Chapitre> rechercher(Etablissement etablissement) throws Exception {
		try {
			List<Chapitre> listeChapitres = chapitreRepository.findByEtablissement(etablissement) ;
			return listeChapitres ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Chapitre> rechercher(Etablissement etablissement, Programme programme) throws Exception {
		try {
			List<Chapitre> listeChapitres = chapitreRepository.findByEtablissementAndProgramme(etablissement, programme) ;
			return listeChapitres ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Chapitre> rechercher(Etablissement etablissement, Programme programme, Trimestre trimestre) throws Exception {
		try {
			List<Chapitre> listeChapitres = chapitreRepository.findByEtablissementAndProgrammeAndTrimestre(etablissement, programme,
					trimestre) ;
			return listeChapitres ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Chapitre> rechercher(Etablissement etablissement, Programme programme, Semestre semestre) throws Exception {
		try {
			List<Chapitre> listeChapitres = chapitreRepository.findByEtablissementAndProgrammeAndSemestre(etablissement, programme,
					semestre) ;
			return listeChapitres ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Chapitre> rechercherTout (Chapitre chapitre) throws Exception {
		try {
			Iterable<Chapitre> itChapitres = chapitreRepository.findAll() ;
			List<Chapitre> listeChapitres = new ArrayList() ;
			itChapitres.forEach(listeChapitres::add);
			return listeChapitres ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
}
