package cm.deepdream.academia.viescolaire.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.viescolaire.repository.UERepository;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Etablissement;
@Transactional
@Service
public class UEService {
	private Logger logger = Logger.getLogger(UEService.class.getName()) ;
	@Autowired
	private UERepository uERepository ;
	
	
	public UE rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			UE ue = uERepository.findByIdAndEtablissement(id, etablissement) ;
			return ue ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<UE> rechercher (UE ue) throws Exception {
		try {
			Iterable<UE> source = uERepository.findAll() ;
			List<UE> cible = new ArrayList<UE>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<UE> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<UE> liste = uERepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<UE> rechercher (Classe classe) throws Exception {
		try {
			List<UE> liste = uERepository.findByClasse(classe) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
