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
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.deepdream.academia.programmation.repository.DetailEmploiTempsRepository;
import cm.deepdream.academia.programmation.repository.EnseignantRepository;
import cm.deepdream.academia.programmation.util.FileStore;
import cm.deepdream.academia.programmation.util.LocalFileStore;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.programmation.util.StringToolkit;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.DetailEmploiTemps;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.enums.SituationE;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class EnseignantService {
	private Logger logger = Logger.getLogger(EnseignantService.class.getName()) ;
	@Autowired
	private EnseignantRepository enseignantRepository ;
	@Autowired
	private DetailEmploiTempsRepository detailEmploiTempsRepository ;
	@Autowired
	private FileStore fileStore ;
	@Autowired
	private LocalFileStore localFileStore ;
	@Value("${app.souscription.aws.bucketName}")
	private String bucketName ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private DocumentProducer producer ;
	
	
	public Enseignant creer (Enseignant enseignant) throws Exception{
		try {
			enseignant.setId(sequenceDAO.nextGlobalId(Enseignant.class.getName()));
			enseignant.setNum(sequenceDAO.nextId(enseignant.getEtablissement(), Enseignant.class.getName()));
			enseignant.setDateCreation(LocalDateTime.now()) ;
			enseignant.setDateDernMaj(LocalDateTime.now()) ;
			enseignant.setSituation(SituationE.En_Poste.name());
			enseignant.setNom(enseignant.getNom().toUpperCase());
			
			if(enseignant.getPrenom() != null && ! enseignant.getPrenom().isEmpty()) {
				enseignant.setPrenom(StringToolkit.getToolkit().capitalize(enseignant.getPrenom()));
			}
			
			Etablissement etablissement = enseignant.getEtablissement() ;
			Photo photo = enseignant.getPhoto() ;
			String extention = Arrays.asList(photo.getContentType().split("/")).get(1) ;
			String path = bucketName ;
			String fileName = String.format("%s", StringToolkit.getToolkit().normalizePath(enseignant.getNom())+"_"+enseignant.getId()+"."+extention);
			photo.setPath(path);
			photo.setSubPath1(StringToolkit.getToolkit().normalizePath(etablissement.getLibelle())+etablissement.getId());
			photo.setSubPath2(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy")));
			photo.setFileName(fileName);
			
			Enseignant enseignantCree = enseignantRepository.save(enseignant) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(enseignantCree), 
					Enseignant.class.getName()));
			
			Map<String, String> metadata = new HashMap<String, String>();
		    metadata.put("Content-Type", photo.getContentType());
		    metadata.put("Content-Length", String.valueOf(photo.getSize()));
		    
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
			return enseignant ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Enseignant modifier (Enseignant enseignant) throws Exception{
		try {
			if(enseignant.getPhoto().isModified()) {
				Etablissement etablissement = enseignant.getEtablissement() ;
				Photo photo = enseignant.getPhoto() ;
				String extention = Arrays.asList(photo.getContentType().split("/")).get(1) ;
				String path = bucketName ;
				String fileName = String.format("%s", StringToolkit.getToolkit().normalizePath(enseignant.getNom())+"_"+enseignant.getId()+"."+extention);
				photo.setPath(path);
				photo.setSubPath1(StringToolkit.getToolkit().normalizePath(etablissement.getLibelle())+etablissement.getId());
				photo.setSubPath2(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy")));
				photo.setFileName(fileName);
				Map<String, String> metadata = new HashMap<String, String>();
			    metadata.put("Content-Type", photo.getContentType());
			    metadata.put("Content-Length", String.valueOf(photo.getSize()));
			    
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
			enseignant.setDateDernMaj(LocalDateTime.now()) ;
			enseignant.setNom(enseignant.getNom().toUpperCase());
			
			if(enseignant.getPrenom() != null && ! enseignant.getPrenom().isEmpty()) {
				enseignant.setPrenom(StringToolkit.getToolkit().capitalize(enseignant.getPrenom()));
			}
			
			Enseignant enseignantModifie = enseignantRepository.save(enseignant) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(enseignantModifie), 
					Enseignant.class.getName()));
			return enseignant ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Long idEnseignant) throws Exception {
		try {
			Enseignant enseignant = enseignantRepository.findById(idEnseignant).orElseThrow(Exception::new) ;
			enseignantRepository.delete(enseignant) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(enseignant), 
					Enseignant.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Enseignant enseignant) throws Exception{
		try {
			enseignantRepository.delete(enseignant) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(enseignant), 
					Enseignant.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Enseignant rechercher (Long idEnseignant) throws Exception {
		try {
			Enseignant enseignant = enseignantRepository.findById(idEnseignant).orElseThrow(Exception::new) ;
			return enseignant ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Enseignant rechercher (Etablissement etablissement, long idEnseignant) throws Exception {
		try {
			Enseignant enseignant = enseignantRepository.findByIdAndEtablissement(idEnseignant, etablissement) ;
			return enseignant ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Enseignant> rechercher (Enseignant enseignant) throws Exception {
		try {
			Iterable<Enseignant> source = enseignantRepository.findAll() ;
			List<Enseignant> cible = new ArrayList<Enseignant>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Enseignant> rechercher (Etablissement etablissement, Classe classe) throws Exception {
		try {
			List<DetailEmploiTemps> listeDetails = detailEmploiTempsRepository.rechercher(etablissement, classe) ;
			List<Enseignant> listeEnseignants = listeDetails.stream().map(d -> d.getEnseignant()).collect(Collectors.toList()) ;
			return listeEnseignants ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Enseignant> rechercher (Etablissement etablissement, String situation) throws Exception {
		try {
			List<Enseignant> listeEnseignants = enseignantRepository.findByEtablissementAndSituation(etablissement, situation) ;
			return listeEnseignants ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Enseignant> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Enseignant> liste = enseignantRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Enseignant> rechercher (Etablissement etablissement, Domaine domaine) throws Exception {
		try {
			List<Enseignant> liste = enseignantRepository.findByEtablissementAndDomaine(etablissement, domaine) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public byte[] download (Photo photo) throws Exception{
		try {
			byte[] bytes = localFileStore.download(photo.getPath(), photo.getSubPath1(), photo.getSubPath2(), photo.getFileName()) ;
			if(bytes == null) {
				bytes = fileStore.download(photo.getPath()+"/"+photo.getSubPath1()+"/"+photo.getSubPath2(), photo.getFileName()) ;
			}
			return bytes ;
		}catch(Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
