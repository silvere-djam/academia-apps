package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.GroupeRepository;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Groupe;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class GroupeService {
	private Logger logger = Logger.getLogger(GroupeService.class.getName()) ;
	@Autowired
	private GroupeRepository groupeRepository ;
	@Autowired
	private DocumentProducer producer ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	
	public Groupe creer (Groupe groupe) throws Exception{
		try {
			groupe.setId(sequenceDAO.nextGlobalId(Groupe.class.getName()));
			groupe.setNum(sequenceDAO.nextId(groupe.getEtablissement(), Groupe.class.getName()));
			groupe.setDateCreation(LocalDateTime.now());
			groupe.setDateDernMaj(LocalDateTime.now());
			Groupe groupeCree = groupeRepository.save(groupe) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(groupeCree), 
					Groupe.class.getName()));
			return groupe ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Groupe modifier (Groupe groupe) throws Exception{
		try {
			groupe.setDateDernMaj(LocalDateTime.now());
			Groupe groupeModifie = groupeRepository.save(groupe) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(groupeModifie), 
					Groupe.class.getName()));
			return groupe ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idGroupe) throws Exception {
		try {
			Groupe groupe = groupeRepository.findById(idGroupe).orElseThrow(Exception::new) ;
			groupeRepository.delete(groupe) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Groupe groupe) throws Exception{
		try {
			groupeRepository.delete(groupe) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Groupe rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Groupe groupe = groupeRepository.findByIdAndEtablissement(id, etablissement) ;
			return groupe ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Groupe> rechercher (Groupe groupe) throws Exception {
		try {
			Iterable<Groupe> source = groupeRepository.findAll() ;
			List<Groupe> cible = new ArrayList<Groupe>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Groupe> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Groupe> liste = groupeRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
