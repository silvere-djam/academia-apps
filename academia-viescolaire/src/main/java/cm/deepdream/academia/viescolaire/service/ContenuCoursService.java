package cm.deepdream.academia.viescolaire.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.viescolaire.repository.ContenuCoursRepository;
import cm.deepdream.academia.viescolaire.repository.CoursRepository;
import cm.deepdream.academia.viescolaire.repository.EvaluationRepository;
import cm.deepdream.academia.viescolaire.dao.SequenceDAO;
import cm.deepdream.academia.viescolaire.data.ContenuCours;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.viescolaire.data.Evaluation;
import cm.deepdream.academia.souscription.data.Etablissement;
@Transactional
@Service
public class ContenuCoursService {
	private Logger logger = Logger.getLogger(ContenuCoursService.class.getName()) ;
	@Autowired
	private ContenuCoursRepository contenuCoursRepository ;
	@Autowired
	private CoursRepository coursRepository ;
	@Autowired
	private EvaluationRepository evaluationRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	public ContenuCours editer (ContenuCours contenuCours) throws Exception{
		try {
			if(contenuCours.getId() == null) {
				contenuCours.setId(sequenceDAO.nextGlobalId(ContenuCours.class.getName())) ;
				contenuCours.setNum(sequenceDAO.nextId(contenuCours.getEtablissement(), ContenuCours.class.getName()));
				contenuCours.setDateCreation(LocalDateTime.now()) ;
			}
			
			if(contenuCours.getCours() != null) {
				Cours cours = coursRepository.findByIdAndEtablissement(contenuCours.getCours().getId(), contenuCours.getEtablissement()) ;
				contenuCours.setDateCours(cours.getDate());
			} 
			contenuCours.setDateDernMaj(LocalDateTime.now()) ;
			contenuCoursRepository.save(contenuCours) ;
			return contenuCours ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public ContenuCours modifier (ContenuCours contenuCours) throws Exception{
		try {
			contenuCours.setDateDernMaj(LocalDateTime.now()) ;
			contenuCoursRepository.save(contenuCours) ;
			return contenuCours ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Long idContenuCours) throws Exception {
		try {
			ContenuCours contenuCours = contenuCoursRepository.findById(idContenuCours).orElseThrow(Exception::new) ;
			contenuCoursRepository.delete(contenuCours) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (ContenuCours contenuCours) throws Exception{
		try {
			logger.log(Level.INFO, "Suppression de l'contenuCours "+contenuCours);
			contenuCoursRepository.delete(contenuCours) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public ContenuCours rechercher (long id) throws Exception {
		try {
			ContenuCours contenuCours = contenuCoursRepository.findById(id).orElseThrow(Exception::new) ;
			return contenuCours ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public ContenuCours rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			ContenuCours contenuCours = contenuCoursRepository.findByIdAndEtablissement(id, etablissement);
			return contenuCours ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<ContenuCours> rechercher (ContenuCours contenuCours) throws Exception {
		try {
			Iterable<ContenuCours> source = contenuCoursRepository.findAll() ;
			List<ContenuCours> cible = new ArrayList<ContenuCours>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<ContenuCours> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<ContenuCours> liste = contenuCoursRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<ContenuCours> rechercher (Cours cours) throws Exception {
		try {
			List<ContenuCours> liste = contenuCoursRepository.findByCours(cours) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
