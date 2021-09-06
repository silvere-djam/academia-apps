package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.security.data.Localisation;
import cm.deepdream.academia.security.data.Utilisateur ;
import cm.deepdream.academia.souscription.data.Etablissement;
@Controller
@SessionAttributes({"etablissementCourant", "listeUtilisateurs"})
public class LocalisationCtrl implements Serializable{
	private Logger logger = Logger.getLogger(LocalisationCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;

	
	@GetMapping ("/administration/localisations")
	public String getAll (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Utilisateur[]> response0 = rest.getForEntity("http://academia-security/ws/utilisateur/etablissement/{idEtablissement}", 
				 Utilisateur[].class, etablissement.getId()) ;
		 List<Utilisateur> listeUtilisateurs = Arrays.asList(response0.getBody());
		 
		 model.addAttribute("listeUtilisateurs", listeUtilisateurs) ;
		 
		 Utilisateur utilisateur = listeUtilisateurs.size() == 0 ? new Utilisateur():listeUtilisateurs.get(0) ;
		
		 ResponseEntity<Localisation[]> response1 = rest.getForEntity("http://academia-security/ws/localisation/etablissement/{idEtablissement}/utilisateur/{idUtilisateur}", 
				 Localisation[].class, etablissement.getId(), utilisateur.getId()==null ? 0L:utilisateur.getId()) ;
		 List<Localisation> listeLocalisations = Arrays.asList(response1.getBody());
		 model.addAttribute("listeLocalisations", listeLocalisations) ;
		 
		 Localisation localisation = new Localisation() ;
		 localisation.setUtilisateur(utilisateur);
		 model.addAttribute("localisation", localisation) ;
		 return "administration/localisations" ;
	}
	
	@PostMapping ("/administration/localisation/recherche")
	public String getByUtilisateur (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@SessionAttribute ("listeUtilisateurs") List<Utilisateur> listeUtilisateurs, Localisation localisation) throws Exception {
		 model.addAttribute("listeUtilisateurs", listeUtilisateurs) ;
		
		 ResponseEntity<Localisation[]> response1 = rest.getForEntity("http://academia-security/ws/localisation/etablissement/{idEtablissement}/utilisateur/{idUtilisateur}", 
				 Localisation[].class, etablissement.getId(), localisation.getUtilisateur().getId()) ;
		 List<Localisation> listeLocalisations = Arrays.asList(response1.getBody());
		 model.addAttribute("listeLocalisations", listeLocalisations) ;
		 
		 model.addAttribute("localisation", localisation) ;
		 return "administration/localisations" ;
	}
	

}
