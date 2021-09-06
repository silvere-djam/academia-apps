package cm.deepdream.academia.viescolaire.service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.viescolaire.repository.ClasseRepository;
import cm.deepdream.academia.viescolaire.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.souscription.data.Etablissement;
@Transactional
@Service
public class ClasseService {
	private Logger logger = Logger.getLogger(ClasseService.class.getName()) ;
	@Autowired
	private ClasseRepository classeRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
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
