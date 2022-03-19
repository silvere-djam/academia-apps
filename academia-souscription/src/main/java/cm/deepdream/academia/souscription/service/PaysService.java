package cm.deepdream.academia.souscription.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.souscription.repository.PaysRepository;
import cm.deepdream.academia.souscription.dao.SequenceDAO;
import cm.deepdream.academia.souscription.model.Pays;
@Service
@Transactional
public class PaysService {
	private Logger logger = Logger.getLogger(PaysService.class.getName()) ;
	@Autowired
	private PaysRepository paysRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	public Pays creer (Pays pays) throws Exception {
		try {
			pays.setId(sequenceDAO.nextGlobalId(Pays.class.getName()));
			paysRepository.save(pays) ;
			return pays ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}	
	
	
	
	public Pays modifier (Pays pays) throws Exception {
		try {
			paysRepository.save(pays) ;
			return pays ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (Pays pays) throws Exception {
		try {
			paysRepository.delete(pays) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (long idPays) throws Exception {
		try {
			Optional<Pays> optPays = paysRepository.findById(idPays) ;
			if(optPays.isPresent())
				paysRepository.delete(optPays.get()) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public Pays rechercher (long id) throws Exception {
		try {
			Optional<Pays> optPays = paysRepository.findById(id) ;
			if(optPays.isPresent()) return optPays.get() ;
			else return null ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Pays> rechercher(Pays pays) throws Exception {
		try {
			Iterable<Pays> itPayss = paysRepository.findAll() ;
			List<Pays> listePayss = new ArrayList() ;
			itPayss.forEach(listePayss::add);
			return listePayss ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Pays> rechercherTout (Pays pays) throws Exception {
		try {
			Iterable<Pays> itPayss = paysRepository.findAll() ;
			List<Pays> listePayss = new ArrayList() ;
			itPayss.forEach(listePayss::add);
			return listePayss ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
}
