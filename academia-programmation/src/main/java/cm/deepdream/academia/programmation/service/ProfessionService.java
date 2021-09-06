package cm.deepdream.academia.programmation.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.programmation.repository.ProfessionRepository;
import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Profession;
import cm.deepdream.academia.programmation.data.UE;
@Transactional
@Service
public class ProfessionService {
	private Logger logger = Logger.getLogger(ProfessionService.class.getName()) ;
	@Autowired
	private ProfessionRepository professionRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	
	public Profession creer (Profession profession) throws Exception{
		try {
			profession.setId(sequenceDAO.nextGlobalId(Profession.class.getName()));
			profession.setNum(sequenceDAO.nextId(profession.getEtablissement(), Profession.class.getName()));
			profession.setDateCreation(LocalDateTime.now());
			profession.setDateDernMaj(LocalDateTime.now());
			professionRepository.save(profession) ;
			return profession ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Profession modifier (Profession profession) throws Exception{
		try {
			profession.setDateDernMaj(LocalDateTime.now());
			professionRepository.save(profession) ;
			return profession ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idProfession) throws Exception {
		try {
			Profession profession = professionRepository.findById(idProfession).orElseThrow(Exception::new) ;
			professionRepository.delete(profession) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Profession profession) throws Exception{
		try {
			professionRepository.delete(profession) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Profession rechercher (long idProfession) throws Exception {
		try {
			Profession profession = professionRepository.findById(idProfession).orElseThrow(Exception::new) ;
			return profession ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Profession rechercher (Etablissement etablissement, long idProfession) throws Exception {
		try {
			Profession profession = professionRepository.findByIdAndEtablissement(idProfession, etablissement) ;
			return profession ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Profession> rechercher (Profession profession) throws Exception {
		try {
			Iterable<Profession> source = professionRepository.findAll() ;
			List<Profession> cible = new ArrayList<Profession>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Profession> rechercher (Etablissement etablissement) throws Exception {
		try {
			List<Profession> liste = professionRepository.findByEtablissement(etablissement) ;
			return liste ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
