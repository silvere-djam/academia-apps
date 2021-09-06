package cm.deepdream.academia.viescolaire.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.viescolaire.repository.ActiviteRepository;
import cm.deepdream.academia.viescolaire.dao.SequenceDAO;
import cm.deepdream.academia.viescolaire.data.Activite;
import cm.deepdream.academia.souscription.data.Etablissement;
@Transactional
@Service
public class ActiviteService {
	private Logger logger = Logger.getLogger(ActiviteService.class.getName()) ;
	@Autowired
	private ActiviteRepository activiteRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	public Activite creer (Activite activite) throws Exception{
		try {
			activite.setId(System.currentTimeMillis()) ;
			activite.setNum(sequenceDAO.nextId(activite.getEtablissement(), Activite.class.getName()));
			activite.setDateCreation(LocalDateTime.now()) ;
			activite.setDateDernMaj(LocalDateTime.now()) ;
			activiteRepository.save(activite) ;
			return activite ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Activite modifier (Activite activite) throws Exception{
		try {
			activite.setDateDernMaj(LocalDateTime.now()) ;
			activiteRepository.save(activite) ;
			return activite ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idActivite) throws Exception {
		try {
			Activite activite = activiteRepository.findById(idActivite).orElseThrow(Exception::new) ;
			activiteRepository.delete(activite) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Activite activite) throws Exception{
		try {
			activiteRepository.delete(activite) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Activite rechercher (long id) throws Exception {
		try {
			Activite activite = activiteRepository.findById(id).orElseThrow(Exception::new) ;
			return activite ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public Activite rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Activite activite = activiteRepository.findByIdAndEtablissement(id, etablissement);
			return activite ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Activite> rechercher (Activite activite) throws Exception {
		try {
			Iterable<Activite> source = activiteRepository.findAll() ;
			List<Activite> cible = new ArrayList<Activite>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Activite> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Activite> liste = activiteRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
