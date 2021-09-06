package cm.deepdream.academia.viescolaire.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.viescolaire.repository.AbsenceRepository;
import cm.deepdream.academia.viescolaire.repository.CoursRepository;
import cm.deepdream.academia.viescolaire.repository.EvaluationRepository;
import cm.deepdream.academia.viescolaire.dao.SequenceDAO;
import cm.deepdream.academia.viescolaire.data.Absence;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.viescolaire.data.Evaluation;
import cm.deepdream.academia.souscription.data.Etablissement;
@Transactional
@Service
public class AbsenceService {
	private Logger logger = Logger.getLogger(AbsenceService.class.getName()) ;
	@Autowired
	private AbsenceRepository absenceRepository ;
	@Autowired
	private CoursRepository coursRepository ;
	@Autowired
	private EvaluationRepository evaluationRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	public Absence editer (Absence absence) throws Exception{
		try {
			if(absence.getId() == null) {
				absence.setId(sequenceDAO.nextGlobalId(Absence.class.getName())) ;
				absence.setNum(sequenceDAO.nextId(absence.getEtablissement(), Absence.class.getName()));
				absence.setDateCreation(LocalDateTime.now()) ;
			}
			
			if(absence.getCours() != null) {
				Cours cours = coursRepository.findByIdAndEtablissement(absence.getCours().getId(), absence.getEtablissement()) ;
				absence.setDateAbsence(cours.getDate());
			} else if(absence.getEvaluation() != null) {
				Evaluation evaluation = evaluationRepository.findByIdAndEtablissement(absence.getEvaluation().getId(), absence.getEtablissement()) ;
				absence.setDateAbsence(evaluation.getDate());
			}
			absence.setDateDernMaj(LocalDateTime.now()) ;
			absenceRepository.save(absence) ;
			return absence ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Absence modifier (Absence absence) throws Exception{
		try {
			absence.setDateDernMaj(LocalDateTime.now()) ;
			absenceRepository.save(absence) ;
			return absence ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idAbsence) throws Exception {
		try {
			Absence absence = absenceRepository.findById(idAbsence).orElseThrow(Exception::new) ;
			absenceRepository.delete(absence) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Absence absence) throws Exception{
		try {
			logger.log(Level.INFO, "Suppression de l'absence "+absence);
			absenceRepository.delete(absence) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Absence rechercher (long id) throws Exception {
		try {
			Absence absence = absenceRepository.findById(id).orElseThrow(Exception::new) ;
			return absence ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Absence rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Absence absence = absenceRepository.findByIdAndEtablissement(id, etablissement);
			return absence ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Absence> rechercher (Absence absence) throws Exception {
		try {
			Iterable<Absence> source = absenceRepository.findAll() ;
			List<Absence> cible = new ArrayList<Absence>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Absence> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Absence> liste = absenceRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Absence> rechercher (Cours cours) throws Exception {
		try {
			List<Absence> liste = absenceRepository.findByCours(cours) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
