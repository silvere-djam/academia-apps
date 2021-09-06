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
import cm.deepdream.academia.programmation.data.Filiere;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant"})
public class FiliereCtrl implements Serializable{
	private Logger logger = Logger.getLogger(FiliereCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/filieres")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Filiere[]> response = rest.getForEntity("http://academia-programmation/ws/filiere/etablissement/{idEtablissement}", 
				 Filiere[].class, etablissement.getId()) ;
		 List<Filiere> listeFilierex = Arrays.asList(response.getBody());
		 model.addAttribute("listeFilieres", listeFilierex) ;
		return "parametrage/filieres" ;
	}
	
	@GetMapping ("/parametrage/ajout-filiere")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Filiere filiere = new Filiere() ;
		model.addAttribute("filiere", filiere) ;
		return "parametrage/ajout-filiere" ;
	}
	
	@PostMapping ("/parametrage/filiere/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  Filiere filiere) throws Exception {
		try {
			filiere.setCreateur(utilisateurCourant.getEmail());
			filiere.setModificateur(utilisateurCourant.getEmail());
			filiere.setEtablissement(etablissement) ;
			rest.postForEntity("http://academia-programmation/ws/filiere/ajout", filiere, Filiere.class);			
			return "redirect:/parametrage/filieres" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("filiere", filiere) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-filiere" ;
		}
	}
	
	
	@GetMapping ("/parametrage/maj-filiere/{id}")
	public String initMaj (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Filiere filiere = rest.getForObject("http://academia-programmation/ws/filiere/etablissement/{idEtablissement}/id/{id}", 
				Filiere.class, etablissement.getId(), id) ;
		model.addAttribute("filiereExistante", filiere) ;
		return "parametrage/maj-filiere" ;
	}
	
	@PostMapping ("/parametrage/filiere/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			Filiere filiereExistante, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		try {
			Filiere filiere = rest.getForObject("http://academia-programmation/ws/filiere//etablissement/{idEtablissement}/id/{id}", 
					Filiere.class, etablissement.getId(), filiereExistante.getId()) ;
			filiere.setModificateur(utilisateurCourant.getEmail()) ;
			filiere.setLibelle(filiereExistante.getLibelle()) ;
			rest.put("http://academia-programmation/ws/filiere/maj", filiere, Filiere.class);
			return "redirect:/parametrage/filieres" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("filiereExistante", filiereExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/maj-filiere" ;
		}
	}
	
	
	@GetMapping ("/parametrage/details-filiere/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Filiere filiere = rest.getForObject("http://academia-programmation/ws/filiere/etablissement/{idEtablissement}/id/{id}", 
				Filiere.class, etablissement.getId(), id) ;
		model.addAttribute("filiereExistante", filiere) ;
		return "parametrage/details-filiere" ;
	}
	
	
}
