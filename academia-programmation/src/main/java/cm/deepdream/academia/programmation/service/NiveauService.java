package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.NiveauRepository;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.producer.DocumentProducer;

@Transactional
@Service
public class NiveauService {
	private Logger logger = Logger.getLogger(NiveauService.class.getName()) ;
	@Autowired
	private NiveauRepository niveauRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private DocumentProducer producer ;
	
	
	public Niveau creer (Niveau niveau) throws Exception{
		try {
			niveau.setId(sequenceDAO.nextGlobalId(Niveau.class.getName()));
			niveau.setNum(sequenceDAO.nextId(niveau.getEtablissement(), Niveau.class.getName()));
			niveau.setDateCreation(LocalDateTime.now()) ;
			niveau.setDateDernMaj(LocalDateTime.now()) ;
			niveauRepository.save(niveau) ;
			producer.publier(new Document(Action.Save.toString(), niveau.toString(), Niveau.class.getName()));
			return niveau ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Niveau modifier (Niveau niveau) throws Exception{
		try {
			niveau.setDateDernMaj(LocalDateTime.now()) ;
			niveauRepository.save(niveau) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(niveau), 
					Niveau.class.getName()));
			return niveau ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idNiveau) throws Exception {
		try {
			Niveau niveau = niveauRepository.findById(idNiveau).orElseThrow(Exception::new) ;
			niveauRepository.delete(niveau) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(niveau), 
					Niveau.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Niveau niveau) throws Exception{
		try {
			niveauRepository.delete(niveau) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(niveau), 
					Niveau.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Niveau rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Niveau niveau = niveauRepository.findByIdAndEtablissement(id, etablissement) ;
			return niveau ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Niveau> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Niveau> listeNiveaux = niveauRepository.findByEtablissement(etablissement) ;
			return listeNiveaux ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Niveau> rechercher (Niveau niveau) throws Exception {
		try {
			Iterable<Niveau> source = niveauRepository.findAll() ;
			List<Niveau> cible = new ArrayList<Niveau>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
}
