package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.deepdream.academia.programmation.repository.FonctionRepository;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Fonction;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class FonctionService {
	private Logger logger = Logger.getLogger(FonctionService.class.getName()) ;
	@Autowired
	private FonctionRepository fonctionRepository ;
	@Autowired
	private DocumentProducer producer ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	
	public Fonction creer (Fonction fonction) throws Exception{
		try {
			fonction.setId(sequenceDAO.nextGlobalId(Fonction.class.getName()));
			fonction.setNum(sequenceDAO.nextId(fonction.getEtablissement(), Fonction.class.getName()));
			fonction.setDateCreation(LocalDateTime.now()) ;
			fonction.setDateDernMaj(LocalDateTime.now()) ;
			Fonction fontionCree = fonctionRepository.save(fonction) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(fontionCree),
					Fonction.class.getName()));
			return fonction ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Fonction modifier (Fonction fonction) throws Exception{
		try {
			fonction.setDateDernMaj(LocalDateTime.now()) ;
			Fonction fonctionModifiee = fonctionRepository.save(fonction) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(fonctionModifiee),
					Fonction.class.getName()));
			return fonction ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idFonction) throws Exception {
		try {
			Fonction fonction = fonctionRepository.findById(idFonction).orElseThrow(Exception::new) ;
			fonctionRepository.delete(fonction) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Fonction fonction) throws Exception{
		try {
			fonctionRepository.delete(fonction) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Fonction rechercher (Etablissement etablissement, long id) throws Exception {
		try {
			Fonction fonction = fonctionRepository.findByIdAndEtablissement(id, etablissement) ;
			return fonction ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Fonction rechercher (long idFonction) throws Exception {
		try {
			Fonction fonction = fonctionRepository.findById(idFonction).orElseThrow(Exception::new) ;
			return fonction ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Fonction> rechercher (Fonction fonction) throws Exception {
		try {
			Iterable<Fonction> source = fonctionRepository.findAll() ;
			List<Fonction> cible = new ArrayList<Fonction>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Fonction> rechercher (Etablissement ecole) throws Exception {
		try {
			List<Fonction> liste = fonctionRepository.findByEtablissement(ecole) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
