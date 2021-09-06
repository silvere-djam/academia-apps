package cm.deepdream.academia.viescolaire.service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.viescolaire.repository.AnneeScolaireRepository;
import cm.deepdream.academia.viescolaire.repository.EtablissementRepository;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;
@Transactional
@Service
public class AnneeScolaireService {
	private Logger logger = Logger.getLogger(AnneeScolaireService.class.getName()) ;
	@Autowired
	private AnneeScolaireRepository anneeScolaireRepository ;
	
	
	public AnneeScolaire rechercher (Etablissement etablissement, long id) throws Exception {
		try {
			AnneeScolaire anneeScolaire = anneeScolaireRepository.findByIdAndEtablissement(id, etablissement) ;
			return anneeScolaire ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public AnneeScolaire rechercherCourant (Etablissement etablissement) throws Exception {
		try {
			AnneeScolaire anneeScolaire = anneeScolaireRepository.rechercherCourant (etablissement) ;
			return anneeScolaire ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<AnneeScolaire> rechercher (AnneeScolaire anneeScolaire) throws Exception {
		try {
			Iterable<AnneeScolaire> source = anneeScolaireRepository.findAll() ;
			List<AnneeScolaire> cible = new ArrayList<AnneeScolaire>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<AnneeScolaire> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<AnneeScolaire> liste = anneeScolaireRepository.findByEtablissement (etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
