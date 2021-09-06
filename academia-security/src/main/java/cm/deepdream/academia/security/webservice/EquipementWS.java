package cm.deepdream.academia.security.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.security.data.Equipement;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.security.service.EtablissementService;
import cm.deepdream.academia.security.service.UtilisateurService;
import cm.deepdream.academia.security.service.EquipementService;

@RestController
@RequestMapping("/ws/equipement")
public class EquipementWS {
	private Logger logger = Logger.getLogger(EquipementWS.class.getName()) ;
	@Autowired
	private EquipementService equipementService ;
	@Autowired
	private EtablissementService etablissementService ;
	@Autowired
	private UtilisateurService utilisateurService ;
	
	@PostMapping("/ajout")
	public Equipement ajout (@RequestBody Equipement equipement) {
		try {
			Equipement equipementCree = equipementService.creer(equipement) ;
			return equipementCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Equipement maj (@RequestBody Equipement equipement) {
		try {
			Equipement equipementMaj = equipementService.modifier(equipement) ;
			return equipementMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (Equipement equipement) {
		try {
			equipementService.supprimer(equipement) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/utilisateur/{idUtilisateur}")
	public List<Equipement> getByUser(@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idUtilisateur") Long idUtilisateur) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Utilisateur utilisateur = utilisateurService.rechercher(etablissement, idUtilisateur) ;
			List<Equipement> listeEquipements = equipementService.rechercher(utilisateur) ;
			return listeEquipements ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Equipement>() ;
		}
	}
	

	
}
