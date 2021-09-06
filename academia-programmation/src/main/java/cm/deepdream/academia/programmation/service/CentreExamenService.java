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
import cm.deepdream.academia.programmation.repository.SalleExamenRepository;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class CentreExamenService {
	private Logger logger = Logger.getLogger(CentreExamenService.class.getName()) ;
	@Autowired
	private CentreExamenRepository centreExamenRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private DocumentProducer producer ;
	@Autowired
	private ExamenRepository examenRepository ;
	@Autowired
	private CandidatRepository candidatRepository ;
	@Autowired
	private SalleExamenRepository salleExamenRepository ;
	
	
	public CentreExamen creer (CentreExamen centreExamen) throws Exception{
		try {
			centreExamen.setId(sequenceDAO.nextGlobalId(CentreExamen.class.getName()));
			centreExamen.setNum(sequenceDAO.nextId(centreExamen.getEtablissement(), CentreExamen.class.getName()));
			centreExamen.setDateCreation(LocalDateTime.now());
			centreExamen.setDateDernMaj(LocalDateTime.now());
			CentreExamen centreExamenCree = centreExamenRepository.save(centreExamen) ;
			
			Etablissement etablissement = centreExamen.getEtablissement() ;
			Examen examen = examenRepository.findByIdAndEtablissement(centreExamen.getExamen().getId(), etablissement) ;
			examen.setNbreCentres(centreExamenRepository.countByEtablissementAndExamen(etablissement, examen));
			Examen examenModifie = examenRepository.save(examen) ;
			
			producer.publier(new Document(Action.Nouveau.toString(), 
					SerializerToolkit.getToolkit().serialize(centreExamenCree), 
					CentreExamen.class.getName()));
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(examenModifie), 
					Examen.class.getName()));
			return centreExamen ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public CentreExamen modifier (CentreExamen centreExamen) throws Exception{
		try {
			centreExamen.setDateDernMaj(LocalDateTime.now());
			Etablissement etablissement = centreExamen.getEtablissement() ;
			
			centreExamen.setNbreCandidats(candidatRepository.countByEtablissementAndCentreExamen(etablissement, centreExamen));
			centreExamen.setNbreSalles(salleExamenRepository.countByEtablissementAndCentreExamen(etablissement, centreExamen));
			CentreExamen centreExamenModifie = centreExamenRepository.save(centreExamen) ;
			
			Examen examen = examenRepository.findByIdAndEtablissement(centreExamen.getExamen().getId(), etablissement) ;
			examen.setNbreCentres(centreExamenRepository.countByEtablissementAndExamen(etablissement, examen));
			Examen examenModifie = examenRepository.save(examen) ;
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(centreExamenModifie), 
					CentreExamen.class.getName()));
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(examenModifie), 
					Examen.class.getName()));

			return centreExamenModifie ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public void supprimer (Long idCentreExamen) throws Exception {
		try {
			CentreExamen centreExamen = centreExamenRepository.findById(idCentreExamen).orElseThrow(Exception::new) ;
			centreExamenRepository.delete(centreExamen) ;
			
			Etablissement etablissement = centreExamen.getEtablissement() ;
			Examen examen = examenRepository.findByIdAndEtablissement(centreExamen.getExamen().getId(), etablissement) ;
			examen.setNbreCentres(centreExamenRepository.countByEtablissementAndExamen(etablissement, examen));
			Examen examenModifie = examenRepository.save(examen) ;
			
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(centreExamen), 
					CentreExamen.class.getName()));
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(examenModifie), 
					Examen.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public void supprimer (CentreExamen centreExamen) throws Exception{
		try {
			centreExamenRepository.delete(centreExamen) ;
			
			Etablissement etablissement = centreExamen.getEtablissement() ;
			Examen examen = examenRepository.findByIdAndEtablissement(centreExamen.getExamen().getId(), etablissement) ;
			examen.setNbreCentres(centreExamenRepository.countByEtablissementAndExamen(etablissement, examen));
			Examen examenModifie = examenRepository.save(examen) ;
			
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(centreExamen), 
					CentreExamen.class.getName()));
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(examenModifie), 
					Examen.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	
	public CentreExamen rechercher (Etablissement etablissement, long idCentreExamen) throws Exception {
		try {
			CentreExamen centreExamen = centreExamenRepository.findByIdAndEtablissement(idCentreExamen, etablissement) ;
			return centreExamen ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public CentreExamen rechercher (Long id) throws Exception {
		try {
			CentreExamen centreExamen = centreExamenRepository.findById(id).get() ;
			return centreExamen ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<CentreExamen> rechercher (Etablissement etablissement, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<CentreExamen> liste = centreExamenRepository.findByEtablissementAndAnneeScolaire(etablissement, anneeScolaire) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<CentreExamen> rechercher (Etablissement etablissement, Examen examen) throws Exception {
		try {
			List<CentreExamen> liste = centreExamenRepository.findByEtablissementAndExamen(etablissement, examen) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<CentreExamen> rechercher (CentreExamen centreExamen) throws Exception {
		try {
			Iterable<CentreExamen> source = centreExamenRepository.findAll() ;
			List<CentreExamen> cible = new ArrayList<CentreExamen>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	

}
