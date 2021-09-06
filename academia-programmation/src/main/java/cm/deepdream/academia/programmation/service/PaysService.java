package cm.deepdream.academia.programmation.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.PaysRepository;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Pays;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class PaysService {
	private Logger logger = Logger.getLogger(PaysService.class.getName()) ;
	@Autowired
	private PaysRepository paysRepository ;
	@Autowired
	private DocumentProducer producer ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	
	public Pays creer (Pays pays) throws Exception{
		try {
			pays.setId(sequenceDAO.nextGlobalId(Pays.class.getName()));
			Pays paysCree = paysRepository.save(pays) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(paysCree), 
					Pays.class.getName()));
			return pays ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Pays modifier (Pays pays) throws Exception{
		try {
			Pays paysModifie = paysRepository.save(pays) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(paysModifie), 
					Pays.class.getName()));
			return pays ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idPays) throws Exception {
		try {
			Pays pays = paysRepository.findById(idPays).orElseThrow(Exception::new) ;
			paysRepository.delete(pays) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Pays pays) throws Exception{
		try {
			paysRepository.delete(pays) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	
	public Pays rechercher (Long id) throws Exception {
		try {
			Optional<Pays> paysOpt = paysRepository.findById(id) ;
			return paysOpt.isPresent() ? paysOpt.get():null;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Pays rechercherLibelle (String libelle) throws Exception {
		try {
			Pays pays = paysRepository.findByLibelle(libelle) ;
			return pays ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Pays> rechercher (Pays pays) throws Exception {
		try {
			Iterable<Pays> source = paysRepository.findAll() ;
			List<Pays> cible = new ArrayList<Pays>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	

}
