package cm.deepdream.academia.souscription.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.souscription.repository.LocaliteRepository;
import cm.deepdream.academia.souscription.dao.SequenceDAO;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Localite;
@Service
@Transactional
public class LocaliteService {
	private Logger logger = Logger.getLogger(LocaliteService.class.getName()) ;
	@Autowired
	private LocaliteRepository localiteRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	public Localite creer (Localite localite) throws Exception {
		try {
			localite.setId(sequenceDAO.nextGlobalId(Localite.class.getName()));
			localiteRepository.save(localite) ;
			return localite ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}	
	
	public Localite modifier (Localite localite) throws Exception {
		try {
			localiteRepository.save(localite) ;
			return localite ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (Localite localite) throws Exception {
		try {
			localiteRepository.delete(localite) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (Long idLocalite) throws Exception {
		try {
			Optional<Localite> optVille = localiteRepository.findById(idLocalite) ;
			if(optVille.isPresent())
				localiteRepository.delete(optVille.get()) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public Localite rechercher (Long id) throws Exception {
		try {
			Optional<Localite> optVille = localiteRepository.findById(id) ;
			if(optVille.isPresent()) return optVille.get() ;
			else return null ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Localite> rechercher(Localite ville) throws Exception {
		try {
			Iterable<Localite> itVilles = localiteRepository.findAll() ;
			List<Localite> listeVilles = new ArrayList() ;
			itVilles.forEach(listeVilles::add);
			return listeVilles ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Localite> rechercherTout (Localite ville) throws Exception {
		try {
			Iterable<Localite> itVilles = localiteRepository.findAll() ;
			List<Localite> listeVilles = new ArrayList() ;
			itVilles.forEach(listeVilles::add);
			return listeVilles ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
}
