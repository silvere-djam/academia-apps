package cm.deepdream.academia.programmation.service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cm.deepdream.academia.programmation.repository.IndicateurRepository;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Indicateur;

@Service
public class IndicateurService {
	private Logger logger = Logger.getLogger(IndicateurService.class.getName()) ;
	@Autowired
	private IndicateurRepository indicateurRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	
	public Indicateur creer (Indicateur indicateur) throws Exception{
		try {
			indicateur.setId(sequenceDAO.nextGlobalId(Indicateur.class.getName()));
			indicateur.setNum(sequenceDAO.nextId(indicateur.getEtablissement(), Indicateur.class.getName()));
			indicateurRepository.save(indicateur) ;
			return indicateur ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Indicateur modifier (Indicateur indicateur) throws Exception{
		try {
			indicateurRepository.save(indicateur) ;
			return indicateur ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idIndicateur) throws Exception {
		try {
			Indicateur indicateur = indicateurRepository.findById(idIndicateur).orElseThrow(Exception::new) ;
			indicateurRepository.delete(indicateur) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Indicateur indicateur) throws Exception{
		try {
			indicateurRepository.delete(indicateur) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Indicateur rechercher (long idIndicateur) throws Exception {
		try {
			Indicateur indicateur = indicateurRepository.findById(idIndicateur).orElseThrow(Exception::new) ;
			return indicateur ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Indicateur rechercher (Etablissement etablissement, Long idIndicateur) throws Exception {
		try {
			Indicateur indicateur = indicateurRepository.findByNumAndEtablissement(idIndicateur, etablissement) ;
			return indicateur ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Indicateur> rechercher (Indicateur indicateur) throws Exception {
		try {
			Iterable<Indicateur> source = indicateurRepository.findAll() ;
			List<Indicateur> cible = new ArrayList<Indicateur>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Indicateur> rechercher (Etablissement etablissement, Classe classe, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<Indicateur> liste = indicateurRepository.findByEtablissementAndClasseAndAnneeScolaire(etablissement, classe, anneeScolaire) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Indicateur> rechercher (Etablissement etablissement, UE ue, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<Indicateur> liste = indicateurRepository.findByEtablissementAndDisciplineAndAnneeScolaire(etablissement, ue, anneeScolaire) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
