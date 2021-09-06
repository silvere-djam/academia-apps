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
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.data.TypeExamen;
import cm.deepdream.academia.programmation.data.Semestre;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "anneeScolaireCourante", "anneeScolaire",
	"listeAnneesScolaires", "listeTypesExamen", "listeTrimestres", "listeSemestres"})
public class ExamenCtrl implements Serializable{
	private Logger logger = Logger.getLogger(ExamenCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/programmation/examens")
	public String indexExamen (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaireCourante) throws Exception {
		 
		 ResponseEntity<AnneeScolaire[]> response3 = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}", 
				 AnneeScolaire[].class, etablissement.getId(), anneeScolaireCourante.getId()) ;
		 List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response3.getBody());
		 model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
		 
		 model.addAttribute("anneeScolaire",  anneeScolaireCourante) ;
		 
		 ResponseEntity<Examen[]> response = rest.getForEntity("http://academia-programmation/ws/examen/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Examen[].class, etablissement.getId(), anneeScolaireCourante.getId() == null ? 0L:anneeScolaireCourante.getId()) ;
		 List<Examen> listeExamens = Arrays.asList(response.getBody());
		 model.addAttribute("listeExamens", listeExamens) ;
		 	
		 initDependencies(model, etablissement, anneeScolaireCourante);
		 Examen examen = new Examen () ;
		 examen.setAnneeScolaire(anneeScolaireCourante);
		 examen.setTypeExamen(new TypeExamen());
		 model.addAttribute("examen", examen) ;
		 return "programmation/examens" ;
	}
	
	
	
	@PostMapping ("/programmation/examen/recherche")
	public String rechercher (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@SessionAttribute("listeAnneesScolaires") List<AnneeScolaire> listeAnneesScolaires, Examen examen) throws Exception {
		 
		 model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
		 model.addAttribute("anneeScolaire",  examen.getAnneeScolaire()) ;
		 
		 ResponseEntity<Examen[]> response = rest.getForEntity("http://academia-programmation/ws/examen/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Examen[].class, etablissement.getId(), examen.getAnneeScolaire().getId()) ;
		 List<Examen> listeExamens = Arrays.asList(response.getBody());
		 model.addAttribute("listeExamens", listeExamens) ;
		 
		 examen.setAnneeScolaire(examen.getAnneeScolaire());
		 model.addAttribute("examen", examen) ;
		 return "programmation/examens" ;
	}
	
	
	
	@GetMapping ("/programmation/ajout-examen")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, @SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire) throws Exception {
		 Examen examen = new Examen () ;
		 examen.setAnneeScolaire(anneeScolaire);
		 model.addAttribute("examen", examen) ;
		 return "programmation/ajout-examen" ;
	}
	
	@PostMapping ("/programmation/examen/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  
			 @SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire,  Examen examen) throws Exception {
		try {
			examen.setCreateur(utilisateurCourant.getEmail());
			examen.setModificateur(utilisateurCourant.getEmail());
			examen.setEtablissement(etablissement) ;
			examen.setAnneeScolaire(anneeScolaire);
			ResponseEntity<Examen> response = rest.postForEntity("http://academia-programmation/ws/examen/ajout", examen, Examen.class);			
			return "redirect:/programmation/examens" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model, etablissement, anneeScolaire);
			model.addAttribute("examen", examen) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/ajout-examen" ;
		}
	}
	
	
	@GetMapping ("/programmation/modification-examen/{id}")
	public String initMaj (Model model, @PathVariable("id") Long id, @SessionAttribute("etablissementCourant") Etablissement etablissement,
			@SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire) throws Exception {
		Examen examen = rest.getForObject("http://academia-programmation/ws/examen/etablissement/{idEtablissement}/id/{id}", 
				Examen.class, etablissement.getId(),  id) ;
		model.addAttribute("examenExistant", examen) ;
		return "programmation/modification-examen" ;
	}
	
	
	@PostMapping ("/programmation/examen/modification")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, @SessionAttribute("etablissementCourant") Etablissement etablissement, 
			 @SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire, Examen examenExistant) throws Exception {
		try {
			Examen examen = rest.getForObject("http://academia-programmation/ws/examen/etablissement/{idEtablissement}/id/{id}", 
					Examen.class, etablissement.getId(), examenExistant.getId()) ;
			examen.setModificateur(utilisateurCourant.getEmail()) ;
			examen.setDateDebut(examenExistant.getDateDebut());
			examen.setDateFin(examenExistant.getDateFin()) ;
			examen.setTypeExamen(examenExistant.getTypeExamen());
			examen.setLibelle(examenExistant.getLibelle());
			examen.setSemestre(examenExistant.getSemestre());
			examen.setTrimestre(examenExistant.getTrimestre());
			ResponseEntity<Examen> response =  rest.postForEntity("http://academia-programmation/ws/examen/modification", examen, Examen.class);
			return "redirect:/programmation/examens" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model, etablissement, anneeScolaire);
			model.addAttribute("examenExistant", examenExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/modification-examen" ;
		}
	}

	
	
	@GetMapping ("/programmation/details-examen/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Examen examen = rest.getForObject("http://academia-programmation/ws/examen/etablissement/{idEtablissement}/id/{id}", 
				Examen.class, etablissement.getId(),  id) ;
		model.addAttribute("examenExistant", examen) ;
		
		ResponseEntity<CentreExamen[]> response = rest.getForEntity("http://academia-programmation/ws/centreexamen/etablissement/{idEtablissement}/examen/{idExamen}", 
				CentreExamen[].class, etablissement.getId(), id) ;
		model.addAttribute("listeCentresExamen", Arrays.asList(response.getBody())) ;
		return "programmation/details-examen" ;
	}
	
	
	private void initDependencies0 (Model model, Etablissement etablissement, AnneeScolaire anneeScolaireCourante) {
		ResponseEntity<AnneeScolaire[]> response = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}", 
				AnneeScolaire[].class, etablissement.getId()) ;
		 List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response.getBody());
		 model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
		 
		 ResponseEntity<Trimestre[]> response3 = rest.getForEntity("http://academia-programmation/ws/trimestre/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Trimestre[].class, etablissement.getId(), anneeScolaireCourante.getId()) ;
		 List<Trimestre> listeTrimestres = Arrays.asList(response3.getBody());
		 model.addAttribute("listeTrimestres", listeTrimestres) ;
		 
		 ResponseEntity<Semestre[]> response4 = rest.getForEntity("http://academia-programmation/ws/semestre/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Semestre[].class, etablissement.getId(), anneeScolaireCourante.getId()) ;
		 List<Semestre> listeSemestres = Arrays.asList(response4.getBody());
		 model.addAttribute("listeSemestres", listeSemestres) ;
		 
		 ResponseEntity<TypeExamen[]> response5 = rest.getForEntity("http://academia-programmation/ws/typeexamen/etablissement/{idEtablissement}", 
				 TypeExamen[].class, etablissement.getId()) ;
		 List<TypeExamen> listeTypesExamen = Arrays.asList(response5.getBody());
		 model.addAttribute("listeTypesExamen", listeTypesExamen) ;
	}
	
	
	private void initDependencies (Model model, Etablissement etablissement, AnneeScolaire anneeScolaireCourante) {
		 ResponseEntity<AnneeScolaire[]> response = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}", 
				AnneeScolaire[].class, etablissement.getId()) ;
		 List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response.getBody());
		 model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
		 
		 ResponseEntity<Trimestre[]> response3 = rest.getForEntity("http://academia-programmation/ws/trimestre/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Trimestre[].class, etablissement.getId(), anneeScolaireCourante.getId()) ;
		 List<Trimestre> listeTrimestres = Arrays.asList(response3.getBody());
		 model.addAttribute("listeTrimestres", listeTrimestres) ;
		 
		 ResponseEntity<Semestre[]> response4 = rest.getForEntity("http://academia-programmation/ws/semestre/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Semestre[].class, etablissement.getId(), anneeScolaireCourante.getId()) ;
		 List<Semestre> listeSemestres = Arrays.asList(response4.getBody());
		 model.addAttribute("listeSemestres", listeSemestres) ;
		 
		 ResponseEntity<TypeExamen[]> response5 = rest.getForEntity("http://academia-programmation/ws/typeexamen/etablissement/{idEtablissement}", 
				 TypeExamen[].class, etablissement.getId()) ;
		 List<TypeExamen> listeTypesExamen = Arrays.asList(response5.getBody());
		 model.addAttribute("listeTypesExamen", listeTypesExamen) ;
	}
}
