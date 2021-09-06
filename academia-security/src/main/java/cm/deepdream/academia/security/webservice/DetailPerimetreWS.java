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
import cm.deepdream.academia.security.data.DetailPerimetre;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.security.service.EtablissementService;
import cm.deepdream.academia.security.service.UtilisateurService;
import cm.deepdream.academia.security.service.DetailPerimetreService;

@RestController
@RequestMapping("/ws/perimetre")
public class DetailPerimetreWS {
	private Logger logger = Logger.getLogger(DetailPerimetreWS.class.getName()) ;
	@Autowired
	private DetailPerimetreService detailPerimetreService ;
	@Autowired
	private EtablissementService etablissementService ;
	@Autowired
	private UtilisateurService utilisateurService ;
	
	@PostMapping("/ajout")
	public DetailPerimetre ajout (@RequestBody DetailPerimetre detailPerimetre) {
		try {
			DetailPerimetre detailPerimetreCree = detailPerimetreService.creer(detailPerimetre) ;
			return detailPerimetreCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public DetailPerimetre maj (@RequestBody DetailPerimetre detailPerimetre) {
		try {
			DetailPerimetre detailPerimetreMaj = detailPerimetreService.modifier(detailPerimetre) ;
			return detailPerimetreMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (DetailPerimetre detailPerimetre) {
		try {
			detailPerimetreService.supprimer(detailPerimetre) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	
	@GetMapping("/etablissement/{idEtablissement}/utilisateur/{idUtilisateur}")
	public List<DetailPerimetre> getByUser(@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idUtilisateur") Long idUtilisateur) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Utilisateur utilisateur = utilisateurService.rechercher(etablissement, idUtilisateur) ;
			List<DetailPerimetre> listeDetailsPerimetre = detailPerimetreService.rechercher(utilisateur) ;
			return listeDetailsPerimetre ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<DetailPerimetre>() ;
		}
	}
	

	
}
