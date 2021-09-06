package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import java.time.LocalDateTime;
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

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant"})
public class PaysCtrl implements Serializable{
	private Logger logger = Logger.getLogger(PaysCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/pays")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		ResponseEntity<Pays[]> response = rest.getForEntity("http://academia-programmation/ws/pays/all", Pays[].class, etablissement.getId()) ;
		List<Pays> listePays = Arrays.asList(response.getBody());
		model.addAttribute("listePays", listePays) ;
		return "parametrage/pays" ;
	}
	
	@GetMapping ("/parametrage/ajout-pays")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Pays pays = new Pays() ;
		model.addAttribute("pays", pays) ;
		return "parametrage/ajout-pays" ;
	}
	
	@PostMapping ("/parametrage/pays/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 Pays pays) throws Exception {
		try {
			rest.postForEntity("http://academia-programmation/ws/pays/ajout", pays, Pays.class);			
			return "redirect:/parametrage/pays" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("pays", pays) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-pays" ;
		}
	}
	
	
	@GetMapping ("/parametrage/maj-pays/{id}")
	public String initMaj (Model model, @PathVariable("id") Long id, 
			@SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		Pays pays = rest.getForObject("http://academia-programmation/ws/pays/id/{id}", Pays.class, id) ;
		model.addAttribute("paysExistant", pays) ;
		return "parametrage/maj-pays" ;
	}
	
	
	@PostMapping ("/parametrage/pays/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			Pays paysExistant) throws Exception {
		try {
			Pays pays = rest.getForObject("http://academia-programmation/ws/pays/id/{id}", 
					Pays.class,  paysExistant.getId()) ;
			pays.setCode(paysExistant.getCode()) ;
			pays.setLibelle(paysExistant.getLibelle()) ;
			pays.setCodeTel(paysExistant.getCodeTel());
			pays.setMonnaie(paysExistant.getMonnaie());
			rest.put("http://academia-programmation/ws/pays/maj", pays, Pays.class);
			return "redirect:/parametrage/pays" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("paysExistant", paysExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/maj-pays" ;
		}
	}
	
	
}
