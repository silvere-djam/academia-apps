package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.repository.TrimestreRepository;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class TrimestreService {
	private Logger logger = Logger.getLogger(TrimestreService.class.getName()) ;
	@Autowired
	private TrimestreRepository trimestreRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private DocumentProducer producer ;
	
	
	
	public Trimestre creer (Trimestre trimestre) throws Exception{
		try {
			if(trimestreRepository.existsByEtablissementAndLibelle(trimestre.getEtablissement(),
					trimestre.getLibelle()))
				return null ;
			trimestre.setId(sequenceDAO.nextGlobalId(Trimestre.class.getName()));
			trimestre.setNum(sequenceDAO.nextId(trimestre.getEtablissement(), Trimestre.class.getName()));
			trimestre.setDateCreation(LocalDateTime.now());
			trimestre.setDateDernMaj(LocalDateTime.now());
			trimestreRepository.save(trimestre) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(trimestre), 
					Trimestre.class.getName()));
			return trimestre ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Trimestre modifier (Trimestre trimestre) throws Exception{
		try {
			trimestre.setDateDernMaj(LocalDateTime.now());
			trimestreRepository.save(trimestre) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(trimestre), 
					Trimestre.class.getName()));
			return trimestre ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idTrimestre) throws Exception {
		try {
			Trimestre trimestre = trimestreRepository.findById(idTrimestre).orElseThrow(Exception::new) ;
			trimestreRepository.delete(trimestre) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(trimestre), 
					Trimestre.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Trimestre trimestre) throws Exception{
		try {
			trimestreRepository.delete(trimestre) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(trimestre), 
					Trimestre.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Trimestre rechercher (long id) throws Exception {
		try {
			Trimestre trimestre = trimestreRepository.findById(id).get();
			return trimestre ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Trimestre rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Trimestre trimestre = trimestreRepository.findByIdAndEtablissement(id, etablissement);
			return trimestre ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Trimestre rechercherCourant (Etablissement etablissement, AnneeScolaire anneeScolaire) throws Exception {
		try {
			Trimestre trimestre = trimestreRepository.findByEtablissementAndAnneeScolaireAndCourant(etablissement, anneeScolaire, 1) ;
			return trimestre ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Trimestre> rechercher (Trimestre trimestre) throws Exception {
		try {
			Iterable<Trimestre> source = trimestreRepository.findAll() ;
			List<Trimestre> cible = new ArrayList<Trimestre>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Trimestre> rechercher (Etablissement etablissement, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<Trimestre> liste = trimestreRepository.findByEtablissementAndAnneeScolaire(etablissement, anneeScolaire) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void definirDefaut (Trimestre trimestre) throws Exception{
		try {
			List<Trimestre> listeTrimestres = trimestreRepository.findByEtablissement(trimestre.getEtablissement()) ;
			for (Trimestre trim : listeTrimestres) {
				trim.setDateDernMaj(LocalDateTime.now());
				trim.setCourant(0);
				trimestreRepository.save(trim) ;
			}
			trimestre.setDateDernMaj(LocalDateTime.now());
			trimestre.setCourant(1);
			trimestreRepository.save(trimestre) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(trimestre), 
					Trimestre.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
}
