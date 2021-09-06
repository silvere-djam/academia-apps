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
import cm.deepdream.academia.security.repository.EquipementRepository;
import cm.deepdream.academia.security.repository.UtilisateurRepository;
import cm.deepdream.academia.security.data.Equipement;
import cm.deepdream.academia.security.data.Localisation;
import cm.deepdream.academia.security.data.Utilisateur;
@Transactional
@Service
public class EquipementService {
	private Logger logger = Logger.getLogger(EquipementService.class.getName()) ;
	@Autowired
	private EquipementRepository equipementRepository ;
	@Autowired
	private UtilisateurRepository utilisateurRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	public Equipement creer (Equipement equipement) throws Exception {
		try {
			equipement.setId(sequenceDAO.nextGlobalId(Equipement.class.getName())) ;
			equipement.setNum(sequenceDAO.nextId(equipement.getEtablissement(), Equipement.class.getName())) ;
			equipementRepository.save(equipement) ;
			return equipement ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}	
	
	public Equipement modifier (Equipement equipement) throws Exception {
		try {
			equipementRepository.save(equipement) ;
			return equipement ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (Equipement equipement) throws Exception {
		try {
			equipementRepository.delete(equipement) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (long idEquipement) throws Exception {
		try {
			Optional<Equipement> optEquipement = equipementRepository.findById(idEquipement) ;
			if(optEquipement.isPresent())
				equipementRepository.delete(optEquipement.get()) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public List<Equipement> rechercher(Utilisateur utilisateur) throws Exception {
		try {
			List<Equipement> listeEquipements = equipementRepository.findByUtilisateur(utilisateur) ;
			return listeEquipements ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Equipement> rechercher (Equipement equipement) throws Exception {
		try {
			Iterable<Equipement> itEquipements = equipementRepository.findAll() ;
			List<Equipement> listeEquipements = new ArrayList() ;
			itEquipements.forEach(listeEquipements::add);
			return listeEquipements ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
}
