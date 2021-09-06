package cm.deepdream.academia.viescolaire.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.viescolaire.repository.EleveRepository;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.souscription.data.Etablissement;

@Transactional
@Service
public class EleveService {
	private Logger logger = Logger.getLogger(EleveService.class.getName()) ;
	@Autowired
	private EleveRepository eleveRepository ;

	
	public Eleve rechercher (Long idEleve) throws Exception {
		try {
			Eleve eleve = eleveRepository.findById(idEleve).orElseThrow(Exception::new) ;
			return eleve ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Eleve rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Eleve eleve = eleveRepository.findByIdAndEtablissement(id, etablissement) ;
			return eleve ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Eleve> rechercher (Eleve eleve) throws Exception {
		try {
			Iterable<Eleve> source = eleveRepository.findAll() ;
			List<Eleve> cible = new ArrayList<Eleve>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Eleve> rechercher (String situation, Etablissement etablissement) throws Exception {
		try {
			List<Eleve> liste = eleveRepository.findByEtablissementAndStatut(etablissement, situation) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Eleve> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Eleve> liste = eleveRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Eleve> rechercher (Etablissement etablissement, Classe classe) throws Exception {
		try {
			List<Eleve> liste = eleveRepository.findByEtablissementAndClasse(etablissement, classe) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public long compter (Etablissement etablissement) throws Exception {
		try {
			long nombre = eleveRepository.countByEtablissement(etablissement);
			return nombre ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
