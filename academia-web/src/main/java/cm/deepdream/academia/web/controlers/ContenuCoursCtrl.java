package cm.deepdream.academia.web.controlers;
import java.io.Serializable ;
import org.springframework.core.env.Environment;
import java.time.LocalDateTime;
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
import cm.deepdream.academia.viescolaire.data.Absence;
import cm.deepdream.academia.viescolaire.data.Cours;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.souscription.data.Etablissement;
@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "listeEleves"})
public class ContenuCoursCtrl implements Serializable{
	private Logger logger = Logger.getLogger(ContenuCoursCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/viescolaire/contenus")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Absence[]> response = rest.getForEntity("http://academia-viescolaire/ws/contenu/etablissement/{idEtablissement}", 
				 Absence[].class, etablissement.getId()) ;
		 List<Absence> listeAbsences = Arrays.asList(response.getBody());
		 model.addAttribute("listeAbsences", listeAbsences) ;
		return "viescolaire/contenus" ;
	}
	

	@GetMapping ("/viescolaire/edition-contenus-cours/{idCours}")
	public String editerAbsCours (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute("classeCourante") Classe classe,
			@PathVariable("idCours") Long idCours) throws Exception {
		Cours cours = rest.getForObject("http://academia-viescolaire/ws/cours/etablissement/{idEtablissement}/id/{id}", 
				Cours.class, etablissement.getId(), idCours) ;
		Absence contenu = new Absence() ;
		contenu.setCours(cours);
		model.addAttribute("contenu", contenu) ;
		
		ResponseEntity<Eleve[]> response0 = rest.getForEntity("http://academia-viescolaire/ws/eleve/etablissement/{idEtablissement}/classe/{idClasse}", 
				 Eleve[].class, etablissement.getId(), classe.getId()) ;
		List<Eleve> listeEleves = Arrays.asList(response0.getBody());
		model.addAttribute("listeEleves", listeEleves) ;
		
		ResponseEntity<Absence[]> response1 = rest.getForEntity("http://academia-viescolaire/ws/contenu/etablissement/{idEtablissement}/cours/{idCours}", 
				Absence[].class, etablissement.getId(), idCours) ;
		 List<Absence> listeAbsences = Arrays.asList(response1.getBody());
		 model.addAttribute("listeAbsences", listeAbsences) ;
		 
		return "viescolaire/edition-contenu-cours" ;
	}
	
	@PostMapping ("/viescolaire/contenu-cours/edition")
	public String ajouterAbsCours (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute ("etablissementCourant") Etablissement etablissement, Absence contenu) throws Exception {
		try {
			contenu.setCreateur(utilisateurCourant.getEmail());
			contenu.setModificateur(utilisateurCourant.getEmail());
			contenu.setEtablissement(etablissement) ;
			contenu.setCours(contenu.getCours());
			rest.postForEntity("http://academia-viescolaire/ws/contenu/edition", contenu, Absence.class);			
			return "redirect:/viescolaire/edition-contenu-cours/"+contenu.getCours().getId() ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("contenu", contenu) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "redirect:/viescolaire/edition-contenu-cours/"+contenu.getCours().getId() ;
		}
	}
	
	@GetMapping ("/viescolaire/maj-contenu/{id}")
	public String initMaj (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, @PathVariable("id") long id) throws Exception {
		Absence contenu = rest.getForObject("http://academia-security/ws/contenu/etablissement/{idEtablissement}/id/{id}", Absence.class, 
				etablissement.getId(), id) ;
		model.addAttribute("contenuExistante", contenu) ;
		return "viescolaire/maj-contenu" ;
	}
	
	@PostMapping ("/viescolaire/contenu/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement, Absence contenuExistante) throws Exception {
		try {
			Absence contenu = rest.getForObject("http://academia-security/ws/contenu/etablissement/{idEtablissement}/id/{id}", 
					Absence.class, etablissement.getId(), contenuExistante.getId()) ;
			contenu.setDateDernMaj(LocalDateTime.now()) ;
			contenu.setModificateur(utilisateurCourant.getEmail()) ;
			contenu.setEtablissement(etablissement) ;
			rest.put("http://academia-security/ws/contenu/maj", contenu, Absence.class);
			return "redirect:/viescolaire/contenus" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("contenuExistant", contenuExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "viescolaire/modification-contenu" ;
		}
	}
	
	@GetMapping ("/viescolaire/details-contenu/{id}")
	public String initDetails (Model model,  @SessionAttribute ("etablissementCourant") Etablissement etablissement, @PathVariable("id") long id) throws Exception {
		Absence contenu = rest.getForObject("http://academia-security/ws/contenu/etablissement/{idEtablissement}/id/{id}", Absence.class, 
			etablissement.getId(), id) ;
		model.addAttribute("contenuExistante", contenu) ;
		return "viescolaire/details-contenu" ;
	}
	
	
	@GetMapping ("/viescolaire/suppr-contenu/{id}")
	public String supprimerAbs (Model model,  @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@PathVariable("id") Long id) throws Exception {
		Absence contenu = rest.getForObject("http://academia-viescolaire/ws/contenu/etablissement/{idEtablissement}/id/{id}", Absence.class, 
			etablissement.getId(), id) ;
		rest.postForEntity("http://academia-viescolaire/ws/contenu/suppr", contenu, Absence.class);
		return "redirect:/viescolaire/edition-contenu-cours/"+contenu.getCours().getId() ;
	}
	
	
	@PostMapping ("/viescolaire/contenu/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute ("etablissementCourant") Etablissement etablissement, Absence contenuExistante) throws Exception {
		try {
			Absence contenu = rest.getForObject("http://academia-security/ws/contenu/etablissement/{idEtablissement}/id/{id}", 
					Absence.class, etablissement.getId(), contenuExistante.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", contenu.getId()) ;
			uriVariables.put("idEtablissement", etablissement.getId()) ;
			rest.delete("http://academia-security/ws/contenu/suppr/etablissement/{idEtablissement}/id/{id}", uriVariables);
			return "redirect:/viescolaire/contenus" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("contenuExistante", contenuExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "viescolaire/details-contenu" ;
		}
	}
	
}
