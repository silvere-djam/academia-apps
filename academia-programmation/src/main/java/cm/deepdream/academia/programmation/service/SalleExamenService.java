package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.deepdream.academia.programmation.repository.CentreExamenRepository;
import cm.deepdream.academia.programmation.repository.SalleExamenRepository;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.SalleExamen;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class SalleExamenService {
	private Logger logger = Logger.getLogger(SalleExamenService.class.getName()) ;
	@Autowired
	private SalleExamenRepository salleExamenRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private DocumentProducer producer ;
	@Autowired
	private CentreExamenRepository centreExamenRepository ;
	
	
	
	public SalleExamen creer (SalleExamen salleExamen) throws Exception{
		try {
			salleExamen.setId(sequenceDAO.nextGlobalId(SalleExamen.class.getName()));
			salleExamen.setNum(sequenceDAO.nextId(salleExamen.getEtablissement(), SalleExamen.class.getName()));
			salleExamen.setDateCreation(LocalDateTime.now());
			salleExamen.setDateDernMaj(LocalDateTime.now());
			SalleExamen salleExamenCree = salleExamenRepository.save(salleExamen) ;
			
			Etablissement etablissement = salleExamen.getEtablissement() ;
			
			CentreExamen centreExamen = centreExamenRepository.findByIdAndEtablissement(salleExamen.getCentreExamen().getId(), etablissement) ;
			centreExamen.setNbreSalles(salleExamenRepository.countByEtablissementAndCentreExamen(etablissement, centreExamen));
			CentreExamen centreExamenModifie = centreExamenRepository.save(centreExamen) ;
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(salleExamenCree), 
					SalleExamen.class.getName()));
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(centreExamenModifie), 
					CentreExamen.class.getName()));
			return salleExamen ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public SalleExamen modifier (SalleExamen salleExamen) throws Exception{
		try {
			salleExamen.setDateDernMaj(LocalDateTime.now());
			SalleExamen salleExamenModifiee = salleExamenRepository.save(salleExamen) ;
			
			Etablissement etablissement = salleExamen.getEtablissement() ;
			
			CentreExamen centreExamen = centreExamenRepository.findByIdAndEtablissement(salleExamen.getCentreExamen().getId(), etablissement) ;
			centreExamen.setNbreSalles(salleExamenRepository.countByEtablissementAndCentreExamen(etablissement, centreExamen));
			CentreExamen centreExamenModifie = centreExamenRepository.save(centreExamen) ;
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(salleExamenModifiee), 
					SalleExamen.class.getName()));
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(centreExamenModifie), 
					CentreExamen.class.getName()));
			return salleExamenModifiee ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public void supprimer (Long idSalleExamen) throws Exception {
		try {
			SalleExamen salleExamen = salleExamenRepository.findById(idSalleExamen).orElseThrow(Exception::new) ;
			salleExamenRepository.delete(salleExamen) ;
			
			Etablissement etablissement = salleExamen.getEtablissement() ;
			
			CentreExamen centreExamen = centreExamenRepository.findByIdAndEtablissement(salleExamen.getCentreExamen().getId(), etablissement) ;
			centreExamen.setNbreSalles(salleExamenRepository.countByEtablissementAndCentreExamen(etablissement, centreExamen));
			CentreExamen centreExamenModifie = centreExamenRepository.save(centreExamen) ;
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(centreExamenModifie), 
					CentreExamen.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public void supprimer (SalleExamen salleExamen) throws Exception {
		try {
			salleExamenRepository.delete(salleExamen) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	

	public SalleExamen rechercher (Etablissement etablissement, Long idSalleExamen) throws Exception {
		try {
			SalleExamen salleExamen = salleExamenRepository.findByIdAndEtablissement(idSalleExamen, etablissement) ;
			return salleExamen ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	

	public List<SalleExamen> rechercher (Etablissement etablissement, CentreExamen centreExamen) throws Exception {
		try {
			List<SalleExamen> liste = salleExamenRepository.findByEtablissementAndCentreExamen(etablissement, centreExamen) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<SalleExamen> rechercher (Etablissement etablissement, Examen examen) throws Exception {
		try {
			List<SalleExamen> liste = salleExamenRepository.findByEtablissementAndExamen(etablissement, examen) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<SalleExamen> rechercher (SalleExamen salleExamen) throws Exception {
		try {
			Iterable<SalleExamen> source = salleExamenRepository.findAll() ;
			List<SalleExamen> cible = new ArrayList<SalleExamen>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	

}
