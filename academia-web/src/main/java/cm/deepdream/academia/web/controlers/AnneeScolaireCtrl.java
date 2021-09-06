package cm.deepdream.academia.web.controlers;
import java.io.Serializable ;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.souscription.data.Etablissement;
@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "listeAnneesScolaires"})
public class AnneeScolaireCtrl implements Serializable{
	private Logger logger = Logger.getLogger(AnneeScolaireCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/programmation/anneesscolaires")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<AnneeScolaire[]> response = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}", 
				 AnneeScolaire[].class, etablissement.getId()) ;
		 List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response.getBody());
		 model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
		return "programmation/anneesscolaires" ;
	}
	

	@GetMapping ("/programmation/ajout-anneescolaire")
	public String initAjout (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		AnneeScolaire anneesScolaire = new AnneeScolaire() ;
		Integer annee = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"))) ;
		anneesScolaire.setLibelle(annee+"/"+(annee+1)) ;
		model.addAttribute("anneeScolaire", anneesScolaire) ;
		return "programmation/ajout-anneescolaire" ;
	}
	
	@PostMapping ("/programmation/anneescolaire/ajout")
	public String ajouter (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute ("etablissementCourant") Etablissement etablissement, AnneeScolaire anneesScolaire) throws Exception {
		try {
			anneesScolaire.setCreateur(utilisateurCourant.getEmail());
			anneesScolaire.setModificateur(utilisateurCourant.getEmail());
			anneesScolaire.setEtablissement(etablissement) ;
			rest.postForEntity("http://academia-programmation/ws/anneescolaire/ajout", anneesScolaire, AnneeScolaire.class);			
			return "redirect:/programmation/anneesscolaires" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("anneesScolaire", anneesScolaire) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/ajout-anneescolaire" ;
		}
	}
	
	@GetMapping ("/programmation/modification-anneescolaire/{id}")
	public String initMaj (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, @PathVariable("id") long id) throws Exception {
		AnneeScolaire anneeScolaire = rest.getForObject("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}/id/{id}", AnneeScolaire.class, 
				etablissement.getId(), id) ;
		model.addAttribute("anneeScolaireExistante", anneeScolaire) ;
		return "programmation/modification-anneescolaire" ;
	}
	
	@PostMapping ("/programmation/anneescolaire/modification")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement, AnneeScolaire anneeScolaireExistante) throws Exception {
		try {
			AnneeScolaire anneeScolaire = rest.getForObject("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}/id/{id}", 
					AnneeScolaire.class, etablissement.getId(), anneeScolaireExistante.getId()) ;
			anneeScolaire.setDateDernMaj(LocalDateTime.now()) ;
			anneeScolaire.setModificateur(utilisateurCourant.getEmail()) ;
			anneeScolaire.setLibelle(anneeScolaireExistante.getLibelle()) ;
			anneeScolaire.setDateDebut(anneeScolaireExistante.getDateDebut());
			anneeScolaire.setDateFin(anneeScolaireExistante.getDateFin());
			anneeScolaire.setEtablissement(etablissement) ;
			rest.put("http://academia-programmation/ws/anneescolaire/modification", anneeScolaire, AnneeScolaire.class);
			return "redirect:/programmation/anneesscolaires" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("anneeScolaireExistante", anneeScolaireExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/modification-anneescolaire" ;
		}
	}
	
	@GetMapping ("/programmation/details-anneescolaire/{id}")
	public String initDetails (Model model,  @SessionAttribute ("etablissementCourant") Etablissement etablissement, @PathVariable("id") long id) throws Exception {
		AnneeScolaire anneeScolaire = rest.getForObject("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}/id/{id}", AnneeScolaire.class, 
			etablissement.getId(), id) ;
		model.addAttribute("anneeScolaireExistante", anneeScolaire) ;
		return "programmation/details-anneescolaire" ;
	}
	
	
	@GetMapping ("/programmation/synchronisation-anneesscolaires")
	public String synchroniser (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			 @SessionAttribute ("listeAnneesScolaires") List<AnneeScolaire> listeAnneesScolaires) throws Exception {
		 ResponseEntity<Integer> response = rest.postForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}/synchronize", 
				 null, Integer.class, etablissement.getId()) ;
		 model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
		 return "programmation/anneesscolaires" ;
	}
	
	
	@PostMapping ("/programmation/anneescolaire/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute ("etablissementCourant") Etablissement etablissement, AnneeScolaire anneescolaireExistante) throws Exception {
		try {
			AnneeScolaire anneescolaire = rest.getForObject("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}/id/{id}", 
					AnneeScolaire.class, etablissement.getId(), anneescolaireExistante.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", anneescolaire.getId()) ;
			uriVariables.put("idEtablissement", etablissement.getId()) ;
			rest.delete("http://academia-programmation/ws/anneescolaire/suppr/etablissement/{idEtablissement}/id/{id}", uriVariables);
			return "redirect:/programmation/anneesscolaires" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("anneescolaireExistante", anneescolaireExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/details-anneescolaire" ;
		}
	}
	
	
	@PostMapping ("/programmation/anneescolaire/definition-defaut")
	public String definirDefaut (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute ("etablissementCourant") Etablissement etablissement, AnneeScolaire anneeScolaireExistante) throws Exception {
		try {
			AnneeScolaire anneeScolaire = rest.getForObject("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}/id/{id}", 
					AnneeScolaire.class, etablissement.getId(), anneeScolaireExistante.getId()) ;
			rest.put("http://academia-programmation/ws/anneescolaire/definition-defaut", anneeScolaire, AnneeScolaire.class);
			return "redirect:/programmation/anneesscolaires" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("anneeScolaireExistante", anneeScolaireExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return null ;
		}
	}
	
}
