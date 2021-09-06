package cm.deepdream.academia.security.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.security.dao.SequenceDAO;
import cm.deepdream.academia.security.repository.DetailPerimetreRepository;
import cm.deepdream.academia.security.repository.EquipementRepository;
import cm.deepdream.academia.security.repository.UtilisateurRepository;
import cm.deepdream.academia.security.data.DetailPerimetre;
import cm.deepdream.academia.security.data.Equipement;
import cm.deepdream.academia.security.data.Utilisateur;
@Transactional
@Service
public class DetailPerimetreService {
	private Logger logger = Logger.getLogger(DetailPerimetreService.class.getName()) ;
	@Autowired
	private DetailPerimetreRepository detailPerimetreRepository ;
	@Autowired
	private UtilisateurRepository utilisateurRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	public DetailPerimetre creer (DetailPerimetre detailPerimetre) throws Exception {
		try {
			detailPerimetre.setId(sequenceDAO.nextGlobalId(DetailPerimetre.class.getName())) ;
			detailPerimetre.setNum(sequenceDAO.nextId(detailPerimetre.getEtablissement(), DetailPerimetre.class.getName())) ;
			DetailPerimetre detailPerimetreCree = detailPerimetreRepository.save(detailPerimetre) ;
			return detailPerimetreCree ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}	
	
	public DetailPerimetre modifier (DetailPerimetre detailPerimetre) throws Exception {
		try {
			DetailPerimetre detailPerimetreMaj = detailPerimetreRepository.save(detailPerimetre) ;
			return detailPerimetreMaj ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (DetailPerimetre detailPerimetre) throws Exception {
		try {
			detailPerimetreRepository.delete(detailPerimetre) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (long idDetailPerimetre) throws Exception {
		try {
			Optional<DetailPerimetre> optDetailPerimetre = detailPerimetreRepository.findById(idDetailPerimetre) ;
			if(optDetailPerimetre.isPresent())
				detailPerimetreRepository.delete(optDetailPerimetre.get()) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public List<DetailPerimetre> rechercher(Utilisateur utilisateur) throws Exception {
		try {
			List<DetailPerimetre> listeDetailsPerimetre = detailPerimetreRepository.findByUtilisateur(utilisateur) ;
			return listeDetailsPerimetre ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<DetailPerimetre> rechercher (DetailPerimetre detailPerimetre) throws Exception {
		try {
			Iterable<DetailPerimetre> itDetailsPerimetre = detailPerimetreRepository.findAll() ;
			List<DetailPerimetre> listeDetailsPerimetre = new ArrayList() ;
			itDetailsPerimetre.forEach(listeDetailsPerimetre::add);
			return listeDetailsPerimetre ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
}
