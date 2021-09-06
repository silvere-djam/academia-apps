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
public class AbsenceCtrl implements Serializable{
	private Logger logger = Logger.getLogger(AbsenceCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/viescolaire/absences")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Absence[]> response = rest.getForEntity("http://academia-viescolaire/ws/absence/etablissement/{idEtablissement}", 
				 Absence[].class, etablissement.getId()) ;
		 List<Absence> listeAbsences = Arrays.asList(response.getBody());
		 model.addAttribute("listeAbsences", listeAbsences) ;
		return "viescolaire/absences" ;
	}
	

	@GetMapping ("/viescolaire/edition-absences-cours/{idCours}")
	public String editerAbsCours (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute("classeCourante") Classe classe,
			@PathVariable("idCours") Long idCours) throws Exception {
		Cours cours = rest.getForObject("http://academia-viescolaire/ws/cours/etablissement/{idEtablissement}/id/{id}", 
				Cours.class, etablissement.getId(), idCours) ;
		Absence absence = new Absence() ;
		absence.setCours(cours);
		model.addAttribute("absence", absence) ;
		
		ResponseEntity<Eleve[]> response0 = rest.getForEntity("http://academia-viescolaire/ws/eleve/etablissement/{idEtablissement}/classe/{idClasse}", 
				 Eleve[].class, etablissement.getId(), classe.getId()) ;
		List<Eleve> listeEleves = Arrays.asList(response0.getBody());
		model.addAttribute("listeEleves", listeEleves) ;
		
		ResponseEntity<Absence[]> response1 = rest.getForEntity("http://academia-viescolaire/ws/absence/etablissement/{idEtablissement}/cours/{idCours}", 
				Absence[].class, etablissement.getId(), idCours) ;
		 List<Absence> listeAbsences = Arrays.asList(response1.getBody());
		 model.addAttribute("listeAbsences", listeAbsences) ;
		 
		return "viescolaire/edition-absence-cours" ;
	}
	
	@PostMapping ("/viescolaire/absence-cours/edition")
	public String ajouterAbsCours (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute ("etablissementCourant") Etablissement etablissement, Absence absence) throws Exception {
		try {
			absence.setCreateur(utilisateurCourant.getEmail());
			absence.setModificateur(utilisateurCourant.getEmail());
			absence.setEtablissement(etablissement) ;
			absence.setCours(absence.getCours());
			rest.postForEntity("http://academia-viescolaire/ws/absence/edition", absence, Absence.class);			
			return "redirect:/viescolaire/edition-absence-cours/"+absence.getCours().getId() ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("absence", absence) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "redirect:/viescolaire/edition-absence-cours/"+absence.getCours().getId() ;
		}
	}
	
	@GetMapping ("/viescolaire/maj-absence/{id}")
	public String initMaj (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, @PathVariable("id") long id) throws Exception {
		Absence absence = rest.getForObject("http://academia-security/ws/absence/etablissement/{idEtablissement}/id/{id}", Absence.class, 
				etablissement.getId(), id) ;
		model.addAttribute("absenceExistante", absence) ;
		return "viescolaire/maj-absence" ;
	}
	
	@PostMapping ("/viescolaire/absence/maj")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement, Absence absenceExistante) throws Exception {
		try {
			Absence absence = rest.getForObject("http://academia-security/ws/absence/etablissement/{idEtablissement}/id/{id}", 
					Absence.class, etablissement.getId(), absenceExistante.getId()) ;
			absence.setDateDernMaj(LocalDateTime.now()) ;
			absence.setModificateur(utilisateurCourant.getEmail()) ;
			absence.setEtablissement(etablissement) ;
			rest.put("http://academia-security/ws/absence/maj", absence, Absence.class);
			return "redirect:/viescolaire/absences" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("absenceExistant", absenceExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "viescolaire/modification-absence" ;
		}
	}
	
	@GetMapping ("/viescolaire/details-absence/{id}")
	public String initDetails (Model model,  @SessionAttribute ("etablissementCourant") Etablissement etablissement, @PathVariable("id") long id) throws Exception {
		Absence absence = rest.getForObject("http://academia-security/ws/absence/etablissement/{idEtablissement}/id/{id}", Absence.class, 
			etablissement.getId(), id) ;
		model.addAttribute("absenceExistante", absence) ;
		return "viescolaire/details-absence" ;
	}
	
	
	@GetMapping ("/viescolaire/suppr-absence/{id}")
	public String supprimerAbs (Model model,  @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@PathVariable("id") Long id) throws Exception {
		Absence absence = rest.getForObject("http://academia-viescolaire/ws/absence/etablissement/{idEtablissement}/id/{id}", Absence.class, 
			etablissement.getId(), id) ;
		rest.postForEntity("http://academia-viescolaire/ws/absence/suppr", absence, Absence.class);
		return "redirect:/viescolaire/edition-absence-cours/"+absence.getCours().getId() ;
	}
	
	
	@PostMapping ("/viescolaire/absence/suppr")
	public String suppression (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute ("etablissementCourant") Etablissement etablissement, Absence absenceExistante) throws Exception {
		try {
			Absence absence = rest.getForObject("http://academia-security/ws/absence/etablissement/{idEtablissement}/id/{id}", 
					Absence.class, etablissement.getId(), absenceExistante.getId()) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", absence.getId()) ;
			uriVariables.put("idEtablissement", etablissement.getId()) ;
			rest.delete("http://academia-security/ws/absence/suppr/etablissement/{idEtablissement}/id/{id}", uriVariables);
			return "redirect:/viescolaire/absences" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("absenceExistante", absenceExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "viescolaire/details-absence" ;
		}
	}
	
}
