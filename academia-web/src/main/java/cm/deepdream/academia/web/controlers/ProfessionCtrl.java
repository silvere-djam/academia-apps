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
import cm.deepdream.academia.programmation.data.Profession;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant"})
public class ProfessionCtrl implements Serializable{
	private Logger logger = Logger.getLogger(ProfessionCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/professions")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Profession[]> response = rest.getForEntity("http://academia-programmation/ws/profession/etablissement/{idEtablissement}", 
				 Profession[].class, etablissement.getId()) ;
		 List<Profession> listeProfessionx = Arrays.asList(response.getBody());
		 model.addAttribute("listeProfessions", listeProfessionx) ;
		return "parametrage/professions" ;
	}
	
	@GetMapping ("/parametrage/ajout-profession")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Profession profession = new Profession() ;
		model.addAttribute("profession", profession) ;
		return "parametrage/ajout-profession" ;
	}
	
	@PostMapping ("/parametrage/profession/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  Profession profession) throws Exception {
		try {
			profession.setCreateur(utilisateurCourant.getEmail());
			profession.setModificateur(utilisateurCourant.getEmail());
			profession.setEtablissement(etablissement) ;
			rest.postForEntity("http://academia-programmation/ws/profession/ajout", profession, Profession.class);			
			return "redirect:/parametrage/professions" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("profession", profession) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-profession" ;
		}
	}
	
	
	@GetMapping ("/parametrage/maj-profession/{id}")
	public String initMaj (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Profession profession = rest.getForObject("http://academia-programmation/ws/profession/etablissement/{idEtablissement}/id/{id}", 
				Profession.class, etablissement.getId(), id) ;
		model.addAttribute("professionExistante", profession) ;
		return "parametrage/maj-profession" ;
	}
	
	@PostMapping ("/parametrage/profession/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			Profession professionExistante, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		try {
			Profession profession = rest.getForObject("http://academia-programmation/ws/profession//etablissement/{idEtablissement}/id/{id}", 
					Profession.class, etablissement.getId(), professionExistante.getId()) ;
			profession.setModificateur(utilisateurCourant.getEmail()) ;
			profession.setLibelle(professionExistante.getLibelle()) ;
			rest.put("http://academia-programmation/ws/profession/maj", profession, Profession.class);
			return "redirect:/parametrage/professions" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("professionExistante", professionExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/maj-profession" ;
		}
	}
	
	
	@GetMapping ("/parametrage/details-profession/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Profession profession = rest.getForObject("http://academia-programmation/ws/profession/etablissement/{idEtablissement}/id/{id}", 
				Profession.class, etablissement.getId(), id) ;
		model.addAttribute("professionExistante", profession) ;
		return "parametrage/details-profession" ;
	}
	
	
}
