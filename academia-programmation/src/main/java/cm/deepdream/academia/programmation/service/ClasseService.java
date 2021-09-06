package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.ClasseRepository;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class ClasseService {
	private Logger logger = Logger.getLogger(ClasseService.class.getName()) ;
	@Autowired
	private ClasseRepository classeRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private DocumentProducer producer ;
	
	
	
	public Classe creer (Classe classe) throws Exception{
		try {
			classe.setId(sequenceDAO.nextGlobalId(Classe.class.getName())) ;
			classe.setNum(sequenceDAO.nextId(classe.getEtablissement(), Classe.class.getName()));
			classe.setDateCreation(LocalDateTime.now()) ;
			classe.setDateDernMaj(LocalDateTime.now()) ;
			Classe classeCreee = classeRepository.save(classe) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(classeCreee), 
					Classe.class.getName()));
			return classe ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Classe modifier (Classe classe) throws Exception{
		try {
			classe.setDateDernMaj(LocalDateTime.now()) ;
			Classe classeModifiee = classeRepository.save(classe) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(classeModifiee), 
					Classe.class.getName()));
			return classe ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idClasse) throws Exception {
		try {
			Classe classe = classeRepository.findById(idClasse).orElseThrow(Exception::new) ;
			classeRepository.delete(classe) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(classe), 
					Classe.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Classe classe) throws Exception{
		try {
			classeRepository.delete(classe) ;
			producer.publier(new Document(Action.Delete.toString(), 
					SerializerToolkit.getToolkit().serialize(classe), 
					Classe.class.getName()));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	public Classe rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Classe classe = classeRepository.findByIdAndEtablissement(id, etablissement);
			return classe ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Classe> rechercher (Classe classe) throws Exception {
		try {
			Iterable<Classe> source = classeRepository.findAll() ;
			List<Classe> cible = new ArrayList<Classe>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Classe> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Classe> liste = classeRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
