package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
import cm.deepdream.academia.programmation.data.Programme;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Chapitre;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.souscription.data.Etablissement;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "listeSemestres", "listeTrimestres", "listeChapitres",
	"programmeCourant"})
public class ProgrammeCtrl implements Serializable{
	private Logger logger = Logger.getLogger(ProgrammeCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/programmation/programmes")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			 @SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire) throws Exception {
		 ResponseEntity<Programme[]> response = rest.getForEntity("http://academia-programmation/ws/programme/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Programme[].class, etablissement.getId(), anneeScolaire.getId()) ;
		 List<Programme> listeProgrammes = Arrays.asList(response.getBody());
		 model.addAttribute("listeProgrammes", listeProgrammes) ;
		 Programme programme = new Programme () ;
		 programme.setAnneeScolaire(anneeScolaire);
		 model.addAttribute("programme", programme) ;
		 model.addAttribute("anneeScolaire", anneeScolaire) ;
		 initDependencies (model, etablissement, anneeScolaire) ;
		 return "programmation/programmes" ;
	}
	
	@PostMapping ("/programmation/programme/recherche")
	public String rechercher (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			  Programme programme) throws Exception {
		AnneeScolaire anneeScolaire = rest.getForObject("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}/id/{id}", 
				 AnneeScolaire.class, etablissement.getId(), programme.getAnneeScolaire().getId()) ;
		
		 ResponseEntity<Programme[]> response = rest.getForEntity("http://academia-programmation/ws/programme/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Programme[].class, etablissement.getId(), anneeScolaire.getId()) ;
		 List<Programme> listeProgrammes = Arrays.asList(response.getBody());
		 model.addAttribute("listeProgrammes", listeProgrammes) ;
		 model.addAttribute("anneeScolaire", anneeScolaire) ;
		 programme = new Programme () ;
		 programme.setAnneeScolaire(anneeScolaire);
		 model.addAttribute("programme", programme) ;
		 initDependencies (model, etablissement, anneeScolaire) ;
		 return "programmation/programmes" ;
	}
	
	@GetMapping ("/programmation/ajout-programme")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) throws Exception {
		Programme programme = new Programme() ;
		programme.setEtablissement(etablissement);
		model.addAttribute("programme", programme) ;
		return "programmation/ajout-programme" ;
	}
	
	@PostMapping ("/programmation/programme/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissementCourant,  Programme programme) throws Exception {
		try {
			programme.setCreateur(utilisateurCourant.getEmail());
			programme.setModificateur(utilisateurCourant.getEmail());
			programme.setEtablissement(etablissementCourant) ;
			rest.postForEntity("http://academia-programmation/ws/programme/ajout", programme, Programme.class);			
			return "redirect:/programmation/programmes" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("programme", programme) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/ajout-programme" ;
		}
	}
	
	
	@GetMapping ("/programmation/modification-programme/{id}")
	public String initMaj (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Programme programme = rest.getForObject("http://academia-programmation/ws/programme/etablissement/{idEtablissement}/id/{id}", 
				Programme.class, etablissement.getId(),  id) ;
		model.addAttribute("programmeExistant", programme) ;
		return "programmation/modification-programme" ;
	}
	
	@PostMapping ("/programmation/programme/modification")
	public String modifierProgramme (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, @SessionAttribute("etablissementCourant") Etablissement etablissement, 
			Programme programmeExistant) throws Exception {
		try {
			Programme programme = rest.getForObject("http://academia-programmation/ws/programme/etablissement/{idEtablissement}/id/{id}", 
					Programme.class, etablissement.getId(), programmeExistant.getId()) ;
			programme.setModificateur(utilisateurCourant.getEmail()) ;
			programme.setAnneeScolaire(programmeExistant.getAnneeScolaire()) ;
			programme.setUe(programmeExistant.getUe()) ;
			programme.setDureeEffectuee(programmeExistant.getDureeEffectuee()) ;
			programme.setDureeTheorique(programmeExistant.getDureeTheorique());
			rest.put("http://academia-programmation/ws/programme/modification", programme, Programme.class);
			return "redirect:/programmation/programmes" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("programmeExistant", programmeExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/maj-programme" ;
		}
	}
	
	@GetMapping ("/programmation/edition-chapitre/{idProgramme}")
	public String initierEditionChapitre (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire, 
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@PathVariable("idProgramme") Long idProgramme) throws Exception {
		
		Programme programme = rest.getForObject("http://academia-programmation/ws/programme/etablissement/{idEtablissement}/id/{id}", 
				Programme.class, etablissement.getId(),  idProgramme) ;
		model.addAttribute("programmeCourant", programme) ;
		
		Chapitre chapitre = new Chapitre() ;
		chapitre.setProgramme(programme);
		chapitre.setDureeTheorique(LocalTime.of(0, 0)) ;
		model.addAttribute("chapitre", chapitre) ;
		
		ResponseEntity<Chapitre[]> response = rest.getForEntity("http://academia-programmation/ws/chapitre/etablissement/{idEtablissement}/programme/{idProgramme}", 
				Chapitre[].class, etablissement.getId(), idProgramme) ;
		model.addAttribute("listeChapitres", Arrays.asList(response.getBody())) ;
		initDependencies(model, etablissement, anneeScolaire);
		return "programmation/edition-chapitre" ;
	}
	
	
	@GetMapping ("/programmation/modification-chapitre/{idChapitre}")
	public String initierModificationChapitre (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire, 
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute("programmeCourant") Programme programme,
			@PathVariable("idChapitre") Long idChapitre) throws Exception {

		Chapitre chapitre = rest.getForObject("http://academia-programmation/ws/chapitre/etablissement/{idEtablissement}/id/{id}", 
				Chapitre.class, etablissement.getId(), idChapitre) ;
		model.addAttribute("chapitre", chapitre) ;
		model.addAttribute("programmeCourant", programme) ;
		ResponseEntity<Chapitre[]> response = rest.getForEntity("http://academia-programmation/ws/chapitre/etablissement/{idEtablissement}/programme/{idProgramme}", 
				Chapitre[].class, etablissement.getId(), programme.getId()) ;
		model.addAttribute("listeChapitres", Arrays.asList(response.getBody())) ;
		initDependencies(model, etablissement, anneeScolaire);
		return "programmation/edition-chapitre" ;
	}
	
	
	
	@PostMapping ("/programmation/programme/chapitre/edition")
	public String modifierChapitre (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateur, 
			@SessionAttribute("etablissementCourant") Etablissement etablissement,
			@SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			@SessionAttribute("programmeCourant") Programme programme,
			Chapitre chapitre) throws Exception {
		try {
			chapitre.setEtablissement(etablissement);
			chapitre.setModificateur(utilisateur.getEmail());
			chapitre.setEtablissement(etablissement);
			chapitre.setProgramme(programme);
			if(chapitre.getId() == null)
				rest.postForEntity("http://academia-programmation/ws/chapitre/ajout", chapitre, Chapitre.class);
			else 
				rest.put("http://academia-programmation/ws/chapitre/modification", chapitre, Chapitre.class);
			chapitre.setId(null);
			model.addAttribute("chapitre", chapitre) ;
			ResponseEntity<Chapitre[]> response = rest.getForEntity("http://academia-programmation/ws/chapitre/etablissement/{idEtablissement}/programme/{idProgramme}", 
					Chapitre[].class, etablissement.getId(), programme.getId()) ;
			model.addAttribute("listeChapitres", Arrays.asList(response.getBody())) ;
			return "programmation/edition-chapitre" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("chapitre", chapitre) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/edition-chapitre" ;
		}
	}
	
	
	@GetMapping ("/programmation/details-programme/{id}")
	public String initDetails (Model model, @PathVariable("id") Long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Programme programme = rest.getForObject("http://academia-programmation/ws/programme/etablissement/{idEtablissement}/id/{id}", 
				Programme.class, etablissement.getId(),  id) ;
		model.addAttribute("programmeExistant", programme) ;
		
		ResponseEntity<Chapitre[]> response = rest.getForEntity("http://academia-programmation/ws/chapitre/etablissement/{idEtablissement}/programme/{idProgramme}", 
				Chapitre[].class, etablissement.getId(), id) ;
		model.addAttribute("listeChapitres", Arrays.asList(response.getBody())) ;
		return "programmation/details-programme" ;
	}
	
	
	@GetMapping ("/programmation/suppr-chapitre/{idChapitre}")
	public String supprimerChapitre (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire, 
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute("programmeCourant") Programme programme,
			@PathVariable("idChapitre") Long idChapitre) throws Exception {
		
		Map<String, Object> uriVariables = new HashMap<String, Object>() ;
		uriVariables.put("id", idChapitre) ;
		uriVariables.put("idEtablissement", etablissement.getId()) ;
		rest.delete("http://academia-programmation/ws/chapitre/suppr/etablissement/{idEtablissement}/id/{id}", uriVariables);
		
		ResponseEntity<Chapitre[]> response = rest.getForEntity("http://academia-programmation/ws/chapitre/etablissement/{idEtablissement}/programme/{idProgramme}", 
				Chapitre[].class, etablissement.getId(), programme.getId()) ;
		List<Chapitre> listeChapitres = Arrays.asList(response.getBody()) ;
		model.addAttribute("listeChapitres", listeChapitres) ;
		
		Chapitre chapitre = new Chapitre() ;
		chapitre.setProgramme(programme);
		model.addAttribute("chapitre", chapitre) ;
		initDependencies(model, etablissement, anneeScolaire);
		return "programmation/edition-chapitre" ;
	}
	
	private void initDependencies (Model model, Etablissement etablissement, AnneeScolaire anneeScolaire) {
		ResponseEntity<AnneeScolaire[]> response = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}", 
				AnneeScolaire[].class, etablissement.getId()) ;
		List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response.getBody());
		model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
		
		 ResponseEntity<Trimestre[]> response4 = rest.getForEntity("http://academia-programmation/ws/trimestre/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Trimestre[].class, etablissement.getId(), anneeScolaire.getId()) ;
		 List<Trimestre> listeTrimestres = Arrays.asList(response4.getBody());
		 model.addAttribute("listeTrimestres", listeTrimestres) ;
		 
		 ResponseEntity<Semestre[]> response5 = rest.getForEntity("http://academia-programmation/ws/semestre/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Semestre[].class, etablissement.getId(), anneeScolaire.getId()) ;
		 List<Semestre> listeSemestres = Arrays.asList(response5.getBody());
		 model.addAttribute("listeSemestres", listeSemestres) ;
	}
}
