package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
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
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Pays;
import cm.deepdream.academia.souscription.data.Localite;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant"})
public class VilleCtrl implements Serializable{
	private Logger logger = Logger.getLogger(VilleCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/villes")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		ResponseEntity<Localite[]> response = rest.getForEntity("http://academia-programmation/ws/ville/all", Localite[].class) ;
		List<Localite> listeVilles = Arrays.asList(response.getBody());
		model.addAttribute("listeVilles", listeVilles) ;
		return "parametrage/villes" ;
	}
	
	@GetMapping ("/parametrage/ajout-ville")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		Localite ville = new Localite() ;
		model.addAttribute("ville", ville) ;
		
		ResponseEntity<Pays[]> response = rest.getForEntity("http://academia-programmation/ws/pays/all", Pays[].class) ;
		model.addAttribute("listePays", response.getBody()) ;
		
		return "parametrage/ajout-ville" ;
	}
	
	@PostMapping ("/parametrage/ville/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 Localite ville) throws Exception {
		try {
			rest.postForEntity("http://academia-programmation/ws/ville/ajout", ville, Localite.class);			
			return "redirect:/parametrage/villes" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("ville", ville) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-ville" ;
		}
	}
	
	
	@GetMapping ("/parametrage/maj-ville/{id}")
	public String initMaj (Model model, @PathVariable("id") Long id, 
			@SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		Localite ville = rest.getForObject("http://academia-programmation/ws/ville/id/{id}", Localite.class, id) ;
		model.addAttribute("villeExistante", ville) ;
		
		ResponseEntity<Pays[]> response = rest.getForEntity("http://academia-programmation/ws/pays/all", Pays[].class) ;
		model.addAttribute("listePays", response.getBody()) ;
		
		return "parametrage/maj-ville" ;
	}
	
	
	@PostMapping ("/parametrage/ville/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			Localite villeExistante) throws Exception {
		try {
			Localite ville = rest.getForObject("http://academia-programmation/ws/ville/id/{id}", 
					Localite.class,  villeExistante.getId()) ;
			ville.setLibelle(villeExistante.getLibelle());
			ville.setPays(villeExistante.getPays());
			rest.put("http://academia-programmation/ws/ville/maj", ville, Localite.class);
			return "redirect:/parametrage/villes" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("villeExistante", villeExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/maj-ville" ;
		}
	}
	
	
}
