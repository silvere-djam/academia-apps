package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.FiliereRepository;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Filiere;
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class FiliereService {
	private Logger logger = Logger.getLogger(FiliereService.class.getName()) ;
	@Autowired
	private FiliereRepository filiereRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private DocumentProducer producer ;
	
	
	
	public Filiere creer (Filiere filiere) throws Exception{
		try {
			filiere.setId(sequenceDAO.nextGlobalId(Filiere.class.getName()));
			filiere.setNum(sequenceDAO.nextId(filiere.getEtablissement(), Filiere.class.getName()));
			filiere.setDateCreation(LocalDateTime.now()) ;
			filiere.setDateDernMaj(LocalDateTime.now()) ;
			filiereRepository.save(filiere) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(filiere), 
					Filiere.class.getName()));
			return filiere ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Filiere modifier (Filiere filiere) throws Exception{
		try {
			filiere.setDateDernMaj(LocalDateTime.now()) ;
			filiereRepository.save(filiere) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(filiere), 
					Filiere.class.getName()));
			return filiere ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idFiliere) throws Exception {
		try {
			Filiere filiere = filiereRepository.findById(idFiliere).orElseThrow(Exception::new) ;
			filiereRepository.delete(filiere) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(filiere), 
					Filiere.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Filiere filiere) throws Exception{
		try {
			filiereRepository.delete(filiere) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(filiere), 
					Filiere.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Filiere rechercher (Etablissement etablissement, long id) throws Exception {
		try {
			Filiere filiere = filiereRepository.findByIdAndEtablissement(id, etablissement) ;
			return filiere ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Filiere rechercher (long idFiliere) throws Exception {
		try {
			Filiere filiere = filiereRepository.findById(idFiliere).orElseThrow(Exception::new) ;
			return filiere ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Filiere> rechercher (Filiere filiere) throws Exception {
		try {
			Iterable<Filiere> source = filiereRepository.findAll() ;
			List<Filiere> cible = new ArrayList<Filiere>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Filiere> rechercher (Etablissement ecole) throws Exception {
		try {
			List<Filiere> liste = filiereRepository.findByEtablissement(ecole) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
