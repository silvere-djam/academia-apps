package cm.deepdream.academia.security.webservice;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.security.service.EtablissementService;
import cm.deepdream.academia.security.service.UtilisateurService;

@RestController
@RequestMapping("/ws/auth")
public class AuthentificationWS {
	private Logger logger = Logger.getLogger(AuthentificationWS.class.getName()) ;
	@Autowired
	private UtilisateurService utilisateurService ;
	@Autowired
	private EtablissementService etablissementService ;
	

	@GetMapping("/telephone/{telephone}")
	public Utilisateur getByLogin (@PathVariable("telephone") String telephone) {
		try {
			Utilisateur utilisateur = utilisateurService.rechercherTelephone(telephone) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/email/{email}/pwd/{password}")
	public Utilisateur authEmail (@PathVariable("email") String email, @PathVariable("password") String password) {
		try {
			Utilisateur utilisateur = utilisateurService.rechercherEmail(email) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/telephone/{telephone}/pwd/{password}")
	public Utilisateur authTelephone (@PathVariable("telephone") String telephone, @PathVariable("password") String password) {
		try {
			Utilisateur utilisateur = utilisateurService.rechercherTelephone(telephone) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
}
