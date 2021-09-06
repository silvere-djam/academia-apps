package cm.deepdream.academia.souscription.service;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.core.joran.util.StringToObjectConverter;
import cm.deepdream.academia.souscription.repository.AbonnementRepository;
import cm.deepdream.academia.souscription.repository.EtablissementRepository;
import cm.deepdream.academia.souscription.repository.OffreRepository;
import cm.deepdream.academia.souscription.dao.SequenceDAO;
import cm.deepdream.academia.souscription.data.Abonnement;
import cm.deepdream.academia.souscription.producer.DocumentProducer;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Logo;
import cm.deepdream.academia.souscription.data.Offre;
import cm.deepdream.academia.souscription.enums.Action;
import cm.deepdream.academia.souscription.enums.StatutA;
import cm.deepdream.academia.souscription.messages.Document;
import cm.deepdream.academia.souscription.util.FileStore;
import cm.deepdream.academia.souscription.util.LocalFileStore;
import cm.deepdream.academia.souscription.util.SerializerToolkit;
import cm.deepdream.academia.souscription.util.StringToolkit;
@Transactional
@Service
public class AbonnementService {
	private Logger logger = Logger.getLogger(AbonnementService.class.getName()) ;
	@Autowired
	private AbonnementRepository abonnementRepository ;
	@Autowired
	private EtablissementRepository etablissementRepository ;
	@Autowired
	private DocumentProducer producer ;
	@Autowired
	private OffreRepository offreRepository ;
	@Autowired
	private FileStore fileStore ;
	@Autowired
	private LocalFileStore localFileStore ;
	@Value("${app.souscription.aws.bucketName}")
	private String bucketName ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	
	public Abonnement creer (Abonnement abonnement) throws Exception {
		try {
			abonnement.setId(sequenceDAO.nextGlobalId(Abonnement.class.getName()));
			abonnement.setNum(sequenceDAO.nextId(abonnement.getEtablissement(), Abonnement.class.getName())) ;
			Optional<Offre> offreOpt = offreRepository.findById(abonnement.getOffre().getId()) ;
			abonnement.setDateDebut(LocalDate.now()) ;
			abonnement.setDateFin(abonnement.getDateDebut().plusMonths(offreOpt.get().getDureeEssai())) ;//???????
			abonnement.setDateCreation(LocalDateTime.now()) ;
			abonnement.setDateDernMaj(LocalDateTime.now()) ;
			Abonnement abonnementCree = abonnementRepository.save(abonnement) ;
			
			producer.publier(new Document(Action.Nouveau.name(), 
					SerializerToolkit.getToolkit().serialize(abonnementCree), 
					Abonnement.class.getName()));
			return abonnement ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}	
	
	
	
	public Abonnement creer (Etablissement etablissement) throws Exception {
		try {
			etablissement.setId(sequenceDAO.nextGlobalId(Etablissement.class.getName()));
			etablissement.setDateCreation(LocalDateTime.now());
			etablissement.setDateDernMaj(LocalDateTime.now());
			
			if(etablissementRepository.existsByTelephoneChef(etablissement.getContactChef().getTelephone())) {
				throw new Exception("Erreur ! Téléphone du chef déjà utilisé") ;
			}
			
			if(etablissementRepository.existsByTelephoneInformaticien(etablissement.getContactChef().getTelephone())) {
				throw new Exception("Erreur ! Téléphone du chef déjà utilisé") ;
			}
			
			if(etablissementRepository.existsByTelephoneChef(etablissement.getContactInformaticien().getTelephone())) {
				throw new Exception("Erreur ! Téléphone de l'informatien déjà utilisé") ;
			}
			
			if(etablissementRepository.existsByTelephoneInformaticien(etablissement.getContactInformaticien().getTelephone())) {
				throw new Exception("Erreur ! Téléphone de l'informatien déjà utilisé") ;
			}
			
			
			Logo logo = etablissement.getLogo() ;
			String extention = Arrays.asList(logo.getContentType().split("/")).get(1) ;
			String path = bucketName ;
			String fileName = String.format("%s", "Logo_"+StringToolkit.getToolkit().normalizePath(etablissement.getLibelle())+"_"+etablissement.getId()+"."+extention);
			logo.setPath(path);
		
			logo.setSubPath(StringToolkit.getToolkit().normalizePath(etablissement.getLibelle())+etablissement.getId());
			logo.setFileName(fileName);
			
			Etablissement etablissementCree = etablissementRepository.save(etablissement) ;
			
			Abonnement abonnement = new Abonnement() ;
			abonnement.setId(sequenceDAO.nextGlobalId(Abonnement.class.getName()));
			abonnement.setNum(sequenceDAO.nextId(abonnement.getEtablissement(), Abonnement.class.getName())) ;
			abonnement.setEtablissement(etablissementCree);
			abonnement.setNbEleves(etablissement.getNbElevesApprox());
			
			List<Offre> listeOffres = offreRepository.findByMinElevesLessThanEqualAndMaxElevesGreaterThanEqualOrderByMinElevesAsc(abonnement.getNbEleves(), 
					abonnement.getNbEleves()) ;
			
			if(listeOffres.size() == 0) {
				throw new Exception(String.format("Erreur ! Offre indisponible pour '%d' élèves", 
						abonnement.getNbEleves())) ;
			}
			
			Offre offre = listeOffres.get(0) ;
			abonnement.setEtablissement(etablissementCree);
			abonnement.setOffre(offre);
			abonnement.setDuree(offre.getDureeEssai());
			abonnement.setDateDebut(LocalDate.now()) ;
			abonnement.setDateFin(abonnement.getDateDebut().plusDays(offre.getDureeEssai())) ;
			abonnement.setDateCreation(LocalDateTime.now()) ;
			abonnement.setDateDernMaj(LocalDateTime.now()) ;
			abonnement.setEvaluation(true);
			abonnement.setStatut(StatutA.En_Cours.name());
			abonnement.setNbEleves(etablissement.getNbElevesApprox());
			
			Abonnement abonnementCree = abonnementRepository.save(abonnement) ;
			
			producer.publier(new Document(Action.Nouveau.name(), 
					SerializerToolkit.getToolkit().serialize(etablissementCree), 
					Etablissement.class.getName()));
			
			producer.publier(new Document(Action.Nouveau.name(), 
					SerializerToolkit.getToolkit().serialize(abonnementCree), 
					Abonnement.class.getName()));
			
		    Map<String, String> metadata = new HashMap<String, String>();
		    metadata.put("Content-Type", logo.getContentType());
		    metadata.put("Content-Length", String.valueOf(logo.getSize()));
		    
		    localFileStore.upload(logo.getPath(), logo.getSubPath(), logo.getFileName(), logo.getBytes());
		    
		    Path logoPath = Paths.get(logo.getPath(), logo.getSubPath(), logo.getFileName()) ;
		    
		    
		    ScheduledExecutorService quickService = Executors.newScheduledThreadPool(1) ;
		    quickService.submit(new Runnable() {
				@Override
				public void run() {
					try{
						FileInputStream inputStream = new FileInputStream(logoPath.toFile()) ;
						fileStore.upload(logo.getPath()+"/"+logo.getSubPath(),  logo.getFileName(), Optional.of(metadata), inputStream);
						try {
					    	inputStream.close(); 
					    }catch(Exception e) {}
					}catch(Exception e){
						logger.log(Level.SEVERE, "Exception occur while storing file remotely",e) ;
					}
				}
			});
		    
			return abonnement ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}	
	
	
	
	public Abonnement modifier (Abonnement abonnement) throws Exception {
		try {
			abonnement.setDateDernMaj(LocalDateTime.now()) ;
			abonnementRepository.save(abonnement) ;
			return abonnement ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	
	public void supprimer (Abonnement abonnement) throws Exception {
		try {
			abonnementRepository.delete(abonnement) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	
	public void supprimer (long idAbonnement) throws Exception {
		try {
			Optional<Abonnement> optAbonnement = abonnementRepository.findById(idAbonnement) ;
			if(optAbonnement.isPresent())
				abonnementRepository.delete(optAbonnement.get()) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	
	public Abonnement rechercher (long id) throws Exception {
		try {
			Optional<Abonnement> optAbonnement = abonnementRepository.findById(id) ;
			if(optAbonnement.isPresent()) return optAbonnement.get() ;
			else return null ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	
	public List<Abonnement> rechercher(Abonnement abonnement) throws Exception {
		try {
			Iterable<Abonnement> itAbonnements = abonnementRepository.findAll() ;
			List<Abonnement> listeAbonnements = new ArrayList() ;
			itAbonnements.forEach(listeAbonnements::add);
			return listeAbonnements ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	
	public List<Abonnement> rechercher(Etablissement etablissement) throws Exception {
		try {
			List<Abonnement> liste = abonnementRepository.findByEtablissement(etablissement) ;
			return liste ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	
	public List<Abonnement> rechercher(String statut) throws Exception {
		try {
			List<Abonnement> liste = abonnementRepository.findByStatut(statut) ;
			return liste ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	
	
	public List<Abonnement> rechercher(LocalDate dateDebut, LocalDate dateFin) throws Exception {
		try {
			List<Abonnement> liste = abonnementRepository.findByDateDebutBetween(dateDebut, dateFin) ;
			return liste ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	
	public List<Abonnement> rechercherTout (Abonnement abonnement) throws Exception {
		try {
			Iterable<Abonnement> itAbonnements = abonnementRepository.findAll() ;
			List<Abonnement> listeAbonnements = new ArrayList() ;
			itAbonnements.forEach(listeAbonnements::add);
			return listeAbonnements ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
}
