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
import cm.deepdream.academia.programmation.data.Niveau;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant"})
public class NiveauCtrl implements Serializable{
	private Logger logger = Logger.getLogger(NiveauCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/niveaux")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Niveau[]> response = rest.getForEntity("http://academia-programmation/ws/niveau/etablissement/{idEtablissement}", 
				 Niveau[].class, etablissement.getId()) ;
		 List<Niveau> listeNiveaux = Arrays.asList(response.getBody());
		 model.addAttribute("listeNiveaux", listeNiveaux) ;
		return "parametrage/niveaux" ;
	}
	
	@GetMapping ("/parametrage/ajout-niveau")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Niveau niveau = new Niveau() ;
		model.addAttribute("niveau", niveau) ;
		return "parametrage/ajout-niveau" ;
	}
	
	@PostMapping ("/parametrage/niveau/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissementCourant,  Niveau niveau) throws Exception {
		try {
			niveau.setCreateur(utilisateurCourant.getEmail());
			niveau.setModificateur(utilisateurCourant.getEmail());
			niveau.setEtablissement(etablissementCourant) ;
			rest.postForEntity("http://academia-programmation/ws/niveau/ajout", niveau, Niveau.class);			
			return "redirect:/parametrage/niveaux" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("niveau", niveau) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-niveau" ;
		}
	}
	
	
	@GetMapping ("/parametrage/maj-niveau/{id}")
	public String initMaj (Model model, @PathVariable("id") long id, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		Niveau niveau = rest.getForObject("http://academia-programmation/ws/niveau/etablissement/{idEtablissement}/id/{id}", 
				Niveau.class, etablissement.getId(), id) ;
		model.addAttribute("niveauExistant", niveau) ;
		return "parametrage/maj-niveau" ;
	}
	
	@PostMapping ("/parametrage/niveau/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			Niveau niveauExistant) throws Exception {
		try {
			Niveau niveau = rest.getForObject("http://academia-programmation/ws/niveau/etablissement/{idEtablissement}/id/{id}", Niveau.class, etablissement.getId(), niveauExistant.getId()) ;
			niveau.setModificateur(utilisateurCourant.getEmail()) ;
			niveau.setAbreviation(niveauExistant.getAbreviation()) ;
			niveau.setLibelle(niveauExistant.getLibelle()) ;
			rest.put("http://academia-programmation/ws/niveau/maj", niveau, Niveau.class);
			return "redirect:/parametrage/niveaux" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("niveauExistant", niveauExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/maj-niveau" ;
		}
	}
	
	
	@GetMapping ("/parametrage/details-niveau/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		Niveau niveau = rest.getForObject("http://academia-programmation/ws/niveau/etablissement/{idEtablissement}/id/{id}", Niveau.class, etablissement.getId(), id) ;
		model.addAttribute("niveauExistant", niveau) ;
		return "parametrage/details-niveau" ;
	}
	
	
}
