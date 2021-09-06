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
import cm.deepdream.academia.programmation.repository.EleveRepository;
import cm.deepdream.academia.programmation.repository.EtablissementRepository;
import cm.deepdream.academia.programmation.util.FileStore;
import cm.deepdream.academia.programmation.util.LocalFileStore;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.programmation.util.StringToolkit;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class EleveService {
	private Logger logger = Logger.getLogger(EleveService.class.getName()) ;
	@Autowired
	private EleveRepository eleveRepository ;
	@Autowired
	private EtablissementRepository etablissementRepository ;
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
	
	public Eleve creer (Eleve eleve) throws Exception{
		try {
			eleve.setId(sequenceDAO.nextGlobalId(Eleve.class.getName())) ;
			eleve.setNum(sequenceDAO.nextId(eleve.getEtablissement(), Eleve.class.getName()));
			eleve.setDateCreation(LocalDateTime.now()) ;
			eleve.setDateDernMaj(LocalDateTime.now());
			
			eleve.setNom(eleve.getNom().toUpperCase());
			
			if(eleve.getPrenom() != null && ! eleve.getPrenom().isEmpty()) {
				eleve.setPrenom(StringToolkit.getToolkit().capitalize(eleve.getPrenom()));
			}
			
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
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(eleveCree), 
					Eleve.class.getName()));
			Map<String, String> metadata = new HashMap<String, String>();
		    metadata.put("Content-Type", photo.getContentType());
		    metadata.put("Content-Length", String.valueOf(photo.getSize()));

		    localFileStore.upload(photo.getPath(), photo.getSubPath1(), photo.getSubPath2(), photo.getFileName(), Base64.decodeBase64(photo.getBytesStr()));
		    
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
			return eleve ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Eleve modifier (Eleve eleve) throws Exception{
		try {
			Photo photo = eleve.getPhoto() ;
			if(photo.isModified()) {
				Etablissement etablissement = eleve.getEtablissement() ;
				String extention = Arrays.asList(photo.getContentType().split("/")).get(1) ;
				String path = bucketName ;
				String fileName = String.format("%s", StringToolkit.getToolkit().normalizePath(eleve.getNom())+"_"+eleve.getId()+"."+extention);
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
			
			eleve.setDateDernMaj(LocalDateTime.now());
			eleve.setNom(eleve.getNom().toUpperCase());
			
			if(eleve.getPrenom() != null && ! eleve.getPrenom().isEmpty()) {
				eleve.setPrenom(StringToolkit.getToolkit().capitalize(eleve.getPrenom())) ;
			}
			
			Eleve eleveModifie = eleveRepository.save(eleve) ;
			
			if(photo.getBytesStr() == null) {
				byte[] bytes = localFileStore.download(photo.getPath(), photo.getSubPath1(), 
						photo.getSubPath2(), photo.getFileName()) ;
				photo.setBytesStr(Base64.encodeBase64String(bytes));
				eleveModifie.setPhoto(photo);
			}
			
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(eleveModifie), 
					Eleve.class.getName()));
			return eleve ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	
	public void supprimer (Long idEleve) throws Exception {
		try {
			Eleve eleve = eleveRepository.findById(idEleve).orElseThrow(Exception::new) ;
			eleveRepository.delete(eleve) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(eleve), 
					Eleve.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	
	public void supprimer (Eleve eleve) throws Exception{
		try {
			eleveRepository.delete(eleve) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(eleve), 
					Eleve.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Eleve rechercher (Long idEleve) throws Exception {
		try {
			Eleve eleve = eleveRepository.findById(idEleve).orElseThrow(Exception::new) ;
			return eleve ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Eleve rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Eleve eleve = eleveRepository.findByIdAndEtablissement(id, etablissement) ;
			return eleve ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Eleve> rechercher (Eleve eleve) throws Exception {
		try {
			Iterable<Eleve> source = eleveRepository.findAll() ;
			List<Eleve> cible = new ArrayList<Eleve>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Eleve> rechercher (String situation, Etablissement etablissement) throws Exception {
		try {
			List<Eleve> liste = eleveRepository.findByEtablissementAndStatut(etablissement, situation) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Eleve> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Eleve> liste = eleveRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Eleve> rechercher (Etablissement etablissement, Classe classe) throws Exception {
		try {
			List<Eleve> liste = eleveRepository.findByEtablissementAndClasse(etablissement, classe) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Eleve> rechercher (Etablissement etablissement, Niveau niveau) throws Exception {
		try {
			Iterable<Eleve> iterableEleves = eleveRepository.findAll() ;
			List<Eleve> listeEleves = new ArrayList<Eleve>() ;
			iterableEleves.forEach(listeEleves::add) ;
			List<Eleve> liste = listeEleves.stream().filter(e -> e.getClasse() != null 
					&& niveau.equals(e.getClasse().getNiveau())).collect(Collectors.toList()) ;
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
	
	public Long compter (Etablissement etablissement) throws Exception {
		try {
			Long nombre = eleveRepository.countByEtablissement(etablissement);
			return nombre ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
