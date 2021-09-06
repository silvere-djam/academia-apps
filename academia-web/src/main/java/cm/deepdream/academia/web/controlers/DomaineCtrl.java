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
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.souscription.data.Etablissement;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant"})
public class DomaineCtrl implements Serializable{
	private Logger logger = Logger.getLogger(DomaineCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/domaines")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Domaine[]> response = rest.getForEntity("http://academia-programmation/ws/domaine/etablissement/{idEtablissement}", 
				 Domaine[].class, etablissement.getId()) ;
		 List<Domaine> listeDomainex = Arrays.asList(response.getBody());
		 model.addAttribute("listeDomaines", listeDomainex) ;
		return "parametrage/domaines" ;
	}
	
	@GetMapping ("/parametrage/ajout-domaine")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Domaine domaine = new Domaine() ;
		domaine.setEtablissement(etablissement);
		model.addAttribute("domaine", domaine) ;
		return "parametrage/ajout-domaine" ;
	}
	
	@PostMapping ("/parametrage/domaine/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissementCourant,  Domaine domaine) throws Exception {
		try {
			domaine.setCreateur(utilisateurCourant.getEmail());
			domaine.setModificateur(utilisateurCourant.getEmail());
			domaine.setEtablissement(etablissementCourant) ;
			rest.postForEntity("http://academia-programmation/ws/domaine/ajout", domaine, Domaine.class);			
			return "redirect:/parametrage/domaines" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("domaine", domaine) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-domaine" ;
		}
	}
	
	
	@GetMapping ("/parametrage/maj-domaine/{id}")
	public String initMaj (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Domaine domaine = rest.getForObject("http://academia-programmation/ws/domaine/etablissement/{idEtablissement}/id/{id}", 
				Domaine.class, etablissement.getId(),  id) ;
		model.addAttribute("domaineExistant", domaine) ;
		return "parametrage/maj-domaine" ;
	}
	
	@PostMapping ("/parametrage/domaine/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, @SessionAttribute("etablissementCourant") Etablissement etablissement, 
			Domaine domaineExistant) throws Exception {
		try {
			Domaine domaine = rest.getForObject("http://academia-programmation/ws/domaine/etablissement/{idEtablissement}/id/{id}", 
					Domaine.class, etablissement.getId(), domaineExistant.getId()) ;
			domaine.setModificateur(utilisateurCourant.getEmail()) ;
			domaine.setLibelle(domaineExistant.getLibelle()) ;
			rest.put("http://academia-programmation/ws/domaine/maj", domaine, Domaine.class);
			return "redirect:/parametrage/domaines" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("domaineExistante", domaineExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/maj-domaine" ;
		}
	}
	
	
	@GetMapping ("/parametrage/details-domaine/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Domaine domaine = rest.getForObject("http://academia-programmation/ws/domaine/etablissement/{idEtablissement}/id/{id}", 
				Domaine.class, etablissement.getId(),  id) ;
		model.addAttribute("domaineExistant", domaine) ;
		return "parametrage/details-domaine" ;
	}
	
	
}
