package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.CandidatRepository;
import cm.deepdream.academia.programmation.repository.CentreExamenRepository;
import cm.deepdream.academia.programmation.repository.ExamenRepository;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class ExamenService {
	private Logger logger = Logger.getLogger(ExamenService.class.getName()) ;
	@Autowired
	private ExamenRepository examenRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private DocumentProducer producer ;
	@Autowired
	private CandidatRepository candidatRepository ;
	@Autowired
	private CentreExamenRepository centreExamenRepository ;
	
	
	
	public Examen creer (Examen examen) throws Exception{
		try {
			examen.setId(sequenceDAO.nextGlobalId(Examen.class.getName()));
			examen.setNum(sequenceDAO.nextId(examen.getEtablissement(), Examen.class.getName()));
			examen.setDateCreation(LocalDateTime.now());
			examen.setDateDernMaj(LocalDateTime.now());
			examen.setNbreCandidats(0L) ;
			examen.setNbreCentres(0L) ;
			Examen examenCree = examenRepository.save(examen) ;
			producer.publier(new Document(Action.Nouveau.toString(), 
					SerializerToolkit.getToolkit().serialize(examenCree), 
					Examen.class.getName()));
			return examen ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Examen modifier (Examen examen) throws Exception{
		try {
			examen.setDateDernMaj(LocalDateTime.now()) ;
			Etablissement etablissement = examen.getEtablissement() ;
			examen.setNbreCandidats(candidatRepository.countByEtablissementAndExamen(etablissement, examen));
			examen.setNbreCentres(centreExamenRepository.countByEtablissementAndExamen(etablissement, examen));
			Examen examenModifie = examenRepository.save(examen) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(examenModifie), 
					Examen.class.getName()));
			return examenModifie ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	
	public void supprimer (Long idExamen) throws Exception {
		try {
			Examen examen = examenRepository.findById(idExamen).orElseThrow(Exception::new) ;
			examenRepository.delete(examen) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(examen), 
					Examen.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Examen examen) throws Exception{
		try {
			examenRepository.delete(examen) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(examen), 
					Examen.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Examen rechercher (Etablissement etablissement, long idExamen) throws Exception {
		try {
			Examen examen = examenRepository.findByIdAndEtablissement(idExamen, etablissement) ;
			return examen ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Examen rechercher (Long id) throws Exception {
		try {
			Examen examen = examenRepository.findById(id).get() ;
			return examen ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Examen> rechercher (Etablissement etablissement, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<Examen> liste = examenRepository.findByEtablissementAndAnneeScolaire(etablissement, anneeScolaire) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Examen> rechercher (Etablissement etablissement, Trimestre trimestre) throws Exception {
		try {
			List<Examen> liste = examenRepository.findByEtablissementAndTrimestre(etablissement, trimestre) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Examen> rechercher (Etablissement etablissement, Semestre semestre) throws Exception {
		try {
			List<Examen> liste = examenRepository.findByEtablissementAndSemestre(etablissement, semestre) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Examen> rechercher (Examen examen) throws Exception {
		try {
			Iterable<Examen> source = examenRepository.findAll() ;
			List<Examen> cible = new ArrayList<Examen>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	

}
