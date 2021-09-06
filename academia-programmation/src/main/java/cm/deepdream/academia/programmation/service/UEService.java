package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.UERepository;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
import cm.deepdream.academia.souscription.data.Etablissement;
@Transactional
@Service
public class UEService {
	private Logger logger = Logger.getLogger(UEService.class.getName()) ;
	@Autowired
	private UERepository uERepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private DocumentProducer producer ;
	
	
	public UE creer (UE ue) throws Exception{
		try {
			if(uERepository.existsByEtablissementAndClasseAndDomaine(ue.getEtablissement(), 
					ue.getClasse(), ue.getDomaine()))
				return null ;
			ue.setId(sequenceDAO.nextGlobalId(UE.class.getName()));
			ue.setNum(sequenceDAO.nextId(ue.getEtablissement(), UE.class.getName()));
			ue.setDateCreation(LocalDateTime.now());
			ue.setDateDernMaj(LocalDateTime.now());
			UE ueCree = uERepository.save(ue) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(ueCree), 
					UE.class.getName()));
			return ue ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public UE modifier (UE ue) throws Exception{
		try {
			ue.setDateDernMaj(LocalDateTime.now());
			UE ueModifiee = uERepository.save(ue) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(ueModifiee), 
					UE.class.getName()));
			return ue ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idMatiereCoefficiee) throws Exception {
		try {
			UE ue = uERepository.findById(idMatiereCoefficiee).orElseThrow(Exception::new) ;
			uERepository.delete(ue) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(ue), 
					UE.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public void supprimer (UE ue) throws Exception{
		try {
			uERepository.delete(ue) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(ue), 
					UE.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public UE rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			UE ue = uERepository.findByIdAndEtablissement(id, etablissement) ;
			return ue ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<UE> rechercher (UE ue) throws Exception {
		try {
			Iterable<UE> source = uERepository.findAll() ;
			List<UE> cible = new ArrayList<UE>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<UE> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<UE> liste = uERepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<UE> rechercher (Classe classe) throws Exception {
		try {
			List<UE> liste = uERepository.findByClasse(classe) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
