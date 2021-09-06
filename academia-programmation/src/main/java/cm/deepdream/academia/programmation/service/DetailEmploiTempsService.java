package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.DetailEmploiTempsRepository;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.DetailEmploiTemps;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.enums.JourSemaine;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class DetailEmploiTempsService {
	private Logger logger = Logger.getLogger(DetailEmploiTempsService.class.getName()) ;
	@Autowired
	private DetailEmploiTempsRepository  detailEmploiTempsRepository ;
	@Autowired
	private DocumentProducer producer ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	
	public DetailEmploiTemps creer (DetailEmploiTemps detailEmploiTemps) throws Exception{
		try {
			detailEmploiTemps.setId(sequenceDAO.nextGlobalId(DetailEmploiTemps.class.getName()));
			detailEmploiTemps.setNum(sequenceDAO.nextId(detailEmploiTemps.getEtablissement(), DetailEmploiTemps.class.getName()));
			detailEmploiTemps.setDateCreation(LocalDateTime.now());
			detailEmploiTemps.setDateDernMaj(LocalDateTime.now());
			detailEmploiTemps.setLibelleJour(JourSemaine.getLibelle(detailEmploiTemps.getJourSemaine()));
			DetailEmploiTemps detailETCree = detailEmploiTempsRepository.save(detailEmploiTemps) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(detailETCree), 
					DetailEmploiTemps.class.getName()));
			return detailEmploiTemps ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public DetailEmploiTemps modifier (DetailEmploiTemps detailEmploiTemps) throws Exception{
		try {
			detailEmploiTemps.setDateDernMaj(LocalDateTime.now());
			DetailEmploiTemps detailETModifie = detailEmploiTempsRepository.save(detailEmploiTemps) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(detailETModifie), 
					DetailEmploiTemps.class.getName()));
			return detailEmploiTemps ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idDetailEmploiTemps) throws Exception {
		try {
			DetailEmploiTemps detailEmploiTemps = detailEmploiTempsRepository.findById(idDetailEmploiTemps).orElseThrow(Exception::new) ;
			detailEmploiTempsRepository.delete(detailEmploiTemps) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (DetailEmploiTemps detailEmploiTemps) throws Exception{
		try {
			detailEmploiTempsRepository.delete(detailEmploiTemps) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public DetailEmploiTemps rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			DetailEmploiTemps detailEmploiTemps = detailEmploiTempsRepository.findByIdAndEtablissement(id, etablissement);
			return detailEmploiTemps ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<DetailEmploiTemps> rechercher (DetailEmploiTemps detailEmploiTemps) throws Exception {
		try {
			Iterable<DetailEmploiTemps> source = detailEmploiTempsRepository.findAll() ;
			List<DetailEmploiTemps> cible = new ArrayList<DetailEmploiTemps>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
