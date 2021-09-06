package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.TypeExamenRepository;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.programmation.data.TypeExamen;
import cm.deepdream.academia.programmation.enums.Action;
import cm.deepdream.academia.programmation.messages.Document;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.producer.DocumentProducer;
@Transactional
@Service
public class TypeExamenService {
	private Logger logger = Logger.getLogger(TypeExamenService.class.getName()) ;
	@Autowired
	private TypeExamenRepository typeExamenRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private DocumentProducer producer ;
	
	
	
	public TypeExamen creer (TypeExamen typeExamen) throws Exception{
		try {
			logger.info("etablissement = "+typeExamen.getEtablissement().getId());
			typeExamen.setId(sequenceDAO.nextGlobalId(TypeExamen.class.getName()));
			typeExamen.setNum(sequenceDAO.nextId(typeExamen.getEtablissement(), TypeExamen.class.getName()));
			typeExamen.setDateCreation(LocalDateTime.now());
			typeExamen.setDateDernMaj(LocalDateTime.now());
			TypeExamen typeExamenCree = typeExamenRepository.save(typeExamen) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(typeExamenCree), 
					TypeExamen.class.getName()));
			return typeExamenCree ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public TypeExamen modifier (TypeExamen typeExamen) throws Exception{
		try {
			typeExamen.setDateDernMaj(LocalDateTime.now());
			TypeExamen typeExamenMaj = typeExamenRepository.save(typeExamen) ;
			producer.publier(new Document(Action.Save.toString(), 
					SerializerToolkit.getToolkit().serialize(typeExamenMaj), 
					TypeExamen.class.getName()));
			return typeExamenMaj ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public void supprimer (Long idTypeExamen) throws Exception {
		try {
			TypeExamen typeExamen = typeExamenRepository.findById(idTypeExamen).orElseThrow(Exception::new) ;
			typeExamenRepository.delete(typeExamen) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (TypeExamen typeExamen) throws Exception{
		try {
			typeExamenRepository.delete(typeExamen) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public TypeExamen rechercher (Etablissement etablissement, Long idTypeExamen) throws Exception {
		try {
			TypeExamen typeExamen = typeExamenRepository.findByIdAndEtablissement(idTypeExamen, etablissement) ;
			return typeExamen ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	
	public List<TypeExamen> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<TypeExamen> liste = typeExamenRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<TypeExamen> rechercher (Etablissement etablissement, Niveau niveau) throws Exception {
		try {
			List<TypeExamen> liste = typeExamenRepository.findByEtablissementAndNiveau(etablissement, niveau) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<TypeExamen> rechercher (TypeExamen typeTypeExamen) throws Exception {
		try {
			Iterable<TypeExamen> source = typeExamenRepository.findAll() ;
			List<TypeExamen> cible = new ArrayList<TypeExamen>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
}
