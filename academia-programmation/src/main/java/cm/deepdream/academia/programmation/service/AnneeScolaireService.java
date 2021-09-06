package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.AnneeScolaireRepository;
import cm.deepdream.academia.programmation.repository.EtablissementRepository;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class AnneeScolaireService {
	private Logger logger = Logger.getLogger(AnneeScolaireService.class.getName()) ;
	@Autowired
	private AnneeScolaireRepository anneeScolaireRepository ;
	@Autowired
	private EtablissementRepository etablissementRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private DocumentProducer producer ;
	
	
	public AnneeScolaire creer (AnneeScolaire anneeScolaire) throws Exception{
		try {
			if(anneeScolaireRepository.existsByEtablissementAndLibelle(anneeScolaire.getEtablissement(), 
					anneeScolaire.getLibelle()))
				return null ;
			anneeScolaire.setId(sequenceDAO.nextGlobalId(AnneeScolaire.class.getName()));
			anneeScolaire.setNum(sequenceDAO.nextId(anneeScolaire.getEtablissement(), AnneeScolaire.class.getName()));
			anneeScolaire.setDateCreation(LocalDateTime.now());
			anneeScolaire.setDateDernMaj(LocalDateTime.now());
			anneeScolaire.setCourante(anneeScolaire.getCourante() == null ? 0:anneeScolaire.getCourante());
			anneeScolaireRepository.save(anneeScolaire) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(anneeScolaire), 
					AnneeScolaire.class.getName()));
			return anneeScolaire ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public AnneeScolaire modifier (AnneeScolaire anneeScolaire) throws Exception{
		try {
			anneeScolaire.setDateDernMaj(LocalDateTime.now());
			anneeScolaire.setCourante(anneeScolaire.getCourante() == null ? 0:anneeScolaire.getCourante());
			anneeScolaireRepository.save(anneeScolaire) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(anneeScolaire), 
					AnneeScolaire.class.getName()));
			return anneeScolaire ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public void definirDefaut (AnneeScolaire anneeScolaire) throws Exception{
		try {
			List<AnneeScolaire> listeAnnees = anneeScolaireRepository.findByEtablissement(anneeScolaire.getEtablissement()) ;
			for (AnneeScolaire annee : listeAnnees) {
				annee.setDateDernMaj(LocalDateTime.now());
				annee.setCourante(0);
				anneeScolaireRepository.save(annee) ;
			}
			anneeScolaire.setDateDernMaj(LocalDateTime.now());
			anneeScolaire.setCourante(1);
			anneeScolaireRepository.save(anneeScolaire) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(anneeScolaire), 
					AnneeScolaire.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idAnneeScolaire) throws Exception {
		try {
			AnneeScolaire anneeScolaire = anneeScolaireRepository.findById(idAnneeScolaire).orElseThrow(Exception::new) ;
			anneeScolaireRepository.delete(anneeScolaire) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(anneeScolaire), 
					AnneeScolaire.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (AnneeScolaire anneeScolaire) throws Exception{
		try {
			anneeScolaireRepository.delete(anneeScolaire) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(anneeScolaire), 
					AnneeScolaire.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public AnneeScolaire rechercher (Etablissement etablissement, long id) throws Exception {
		try {
			AnneeScolaire anneeScolaire = anneeScolaireRepository.findByIdAndEtablissement(id, etablissement) ;
			return anneeScolaire ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public AnneeScolaire rechercherCourant (Etablissement etablissement) throws Exception {
		try {
			AnneeScolaire anneeScolaire = anneeScolaireRepository.rechercherCourant (etablissement) ;
			return anneeScolaire ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<AnneeScolaire> rechercher (AnneeScolaire anneeScolaire) throws Exception {
		try {
			Iterable<AnneeScolaire> source = anneeScolaireRepository.findAll() ;
			List<AnneeScolaire> cible = new ArrayList<AnneeScolaire>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<AnneeScolaire> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<AnneeScolaire> liste = anneeScolaireRepository.findByEtablissement (etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<AnneeScolaire> synchroniser (Etablissement etablissement) throws Exception {
		try {
			
			final List<AnneeScolaire> listeAnneesScolaires = anneeScolaireRepository.findByEtablissement (etablissement) ;
			
			logger.info("Nombre d'établissements synchronisés : "+listeAnneesScolaires.size());
			
			Runnable task = () -> {
				listeAnneesScolaires.forEach(anneeScolaire -> {
					try {
						producer.publier(new Document(Action.Save.toString(), 
								SerializerToolkit.getToolkit().serialize(anneeScolaire), 
								AnneeScolaire.class.getName()));
					}catch(Exception ex) {}
				});
			} ;
			
			Executors.newSingleThreadExecutor().submit(task) ;
			return listeAnneesScolaires ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
