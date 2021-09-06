package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.DetailEmploiTemps;
import cm.deepdream.academia.programmation.data.UE;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.data.EmploiTemps;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "anneeScolaireCourante", "classeCourante"})
public class EmploiTempsCtrl implements Serializable{
	private Logger logger = Logger.getLogger(EmploiTempsCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/programmation/emploistemps")
	public String index (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire) throws Exception {
		 ResponseEntity<EmploiTemps[]> response = rest.getForEntity("http://academia-programmation/ws/emploitemps/etablissement/{idEtablissement}", 
				 EmploiTemps[].class, etablissement.getId()) ;
		 List<EmploiTemps> listeEmploisTemps = Arrays.asList(response.getBody());
		 model.addAttribute("listeEmploisTemps", listeEmploisTemps) ;
		 model.addAttribute("anneeScolaire", anneeScolaire) ;
		 EmploiTemps emploiTemps = new EmploiTemps () ;
		 emploiTemps.setAnneeScolaire(anneeScolaire);
		 model.addAttribute("emploiTemps", emploiTemps) ;
		 initDependencies0 (model,  etablissement) ;
		 return "programmation/emploistemps" ;
	}
	
	@PostMapping ("/programmation/emploitemps/recherche")
	public String rechercher (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			  EmploiTemps emploiTemps) throws Exception {
		 AnneeScolaire anneeScolaire = rest.getForObject("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}/id/{id}", 
				 AnneeScolaire.class, etablissement.getId(), emploiTemps.getAnneeScolaire().getId()) ;
				  
		 ResponseEntity<EmploiTemps[]> response = rest.getForEntity("http://academia-programmation/ws/emploitemps/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 EmploiTemps[].class, etablissement.getId(), anneeScolaire.getId()) ;
		 List<EmploiTemps> listeEmploisTemps = Arrays.asList(response.getBody());
		 model.addAttribute("listeEmploisTemps", listeEmploisTemps) ;
		 model.addAttribute("anneeScolaire", anneeScolaire) ;
		 emploiTemps = new EmploiTemps () ;
		 emploiTemps.setAnneeScolaire(anneeScolaire);
		 model.addAttribute("emploiTemps", emploiTemps) ;
		 initDependencies0 (model,  etablissement) ;
		 return "programmation/emploistemps" ;
	}
	
	@GetMapping ("/programmation/ajout-emploitemps")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,  
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant ) throws Exception {
		EmploiTemps emploiTemps = new EmploiTemps() ;
		model.addAttribute("emploiTemps", emploiTemps) ;
		initDependencies(model, etablissement, anneeScolaire) ;
		return "programmation/ajout-emploitemps" ;
	}
	
	@PostMapping ("/programmation/emploitemps/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  
			 @SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire, EmploiTemps emploiTemps) throws Exception {
		try {
			emploiTemps.setCreateur(utilisateurCourant.getEmail());
			emploiTemps.setModificateur(utilisateurCourant.getEmail());
			emploiTemps.setEtablissement(etablissement) ;
			emploiTemps.setAnneeScolaire(anneeScolaire);
			rest.postForEntity("http://academia-programmation/ws/emploitemps/ajout", emploiTemps, EmploiTemps.class);			
			return "redirect:/programmation/emploistemps" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model, etablissement, anneeScolaire) ;
			model.addAttribute("emploiTemps", emploiTemps) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/ajout-emploitemps" ;
		}
	}
	
	
	@GetMapping ("/programmation/modification-emploitemps/{id}")
	public String initMaj (Model model, @PathVariable("id") Long id, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire) throws Exception {
		EmploiTemps emploiTemps = rest.getForObject("http://academia-programmation/ws/emploitemps/etablissement/{idEtablissement}/id/{id}", 
				EmploiTemps.class, etablissement.getId(), id) ;
		model.addAttribute("emploiTempsExistant", emploiTemps) ;
		initDependencies(model, etablissement, anneeScolaire) ;
		return "programmation/modification-emploitemps" ;
	}
	
