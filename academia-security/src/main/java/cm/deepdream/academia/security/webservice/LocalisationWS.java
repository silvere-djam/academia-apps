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
import cm.deepdream.academia.security.data.Localisation;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.security.service.EtablissementService;
import cm.deepdream.academia.security.service.UtilisateurService;
import cm.deepdream.academia.security.service.LocalisationService;

@RestController
@RequestMapping("/ws/localisation")
public class LocalisationWS {
	private Logger logger = Logger.getLogger(LocalisationWS.class.getName()) ;
	@Autowired
	private LocalisationService localisationService ;
	@Autowired
	private EtablissementService etablissementService ;
	@Autowired
	private UtilisateurService utilisateurService ;
	
	@PostMapping("/ajout")
	public Localisation ajout (@RequestBody Localisation localisation) {
		try {
			Localisation localisationCree = localisationService.creer(localisation) ;
			return localisationCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Localisation maj (@RequestBody Localisation localisation) {
		try {
			Localisation localisationMaj = localisationService.modifier(localisation) ;
			return localisationMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (Localisation localisation) {
		try {
			localisationService.supprimer(localisation) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/utilisateur/{idUtilisateur}")
	public List<Localisation> getByUser(@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idUtilisateur") Long idUtilisateur) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Utilisateur utilisateur = utilisateurService.rechercher(etablissement, idUtilisateur) ;
			List<Localisation> listeLocalisations = localisationService.rechercher(utilisateur) ;
			return listeLocalisations ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Localisation>() ;
		}
	}
	

	
}
