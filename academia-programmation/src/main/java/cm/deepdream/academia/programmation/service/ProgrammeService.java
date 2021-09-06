package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.ProgrammeRepository;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Programme;

@Transactional
@Service
public class ProgrammeService {
	private Logger logger = Logger.getLogger(ProgrammeService.class.getName()) ;
	@Autowired
	private ProgrammeRepository programmeRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	
	public Programme creer (Programme programme) throws Exception{
		try {
			programme.setId(sequenceDAO.nextGlobalId(Programme.class.getName()));
			programme.setNum(sequenceDAO.nextId(programme.getEtablissement(), Programme.class.getName()));
			programme.setDateCreation(LocalDateTime.now()) ;
			programme.setDateDernMaj(LocalDateTime.now()) ;
			programme.setTauxCouverture(0.0f);
			programme.setTauxHoraire(0.0f);
			programmeRepository.save(programme) ;
			return programme ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Programme modifier (Programme programme) throws Exception{
		try {
			programme.setDateDernMaj(LocalDateTime.now()) ;
			programmeRepository.save(programme) ;
			return programme ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idProgramme) throws Exception {
		try {
			Programme programme = programmeRepository.findById(idProgramme).orElseThrow(Exception::new) ;
			programmeRepository.delete(programme) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Programme programme) throws Exception{
		try {
			programmeRepository.delete(programme) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Programme rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			Programme programme = programmeRepository.findByIdAndEtablissement(id, etablissement) ;
			return programme ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Programme rechercher (Etablissement etablissement, AnneeScolaire anneeScolaire, 
			UE ue) throws Exception {
		try {
			Programme programme = programmeRepository.findByEtablissementAndAnneeScolaireAndUe(etablissement, anneeScolaire,
					ue) ;
			return programme ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Programme> rechercher (Etablissement etablissement, AnneeScolaire anneeScolaire) throws Exception {
		try {
			List<Programme> listeProgrammes = programmeRepository.findByEtablissementAndAnneeScolaire(etablissement, anneeScolaire) ;
			return listeProgrammes ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Programme> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Programme> listeProgrammes = programmeRepository.findByEtablissement(etablissement) ;
			return listeProgrammes ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Programme> rechercher (Programme programme) throws Exception {
		try {
			Iterable<Programme> source = programmeRepository.findAll() ;
			List<Programme> cible = new ArrayList<Programme>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
}
