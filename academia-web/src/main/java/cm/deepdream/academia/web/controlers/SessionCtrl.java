package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.security.data.Session;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.souscription.data.Etablissement;

@Controller
@SessionAttributes({"sessionCourante", "etablissementCourant"})
public class SessionCtrl implements Serializable{
	private Logger logger = Logger.getLogger(SessionCtrl.class.getName()) ;
	@Autowired
	//@LoadBalanced 
	private RestTemplate rest ;
	
	@GetMapping ("/administration/sessions")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		ResponseEntity<Session[]> response = rest.getForEntity("http://academia-security/ws/session/etablissement/{id}", 
				Session[].class, etablissement.getId()) ;
		List<Session> listeSessions = Arrays.asList(response.getBody());
		model.addAttribute("listeSessions", listeSessions) ;
		return "administration/sessions" ;
	}
	
	@GetMapping ("/administration/sessions/all")
	public String tous (Model model) throws Exception {
		return "administration/sessions" ;
	}
	
	@GetMapping ("/administration/sessions/profil/{idProfil}")
	public String parProfil (Model model) throws Exception {
		return "administration/sessions" ;
	}
	
	@GetMapping ("/administration/ajout-session")
	public String initAjout (Model model) throws Exception {
		return "administration/ajout-session" ;
	}
	
	@PostMapping ("/administration/session/ajout")
	public String ajouter (Model model) throws Exception {
		return "administration/sessions" ;
	}
	
	 @GetMapping ("/administration/historique-connexions")
	 public String historiqueConnexions (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			 @SessionAttribute ("etablissementCourante") Etablissement etablissement) {
		 ResponseEntity<Session[]> response = rest.getForEntity(String.format("http://academia-securite/ws/session/etablissement/%d/utilisateur/%d", etablissement.getId(), utilisateurCourant.getId()), Session[].class) ;
		 List<Session> listeSessions = Arrays.asList(response.getBody());
		 model.addAttribute("listeSessions", listeSessions) ;
		 model.addAttribute("utilisateurCourant", utilisateurCourant) ;
	     return "administration/historique-connexions" ;
	 }

}
