package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.CandidatRepository;
import cm.deepdream.academia.programmation.repository.CentreExamenRepository;
import cm.deepdream.academia.programmation.repository.ExamenRepository;
import cm.deepdream.academia.programmation.repository.SalleExamenRepository;
import cm.deepdream.academia.programmation.util.FileStore;
import cm.deepdream.academia.programmation.util.LocalFileStore;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.Candidat;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.data.SalleExamen;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
import cm.deepdream.academia.souscription.data.Etablissement;

@Service
@Transactional
public class CandidatService {
	private Logger logger = Logger.getLogger(CandidatService.class.getName()) ;
	@Autowired
	private CandidatRepository candidatRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private FileStore fileStore ;
	@Autowired
	private LocalFileStore localFileStore ;
	@Value("${app.souscription.aws.bucketName}")
	private String bucketName ;
	@Autowired
	private DocumentProducer producer ;
	@Autowired
	private ExamenRepository examenRepository ;
	@Autowired 
	private CentreExamenRepository centreExamenRepository ;
	@Autowired
	private SalleExamenRepository salleExamenRepository ;
	
	
	public Candidat creer (Candidat candidat) throws Exception {
		try {
			candidat.setId(sequenceDAO.nextGlobalId(Candidat.class.getName()));
			candidat.setId(sequenceDAO.nextId(candidat.getEtablissement(), Candidat.class.getName())) ;
			candidat.setDateCreation(LocalDateTime.now());
			candidat.setDateDernMaj(LocalDateTime.now());
			Candidat candidatCree = candidatRepository.save(candidat) ;
			
			Etablissement etablissement = candidat.getEtablissement() ;
			
			Examen examen = examenRepository.findByIdAndEtablissement(candidat.getExamen().getId(), etablissement) ;
			examen.setNbreCandidats(candidatRepository.countByEtablissementAndExamen(etablissement, examen));
			Examen examenModifie = examenRepository.save(examen) ;
			
			CentreExamen centreExamen = centreExamenRepository.findByIdAndEtablissement(candidat.getCentreExamen().getId(), etablissement) ;
			centreExamen.setNbreCandidats(candidatRepository.countByEtablissementAndCentreExamen(etablissement, centreExamen));
			CentreExamen centreExamenModifie = centreExamenRepository.save(centreExamen) ;
					
			SalleExamen salleExamen = salleExamenRepository.findByIdAndEtablissement(candidat.getSalleExamen().getId(), etablissement) ;
			salleExamen.setNbCandidats(candidatRepository.countByEtablissementAndSalleExamen(etablissement, salleExamen));
			SalleExamen salleExamenModifiee = salleExamenRepository.save(salleExamen) ;
			
			producer.publier(new Document(Action.Nouveau.toString(), 
					SerializerToolkit.getToolkit().serialize(candidatCree), 
					Candidat.class.getName()));
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(examenModifie), 
					Examen.class.getName()));
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(centreExamenModifie), 
					CentreExamen.class.getName()));
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(salleExamenModifiee), 
					SalleExamen.class.getName()));
			return candidatCree ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}	
	
	
	
	public Candidat modifier (Candidat candidat) throws Exception {
		try {
			candidat.setDateDernMaj(LocalDateTime.now());
			Candidat candidatModifie = candidatRepository.save(candidat) ;
			
			Etablissement etablissement = candidat.getEtablissement() ;
			
			Examen examen = examenRepository.findByIdAndEtablissement(candidat.getExamen().getId(), etablissement) ;
			examen.setNbreCandidats(candidatRepository.countByEtablissementAndExamen(etablissement, examen));
			Examen examenModifie = examenRepository.save(examen) ;
			
			CentreExamen centreExamen = centreExamenRepository.findByIdAndEtablissement(candidat.getCentreExamen().getId(), etablissement) ;
			centreExamen.setNbreCandidats(candidatRepository.countByEtablissementAndCentreExamen(etablissement, centreExamen));
			CentreExamen centreExamenModifie = centreExamenRepository.save(centreExamen) ;
					
			SalleExamen salleExamen = salleExamenRepository.findByIdAndEtablissement(candidat.getSalleExamen().getId(), etablissement) ;
			salleExamen.setNbCandidats(candidatRepository.countByEtablissementAndSalleExamen(etablissement, salleExamen));
			SalleExamen salleExamenModifiee = salleExamenRepository.save(salleExamen) ;
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(candidatModifie), 
					Candidat.class.getName()));
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(examenModifie), 
					Examen.class.getName()));
			
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(centreExamenModifie), 
					CentreExamen.class.getName()));
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(salleExamenModifiee), 
					SalleExamen.class.getName()));
			return candidat ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	
	
	public void supprimer (Candidat candidat) throws Exception {
		try {
			candidatRepository.delete(candidat) ;
			
			Etablissement etablissement = candidat.getEtablissement() ;
			
			Examen examen = examenRepository.findByIdAndEtablissement(candidat.getExamen().getId(), etablissement) ;
			examen.setNbreCandidats(candidatRepository.countByEtablissementAndExamen(etablissement, examen));
			Examen examenModifie = examenRepository.save(examen) ;
			
			CentreExamen centreExamen = centreExamenRepository.findByIdAndEtablissement(candidat.getCentreExamen().getId(), etablissement) ;
			centreExamen.setNbreCandidats(candidatRepository.countByEtablissementAndCentreExamen(etablissement, centreExamen));
			CentreExamen centreExamenModifie = centreExamenRepository.save(centreExamen) ;
					
			SalleExamen salleExamen = salleExamenRepository.findByIdAndEtablissement(candidat.getSalleExamen().getId(), etablissement) ;
			salleExamen.setNbCandidats(candidatRepository.countByEtablissementAndSalleExamen(etablissement, salleExamen));
			SalleExamen salleExamenModifiee = salleExamenRepository.save(salleExamen) ;
			
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(candidat), 
					Candidat.class.getName()));
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(examenModifie), 
					Examen.class.getName()));
			
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(centreExamenModifie), 
					CentreExamen.class.getName()));
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(salleExamenModifiee), 
					SalleExamen.class.getName()));
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public void supprimer (Long idCandidat) throws Exception {
		try {
			Optional<Candidat> candidatOpt = candidatRepository.findById(idCandidat) ;
			if(candidatOpt.isPresent()) {
				Candidat candidat = candidatOpt.get() ;
				candidatRepository.delete(candidat) ;
				
				Etablissement etablissement = candidat.getEtablissement() ;
				
				Examen examen = examenRepository.findByIdAndEtablissement(candidat.getExamen().getId(), etablissement) ;
				examen.setNbreCandidats(candidatRepository.countByEtablissementAndExamen(etablissement, examen));
				Examen examenModifie = examenRepository.save(examen) ;
				
				CentreExamen centreExamen = centreExamenRepository.findByIdAndEtablissement(candidat.getCentreExamen().getId(), etablissement) ;
				centreExamen.setNbreCandidats(candidatRepository.countByEtablissementAndCentreExamen(etablissement, centreExamen));
				CentreExamen centreExamenModifie = centreExamenRepository.save(centreExamen) ;
						
				SalleExamen salleExamen = salleExamenRepository.findByIdAndEtablissement(candidat.getSalleExamen().getId(), etablissement) ;
				salleExamen.setNbCandidats(candidatRepository.countByEtablissementAndSalleExamen(etablissement, salleExamen));
				SalleExamen salleExamenModifiee = salleExamenRepository.save(salleExamen) ;
				
				producer.publier(new Document(Action.Delete.toString(), 
						SerializerToolkit.getToolkit().serialize(candidat), 
						Candidat.class.getName()));
				
				producer.publier(new Document(Action.Save.toString(), 
						SerializerToolkit.getToolkit().serialize(examenModifie), 
						Examen.class.getName()));
				
				
				producer.publier(new Document(Action.Save.toString(), 
						SerializerToolkit.getToolkit().serialize(centreExamenModifie), 
						CentreExamen.class.getName()));
				
				producer.publier(new Document(Action.Save.toString(), 
						SerializerToolkit.getToolkit().serialize(salleExamenModifiee), 
						SalleExamen.class.getName()));
			}
			
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	
	public Candidat rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Candidat candidat = candidatRepository.findByIdAndEtablissement(id, etablissement) ;
			return candidat ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public List<Candidat> rechercher(Candidat candidat) throws Exception {
		try {
			Iterable<Candidat> itCandidats = candidatRepository.findAll() ;
			List<Candidat> listeCandidats = new ArrayList() ;
			itCandidats.forEach(listeCandidats::add);
			return listeCandidats ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Candidat> rechercher(Etablissement etablissement) throws Exception {
		try {
			List<Candidat> liste = candidatRepository.findByEtablissement(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Candidat> rechercher(Etablissement etablissement, Examen examen) throws Exception {
		try {
			List<Candidat> liste = candidatRepository.findByEtablissementAndExamen(etablissement, examen) ;
			return liste ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public List<Candidat> rechercher(Etablissement etablissement, CentreExamen centreExamen) throws Exception {
		try {
			List<Candidat> liste = candidatRepository.findByEtablissementAndCentreExamen(etablissement, centreExamen) ;
			return liste ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public List<Candidat> rechercher(Etablissement etablissement, SalleExamen salleExamen) throws Exception {
		try {
			List<Candidat> liste = candidatRepository.findByEtablissementAndSalleExamen(etablissement, salleExamen) ;
			return liste ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public List<Candidat> rechercherTout (Candidat candidat) throws Exception {
		try {
			Iterable<Candidat> itCandidats = candidatRepository.findAll() ;
			List<Candidat> listeCandidats = new ArrayList() ;
			itCandidats.forEach(listeCandidats::add);
			return listeCandidats ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
}
