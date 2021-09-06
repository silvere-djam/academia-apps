package cm.deepdream.academia.viescolaire.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.viescolaire.repository.DomaineRepository;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.viescolaire.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.enums.Action;

@Transactional
@Service
public class DomaineService {
	private Logger logger = Logger.getLogger(DomaineService.class.getName()) ;
	@Autowired
	private DomaineRepository domaineRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	public Domaine rechercher (Long id) throws Exception {
		try {
			Domaine domaine = domaineRepository.findById(id).orElseThrow(Exception::new) ;
			return domaine ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Domaine rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Domaine domaine = domaineRepository.findByIdAndEtablissement(id, etablissement) ;
			return domaine ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Domaine> rechercher (Domaine domaine) throws Exception {
		try {
			Iterable<Domaine> source = domaineRepository.findAll() ;
			List<Domaine> cible = new ArrayList<Domaine>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Domaine> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Domaine> liste = domaineRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
}
