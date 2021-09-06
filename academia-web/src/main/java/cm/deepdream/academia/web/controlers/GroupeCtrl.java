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
import cm.deepdream.academia.programmation.data.Groupe;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant"})
public class GroupeCtrl implements Serializable{
	private Logger logger = Logger.getLogger(GroupeCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/groupes")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		ResponseEntity<Groupe[]> response = rest.getForEntity("http://academia-programmation/ws/groupe/etablissement/{idEtablissement}", Groupe[].class, etablissement.getId()) ;
		List<Groupe> listeGroupes = Arrays.asList(response.getBody());
		model.addAttribute("listeGroupes", listeGroupes) ;
		return "parametrage/groupes" ;
	}
	
	@GetMapping ("/parametrage/ajout-groupe")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Groupe groupe = new Groupe() ;
		model.addAttribute("groupe", groupe) ;
		return "parametrage/ajout-groupe" ;
	}
	
	@PostMapping ("/parametrage/groupe/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  Groupe groupe) throws Exception {
		try {
			groupe.setCreateur(utilisateurCourant.getEmail());
			groupe.setModificateur(utilisateurCourant.getEmail());
			groupe.setEtablissement(etablissement) ;
			rest.postForEntity("http://academia-programmation/ws/groupe/ajout", groupe, Groupe.class);			
			return "redirect:/parametrage/groupes" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("groupe", groupe) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-groupe" ;
		}
	}
	
	
	@GetMapping ("/parametrage/maj-groupe/{id}")
	public String initMaj (Model model, @PathVariable("id") long id, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		Groupe groupe = rest.getForObject("http://academia-programmation/ws/groupe/etablissement/{idEtablissement}/id/{id}", 
				Groupe.class, etablissement.getId(), id) ;
		model.addAttribute("groupeExistant", groupe) ;
		return "parametrage/maj-groupe" ;
	}
	
	@PostMapping ("/parametrage/groupe/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			Groupe groupeExistant) throws Exception {
		try {
			Groupe groupe = rest.getForObject("http://academia-programmation/ws/groupe/etablissement/{idEtablissement}/id/{id}", 
					Groupe.class, etablissement.getId(), groupeExistant.getId()) ;
			groupe.setDateDernMaj(LocalDateTime.now()) ;
			groupe.setModificateur(utilisateurCourant.getEmail()) ;
			groupe.setLibelle(groupeExistant.getLibelle()) ;
			rest.put("http://academia-programmation/ws/groupe/maj", groupe, Groupe.class);
			return "redirect:/parametrage/groupes" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("groupeExistant", groupeExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/maj-groupe" ;
		}
	}
	
	@GetMapping ("/parametrage/details-groupe/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		Groupe groupe = rest.getForObject("http://academia-programmation/ws/groupe/etablissement/{idEtablissement}/id/{id}", 
				Groupe.class, etablissement.getId(), id) ;
		model.addAttribute("groupeExistant", groupe) ;
		return "parametrage/details-groupe" ;
	}
	
	
}
