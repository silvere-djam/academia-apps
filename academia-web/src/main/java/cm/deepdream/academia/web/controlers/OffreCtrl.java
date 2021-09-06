package cm.deepdream.academia.web.controlers;
import java.io.Serializable ;
import org.springframework.core.env.Environment;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.security.data.Action;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Offre;
@Controller
@SessionAttributes({"utilisateurCourant"})
public class OffreCtrl implements Serializable{
	private Logger logger = Logger.getLogger(OffreCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/souscription/offres")
	public String index (Model model) throws Exception {
		 ResponseEntity<Offre[]> response = rest.getForEntity("http://academia-souscription/ws/offre/all", Offre[].class) ;
		 List<Offre> listeOffres = Arrays.asList(response.getBody());
		 model.addAttribute("listeOffres", listeOffres) ;
		 return "souscription/offres" ;
	}
	

	@GetMapping ("/souscription/ajout-offre")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Offre offre = new Offre() ;
		model.addAttribute("offre", offre) ;
		return "souscription/ajout-offre" ;
	}
	
	@PostMapping ("/souscription/offre/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			Offre offre) throws Exception {
		try {
			rest.postForEntity("http://academia-souscription/ws/offre/ajout", offre, Offre.class);			
			return "redirect:/souscription/offres" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("offre", offre) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "souscription/ajout-offre" ;
		}
	}
	
	@GetMapping ("/souscription/modification-offre/{id}")
	public String initMaj (Model model, @PathVariable("id") long id) throws Exception {
		Offre offre = rest.getForObject("http://academia-souscription/ws/offre/id/{id}", Offre.class, id) ;
		model.addAttribute("offreExistante", offre) ;
		return "souscription/modification-offre" ;
	}
	
	@PostMapping ("/souscription/offre/modification")
	public String modifier (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Offre offreExistante) throws Exception {
		try {
			Offre offre = rest.getForObject("http://academia-souscription/ws/offre/id/{id}", 
					Offre.class, offreExistante.getId()) ;
			offre.setLibelle(offreExistante.getLibelle());
			offre.setMinEleves(offreExistante.getMinEleves());
			offre.setMaxEleves(offreExistante.getMaxEleves());
			offre.setMaxUtilisateurs(offreExistante.getMaxUtilisateurs());
			offre.setDureeEssai(offreExistante.getDureeEssai());
			offre.setDescription(offreExistante.getDescription());
			offre.setMontantBase(offreExistante.getMontantBase());
			offre.setMontantMillier(offreExistante.getMontantMillier());
			rest.put("http://academia-souscription/ws/offre/modification", offre, Offre.class);
			return "redirect:/souscription/offres" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("offreExistante", offreExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "souscription/modification-offre" ;
		}
	}
	
	@GetMapping ("/souscription/details-offre/{id}")
	public String initDetails (Model model,  @PathVariable("id") long id) throws Exception {
		Offre offre = rest.getForObject("http://academia-souscription/ws/offre/id/{id}", 
				Offre.class, id) ;
		model.addAttribute("offreExistante", offre) ;
		return "souscription/details-offre" ;
	}
	
	
	@PostMapping ("/souscription/offre/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			Offre offreExistante) throws Exception {
		try {
			Offre offre = rest.getForObject("http://academia-souscription/ws/offre/id/{id}", 
					Offre.class, offreExistante.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", offre.getId()) ;

			rest.delete("http://academia-souscription/ws/offre/suppr/id/{id}", uriVariables);
			return "redirect:/souscription/offres" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("offreExistante", offreExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "souscription/details-offre" ;
		}
	}
	
}