	@PostMapping ("/programmation/emploitemps/modification")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			@SessionAttribute ("etablissementCourant") Etablissement etablissement, EmploiTemps emploiTempsExistant) throws Exception {
		try {
			EmploiTemps emploiTemps = rest.getForObject("http://academia-programmation/ws/emploitemps/etablissement/{idEtablissement}/id/{id}", 
					EmploiTemps.class, etablissement.getId(), emploiTempsExistant.getId()) ;
			emploiTemps.setModificateur(utilisateurCourant.getEmail()) ;
			emploiTemps.setClasse(emploiTempsExistant.getClasse());
			emploiTemps.setEnseignantPrincipal(emploiTempsExistant.getEnseignantPrincipal());
			emploiTemps.setTrimestre(emploiTempsExistant.getTrimestre());
			emploiTemps.setSemestre(emploiTempsExistant.getSemestre());
			rest.put("http://academia-programmation/ws/emploitemps/modification", emploiTemps, EmploiTemps.class);
			return "redirect:/programmation/emploistemps" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model, etablissement, anneeScolaire) ;
			model.addAttribute("emploiTempsExistant", emploiTempsExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/modification-emploitemps" ;
		}
	}
	
	
	@GetMapping ("/programmation/edition-details/{id}")
	public String editionDetails (Model model,@SessionAttribute("etablissementCourant") Etablissement etablissement, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire, @PathVariable("id") Long id) throws Exception {
		EmploiTemps emploiTemps = rest.getForObject("http://academia-programmation/ws/emploitemps/etablissement/{idEtablissement}/id/{id}", 
				EmploiTemps.class, etablissement.getId(), id) ;
		model.addAttribute("emploiTempsExistant", emploiTemps) ;
		model.addAttribute("classeCourante", emploiTemps.getClasse()) ;
		
		DetailEmploiTemps detail = new DetailEmploiTemps() ;
		detail.setEmploiTemps(emploiTemps);
		detail.setUe(new UE());
		detail.setEnseignant(new Enseignant());
		model.addAttribute("detailET", detail) ;
		ResponseEntity<DetailEmploiTemps[]> response = rest.getForEntity("http://academia-programmation/ws/emploitemps/details/etablissement/{idEtablissement}/id/{id}", 
				DetailEmploiTemps[].class, etablissement.getId(), emploiTemps.getId()) ;
		List<DetailEmploiTemps> listeDetailsET = Arrays.asList(response.getBody());
		model.addAttribute("listeDetailsET", listeDetailsET) ;
		initDependencies (model, etablissement, anneeScolaire) ;
		return "programmation/edition-details" ;
	}
	
	@PostMapping ("/programmation/emploitemps/maj-detail")
	public String majPermission (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			@SessionAttribute("etablissementCourant") Etablissement etablissement, DetailEmploiTemps detailET) throws Exception {
		try {
			logger.info("Edition d'une permission") ;
			detailET.setDateDernMaj(LocalDateTime.now()) ;
			detailET.setModificateur(utilisateurCourant.getEmail()) ;
			detailET.setEtablissement(etablissement);
			rest.postForObject("http://academia-programmation/ws/detailet/ajout", detailET, DetailEmploiTemps.class) ;
			
			EmploiTemps emploiTemps = rest.getForObject("http://academia-programmation/ws/emploitemps/etablissement/{idEtablissement}/id/{id}", 
					EmploiTemps.class, etablissement.getId(), detailET.getEmploiTemps().getId()) ;
			detailET = new DetailEmploiTemps() ;
			detailET.setEmploiTemps(emploiTemps);
			
			model.addAttribute("detailET", detailET) ;
			model.addAttribute("emploiTempsExistant", emploiTemps) ;
			ResponseEntity<DetailEmploiTemps[]> response = rest.getForEntity("http://academia-programmation/ws/emploitemps/details/etablissement/{idEtablissement}/id/{id}", 
					DetailEmploiTemps[].class, etablissement.getId(), emploiTemps.getId()) ;
			List<DetailEmploiTemps> listeDetailsET = Arrays.asList(response.getBody());
			model.addAttribute("listeDetailsET", listeDetailsET) ;
			initDependencies (model, etablissement, anneeScolaire) ;
			return "programmation/edition-details" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model, etablissement, anneeScolaire) ;
			model.addAttribute("detailET", detailET) ;
			model.addAttribute("emploiTempsExistants", detailET.getEmploiTemps()) ;
			ResponseEntity<DetailEmploiTemps[]> response = rest.getForEntity("http://academia-programmation/ws/emploitemps/details/etablissement/{idEtablissement}/id/{id}", 
					DetailEmploiTemps[].class, detailET.getEmploiTemps().getId()) ;
			List<DetailEmploiTemps> listeDetailsET = Arrays.asList(response.getBody());
			model.addAttribute("listeDetailsET", listeDetailsET) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/edition-details" ;
		}
	}
	
	
	@GetMapping ("/programmation/suppr-detail/{id}")
	public String supprPermission (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
		@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaire,
		@SessionAttribute("etablissementCourant") Etablissement etablissement, @PathVariable("id") Long id) throws Exception {
		DetailEmploiTemps detailET = null ;
		try {
			detailET = rest.getForObject("http://academia-programmation/ws/detailet/etablissement/{idEtablissement}/id/{id}", 
					DetailEmploiTemps.class, etablissement.getId(), id) ;
			Map<String, Object> uriVariables = new HashMap<String, Object>() ;
			uriVariables.put("id", detailET.getId()) ;
			uriVariables.put("idEtablissement", etablissement.getId()) ;
			rest.delete("http://academia-programmation/ws/detailet/suppr/{idEtablissement}/id/{id}", uriVariables) ;
			
			EmploiTemps emploiTemps = rest.getForObject("http://academia-programmation/ws/emploitemps/etablissement/{idEtablissement}/id/{id}", 
					EmploiTemps.class, etablissement.getId(), detailET.getEmploiTemps().getId()) ;
			detailET = new DetailEmploiTemps() ;
			detailET.setEmploiTemps(emploiTemps) ;
			model.addAttribute("detailET", detailET) ;
			ResponseEntity<DetailEmploiTemps[]> response = rest.getForEntity("http://academia-programmation/ws/emploitemps/details/etablissement/{idEtablissement}/id/{id}", 
					DetailEmploiTemps[].class, etablissement.getId(), emploiTemps.getId()) ;
			List<DetailEmploiTemps> listeDetailsET = Arrays.asList(response.getBody());
			model.addAttribute("listeDetailsET", listeDetailsET) ;
			initDependencies (model, etablissement, anneeScolaire) ;
			return "programmation/edition-details" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies (model, etablissement, anneeScolaire) ;
			model.addAttribute("detailET", detailET) ;
			model.addAttribute("profilExistant", detailET.getEmploiTemps()) ;
			ResponseEntity<DetailEmploiTemps[]> response = rest.getForEntity("http://academia-programmation/ws/emploitemps/details/etablissement/{idEtablissement}/id/{id}", 
					DetailEmploiTemps[].class, etablissement.getId(), detailET.getEmploiTemps().getId()) ;
			List<DetailEmploiTemps> listeDetailsET = Arrays.asList(response.getBody());
			model.addAttribute("listeDetailsET", listeDetailsET) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/edition-details" ;
		}
	}
	
	
	@GetMapping ("/programmation/details-emploitemps/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		EmploiTemps emploiTemps = rest.getForObject("http://academia-programmation/ws/emploitemps/etablissement/{idEtablissement}/id/{id}", 
				EmploiTemps.class, etablissement.getId(), id) ;
		model.addAttribute("emploiTempsExistant", emploiTemps) ;
		
		ResponseEntity<DetailEmploiTemps[]> response = rest.getForEntity("http://academia-programmation/ws/emploitemps/details/etablissement/{idEtablissement}/id/{id}", 
				DetailEmploiTemps[].class, etablissement.getId(), emploiTemps.getId()) ;
		List<DetailEmploiTemps> listeDetailsET = Arrays.asList(response.getBody());
		model.addAttribute("listeDetailsET", listeDetailsET) ;
		return "programmation/details-emploitemps" ;
	}
	
	
	private void initDependencies0 (Model model, Etablissement etablissement) {
		ResponseEntity<AnneeScolaire[]> response = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}", 
				AnneeScolaire[].class, etablissement.getId()) ;
		List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response.getBody());
		model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
	}
	
	
	private void initDependencies (Model model, Etablissement etablissement, AnneeScolaire anneeScolaire) {
		 ResponseEntity<Enseignant[]> response = rest.getForEntity("http://academia-programmation/ws/enseignant/etablissement/{idEtablissement}", 
				 Enseignant[].class, etablissement.getId()) ;
		 List<Enseignant> listeEnseignants = Arrays.asList(response.getBody());
		 model.addAttribute("listeEnseignants", listeEnseignants) ;
		 
		 ResponseEntity<Classe[]> response2 = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}", 
				 Classe[].class, etablissement.getId()) ;
		 List<Classe> listeClasses = Arrays.asList(response2.getBody());
		 model.addAttribute("listeClasses", listeClasses) ;
		 
		 ResponseEntity<UE[]> response3 = rest.getForEntity("http://academia-programmation/ws/ue/etablissement/{idEtablissement}", 
				 UE[].class, etablissement.getId()) ;
		 List<UE> listeUEs = Arrays.asList(response3.getBody());
		 model.addAttribute("listeUEs", listeUEs) ;
		 
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
