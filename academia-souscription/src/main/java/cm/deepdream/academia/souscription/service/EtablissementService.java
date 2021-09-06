package cm.deepdream.academia.souscription.service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.souscription.repository.EtablissementRepository;
import cm.deepdream.academia.souscription.util.FileStore;
import cm.deepdream.academia.souscription.util.LocalFileStore;
import cm.deepdream.academia.souscription.util.SerializerToolkit;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Logo;
import cm.deepdream.academia.souscription.enums.Action;
import cm.deepdream.academia.souscription.messages.Document;
import cm.deepdream.academia.souscription.dao.SequenceDAO;
import cm.deepdream.academia.souscription.producer.DocumentProducer;
@Transactional
@Service
public class EtablissementService {
	private Logger logger = Logger.getLogger(EtablissementService.class.getName()) ;
	@Autowired
	private EtablissementRepository etablissementRepository ;
	@Autowired
	private DocumentProducer producer ;
	@Autowired
	private FileStore fileStore ;
	@Autowired 
	private LocalFileStore localFileStore ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	public Etablissement creer (Etablissement etablissement) throws Exception{
		try {
			etablissement.setId(sequenceDAO.nextGlobalId(Etablissement.class.getName()));
			etablissementRepository.save(etablissement) ;
			producer.publier(new Document(Action.Nouveau.name(), 
					SerializerToolkit.getToolkit().serialize(etablissement), 
					Etablissement.class.getName()));
			return etablissement ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	
	public Etablissement modifier (Etablissement etablissement) throws Exception{
		try {
			etablissementRepository.save(etablissement) ;
			producer.publier(new Document(Action.Modification.name(), 
					SerializerToolkit.getToolkit().serialize(etablissement), 
					Etablissement.class.getName()));
			return etablissement ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	
	public void supprimer (Long idEtablissement) throws Exception {
		try {
			Etablissement etablissement = etablissementRepository.findById(idEtablissement).orElseThrow(Exception::new) ;
			etablissementRepository.delete(etablissement) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Etablissement rechercher (Long idEtablissement) throws Exception {
		try {
			Etablissement etablissement = etablissementRepository.findById(idEtablissement).orElseGet(null) ;
			return etablissement ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Etablissement etablissement) throws Exception{
		try {
			etablissementRepository.delete(etablissement) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Etablissement> rechercher (Etablissement etablissement) throws Exception {
		try {
			Iterable<Etablissement> source = etablissementRepository.findAll() ;
			List<Etablissement> cible = new ArrayList<Etablissement>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public byte[] download (Logo logo) throws Exception{
		try {
			byte[] bytes = localFileStore.download(logo.getPath(), logo.getSubPath(), logo.getFileName()) ;
			if(bytes == null) {
				bytes = fileStore.download(logo.getPath()+"/"+logo.getSubPath(), logo.getFileName()) ;
			}
			return bytes ;
		}catch(Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
}
