package cm.deepdream.academia.programmation.service;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.ClasseRepository;
import cm.deepdream.academia.programmation.repository.EleveRepository;
import cm.deepdream.academia.programmation.repository.InscriptionRepository;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.util.FileStore;
import cm.deepdream.academia.programmation.util.LocalFileStore;
import cm.deepdream.academia.programmation.util.StringToolkit;
import cm.deepdream.academia.programmation.data.Inscription;
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.programmation.enums.StatutE;
@Transactional
@Service
public class InscriptionService {
	private Logger logger = Logger.getLogger(InscriptionService.class.getName()) ;
	@Autowired
	private InscriptionRepository inscriptionRepository ;
	@Autowired
	private EleveRepository eleveRepository ;
	@Autowired
	private ClasseRepository classeRepository ;
	@Autowired
	private FileStore fileStore ;
	@Autowired
	private LocalFileStore localFileStore ;
	@Value("${app.souscription.aws.bucketName}")
	private String bucketName ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	public Inscription creer (Inscription inscription) throws Exception {
		try {
			logger.log(Level.INFO, "Ajout de l'inscription "+inscription.getEleve().getId()+" "+inscription.getEtablissement().getId()); 
			
			AnneeScolaire anneeScolaire = inscription.getAnneeScolaire() ;
			Eleve eleveExistant = inscription.getEleve() ;
			
			Eleve eleve = eleveRepository.findByIdAndEtablissement(eleveExistant.getId(), inscription.getEtablissement()) ;
			eleve.setClasse(inscription.getClasse());
			eleve.setModificateur(inscription.getModificateur());
			eleve.setDateDernMaj(LocalDateTime.now());
			
			Etablissement etablissement = eleve.getEtablissement() ;
			Photo photo = eleve.getPhoto() ;
			String extention = Arrays.asList(photo.getContentType().split("/")).get(1) ;
			String path = bucketName ;
			String fileName = String.format("%s", StringToolkit.getToolkit().normalizePath(eleve.getNom())+"_"+eleve.getId()+"."+extention);
			photo.setPath(path);
			photo.setSubPath1(StringToolkit.getToolkit().normalizePath(etablissement.getLibelle())+etablissement.getId());
			photo.setSubPath2(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy")));
			photo.setFileName(fileName);
			Eleve eleveCree = eleveRepository.save(eleve) ;
			
			List<Inscription> listeInscriptions = inscriptionRepository.findByEtablissementAndEleveAndAnneeScolaire(etablissement, 
					eleveCree , anneeScolaire) ;
			if(listeInscriptions.size() == 0) {
				inscription.setId(sequenceDAO.nextGlobalId(Inscription.class.getName()));
				inscription.setNum(sequenceDAO.nextId(inscription.getEtablissement(), Inscription.class.getName())) ;
				inscription.setDateCreation(LocalDateTime.now());
				inscription.setDateDernMaj(LocalDateTime.now());
				inscriptionRepository.save(inscription) ;
			} else {
				Inscription inscriptionExistante = listeInscriptions.get(0) ;
				inscriptionExistante.setStatut(inscription.getStatut()); 
				inscriptionExistante.setDateInscription(inscription.getDateInscription());
				inscriptionExistante.setClasse(inscription.getClasse());
				inscriptionExistante.setDateDernMaj(LocalDateTime.now());
				inscriptionRepository.save(inscriptionExistante) ;
			}
			
			Map<String, String> metadata = new HashMap<String, String>();
		    metadata.put("Content-Type", photo.getContentType());
		    metadata.put("Content-Length", String.valueOf(photo.getSize()));
		    
		    if(photo.getBytesStr() != null) {
		    	localFileStore.upload(photo.getPath(), photo.getSubPath1(), photo.getSubPath2(), photo.getFileName(), 
		    		Base64.decodeBase64(photo.getBytesStr()));
		    
		    	Path logoPath = Paths.get(photo.getPath(), photo.getSubPath1(), photo.getSubPath2(), photo.getFileName()) ;
		    
		    
		    	ScheduledExecutorService quickService = Executors.newScheduledThreadPool(1) ;
		    	quickService.submit(new Runnable() {
		    		@Override
		    		public void run() {
		    			try{
		    				FileInputStream inputStream = new FileInputStream(logoPath.toFile()) ;
		    				fileStore.upload(photo.getPath()+"/"+photo.getSubPath1()+"/"+photo.getSubPath2(),  photo.getFileName(), Optional.of(metadata), inputStream);
		    				try {
		    					inputStream.close(); 
		    				}catch(Exception e) {}
		    			}catch(Exception e){
		    				logger.log(Level.SEVERE, "Exception occur while storing file remotely",e) ;
		    			}
		    		}
		    	});
		    }
			return inscriptionRepository.findByIdAndEtablissement(inscription.getId(), etablissement) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}	
	
	
	private String genererMatricule(Inscription inscription) {		
		Integer effectif = inscriptionRepository.countNumbreInscriptions(inscription.getEtablissement(), inscription.getAnneeScolaire(), 
				inscription.getClasse().getNiveau()) +1 ;
		Classe classe = classeRepository.findByIdAndEtablissement(inscription.getClasse().getId(), 
				inscription.getEtablissement()) ;
		Integer pad = 4 - Integer.toString(effectif).length() ;
		String matricule = inscription.getAnneeScolaire().getDateDebut().format(DateTimeFormatter.ofPattern("yy"))+
				classe.getNiveau().getLibelle().substring(0, 1).toUpperCase()+
				String.format("%0"+pad+"d", effectif) ;
		return matricule ;
	}
	
	
	
	public Inscription modifier (Inscription inscription) throws Exception {
		try {
			inscription.setDateDernMaj(LocalDateTime.now());
			inscriptionRepository.save(inscription) ;
			return inscription ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (Inscription inscription) throws Exception {
		try {
			inscriptionRepository.delete(inscription) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (long idInscription) throws Exception {
		try {
			Optional<Inscription> optInscription = inscriptionRepository.findById(idInscription) ;
			if(optInscription.isPresent())
				inscriptionRepository.delete(optInscription.get()) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public Inscription rechercher (Long id) throws Exception {
		try {
			Optional<Inscription> optInscription = inscriptionRepository.findById(id) ;
			if(optInscription.isPresent()) return optInscription.get() ;
			else return null ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public Inscription rechercher (Etablissement etablissement, long id) throws Exception {
		try {
			Inscription inscription = inscriptionRepository.findByIdAndEtablissement(id, etablissement) ;
			return inscription ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public List<Inscription> rechercher(Etablissement etablissement, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<Inscription> listeInscriptions = inscriptionRepository.findByEtablissementAndAnneeScolaire (etablissement, anneeScolaire) ;
			return listeInscriptions ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Inscription> rechercher(Etablissement etablissement, Classe classe, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<Inscription> listeInscriptions = inscriptionRepository.findByEtablissementAndClasseAndAnneeScolaire (etablissement,  classe,  anneeScolaire) ;
			return listeInscriptions ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Inscription> rechercher(Etablissement etablissement, Eleve eleve, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<Inscription> listeInscriptions = inscriptionRepository.findByEtablissementAndEleveAndAnneeScolaire (etablissement,  eleve,  anneeScolaire) ;
			return listeInscriptions ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Inscription> rechercherTout (Inscription inscription) throws Exception {
		try {
			Iterable<Inscription> itInscriptions = inscriptionRepository.findAll() ;
			List<Inscription> listeInscriptions = new ArrayList() ;
			itInscriptions.forEach(listeInscriptions::add);
			return listeInscriptions ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
}
