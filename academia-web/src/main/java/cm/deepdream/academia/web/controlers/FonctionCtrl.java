package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.programmation.data.Fonction;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.souscription.data.Etablissement;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant"})
public class FonctionCtrl implements Serializable{
	private Logger logger = Logger.getLogger(FonctionCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/fonctions")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Fonction[]> response = rest.getForEntity("http://academia-programmation/ws/fonction/etablissement/{idEtablissement}", 
				 Fonction[].class, etablissement.getId()) ;
		 List<Fonction> listeFonctions = Arrays.asList(response.getBody());
		 model.addAttribute("listeFonctions", listeFonctions) ;
		return "parametrage/fonctions" ;
	}
	
	@GetMapping ("/parametrage/ajout-fonction")
	public String initAjout (Model model) throws Exception {
		Fonction fonction = new Fonction() ;
		model.addAttribute("fonction", fonction) ;
		return "parametrage/ajout-fonction" ;
	}
	
	@PostMapping ("/parametrage/fonction/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  Fonction fonction) throws Exception {
		try {
			fonction.setCreateur(utilisateurCourant.getEmail());
			fonction.setModificateur(utilisateurCourant.getEmail());
			fonction.setEtablissement(etablissement) ;
			rest.postForEntity("http://academia-programmation/ws/fonction/ajout", fonction, Fonction.class);			
			return "redirect:/parametrage/fonctions" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("fonction", fonction) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-fonction" ;
		}
	}
	
	
	@GetMapping ("/parametrage/maj-fonction/{id}")
	public String initMaj (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Fonction fonction = rest.getForObject("http://academia-programmation/ws/fonction/etablissement/{idEtablissement}/id/{id}", 
				Fonction.class, etablissement.getId(), id) ;
		model.addAttribute("fonctionExistante", fonction) ;
		return "parametrage/maj-fonction" ;
	}
	
	@PostMapping ("/parametrage/fonction/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			Fonction fonctionExistante, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		try {
			Fonction fonction = rest.getForObject("http://academia-programmation/ws/fonction//etablissement/{idEtablissement}/id/{id}", 
					Fonction.class, etablissement.getId(), fonctionExistante.getId()) ;
			fonction.setModificateur(utilisateurCourant.getEmail()) ;
			fonction.setLibelle(fonctionExistante.getLibelle()) ;
			rest.put("http://academia-programmation/ws/fonction/maj", fonction, Fonction.class);
			return "redirect:/parametrage/fonctions" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("fonctionExistante", fonctionExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/maj-fonction" ;
		}
	}
	
	
	@GetMapping ("/parametrage/details-fonction/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Fonction fonction = rest.getForObject("http://academia-programmation/ws/fonction/etablissement/{idEtablissement}/id/{id}", 
				Fonction.class, etablissement.getId(), id) ;
		model.addAttribute("fonctionExistante", fonction) ;
		return "parametrage/details-fonction" ;
	}
	
	@PostMapping ("/parametrage/fonction/suppr")
	public String suppression (Model model, @SessionAttribute("etablissementCourant") Etablissement etablissement, Fonction fonctionExistante) throws Exception {
		try {
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", fonctionExistante.getId()) ;
			uriVariables.put("idEtablissement", etablissement.getId()) ;
			rest.delete("http://academia-programmation/ws/fonction/suppr/etablissement/{idEtablissement}/id/{id}", 
					uriVariables);
			return "redirect:/parametrage/fonctions" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("fonctionExistante", fonctionExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/details-fonction" ;
		}
	}
	
	
}
