package cm.deepdream.academia.viescolaire.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.viescolaire.repository.DetailEmploiTempsRepository;
import cm.deepdream.academia.viescolaire.repository.EnseignantRepository;
import cm.deepdream.academia.programmation.util.SerializerToolkit;
import cm.deepdream.academia.viescolaire.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.DetailEmploiTemps;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.souscription.data.Etablissement;

@Transactional
@Service
public class EnseignantService {
	private Logger logger = Logger.getLogger(EnseignantService.class.getName()) ;
	@Autowired
	private EnseignantRepository enseignantRepository ;
	@Autowired
	private DetailEmploiTempsRepository detailEmploiTempsRepository ;

	
	
	public Enseignant rechercher (Long idEnseignant) throws Exception {
		try {
			Enseignant enseignant = enseignantRepository.findById(idEnseignant).orElseThrow(Exception::new) ;
			return enseignant ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Enseignant rechercher (Etablissement etablissement, long idEnseignant) throws Exception {
		try {
			Enseignant enseignant = enseignantRepository.findByIdAndEtablissement(idEnseignant, etablissement) ;
			return enseignant ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Enseignant> rechercher (Enseignant enseignant) throws Exception {
		try {
			Iterable<Enseignant> source = enseignantRepository.findAll() ;
			List<Enseignant> cible = new ArrayList<Enseignant>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Enseignant> rechercher (Etablissement etablissement, Classe classe) throws Exception {
		try {
			List<DetailEmploiTemps> listeDetails = detailEmploiTempsRepository.rechercher(etablissement, classe) ;
			List<Enseignant> listeEnseignants = listeDetails.stream().map(d -> d.getEnseignant()).collect(Collectors.toList()) ;
			return listeEnseignants ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	
	public List<Enseignant> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Enseignant> liste = enseignantRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Enseignant> rechercher (Etablissement etablissement, Domaine domaine) throws Exception {
		try {
			List<Enseignant> liste = enseignantRepository.findByEtablissementAndDomaine(etablissement, domaine) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
