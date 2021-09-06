package cm.deepdream.academia.security.service;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.security.repository.ClasseRepository;
import cm.deepdream.academia.programmation.data.Classe;
@Transactional
@Service
public class ClasseService {
	private Logger logger = Logger.getLogger(ClasseService.class.getName()) ;
	@Autowired
	private ClasseRepository classeRepository ;

	
	
	public Classe rechercher(Etablissement etablissement, Long id) throws Exception {
		try {
			Classe classe = classeRepository.findByIdAndEtablissement(id, etablissement) ;
			return classe ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Classe> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Classe> listeClasses  = classeRepository.findByEtablissement(etablissement);
			return listeClasses ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
}
