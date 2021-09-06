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
import cm.deepdream.academia.programmation.data.Candidat;
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.data.SalleExamen;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "anneeScolaireCourante", "anneeScolaire",
	"listeAnneesScolaires", "examenCourant", "listeExamens"})
public class CentreExamenCtrl implements Serializable{
	private Logger logger = Logger.getLogger(CentreExamenCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/programmation/centres-examen")
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
		 
		 Examen examen = listeExamens.size() == 0 ? null:listeExamens.get(0) ;
		 model.addAttribute("examenCourant", examen) ;
		 
		 ResponseEntity<CentreExamen[]> response1 = rest.getForEntity("http://academia-programmation/ws/centreexamen/etablissement/{idEtablissement}/examen/{idExamen}", 
				 CentreExamen[].class, etablissement.getId(), examen == null ? 0L:examen.getId()) ;
		 List<CentreExamen> listeCentresExamen = Arrays.asList(response1.getBody());
		 model.addAttribute("listeCentresExamen", listeCentresExamen) ;
		 		 
		 CentreExamen centreExamen = new CentreExamen () ;
		 examen.setAnneeScolaire(anneeScolaireCourante);
		 centreExamen.setAnneeScolaire(anneeScolaireCourante);
		 centreExamen.setExamen(examen);
		 model.addAttribute("centreExamen", centreExamen) ;
		 return "programmation/centres-examen" ;
	}
	
	
	
	@PostMapping ("/programmation/centreexamen/recherche")
	public String rechercher (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@SessionAttribute("listeAnneesScolaires") List<AnneeScolaire> listeAnneesScolaires,
			@SessionAttribute("listeExamens") List<Examen> listeExamens,
			CentreExamen centreExamen) throws Exception {
		 
		 model.addAttribute("listeAnneesScolaires", listeAnneesScolaires) ;
		 model.addAttribute("anneeScolaire",  centreExamen.getAnneeScolaire()) ;
		 
		 model.addAttribute("listeExamens", listeExamens) ;
		 
		 ResponseEntity<CentreExamen[]> response = rest.getForEntity("http://academia-programmation/ws/centreexamen/etablissement/{idEtablissement}/examen/{idExamen}", 
				 CentreExamen[].class, etablissement.getId(), centreExamen.getExamen().getId()) ;
		 List<CentreExamen> listeCentresExamen = Arrays.asList(response.getBody());
		 model.addAttribute("listeCentresExamen", listeCentresExamen) ;
		 
		 Examen examen = rest.getForObject("http://academia-programmation/ws/examen/etablissement/{idEtablissement}/id/{id}", 
					Examen.class, etablissement.getId(), centreExamen.getExamen().getId()) ;
		 model.addAttribute("examenCourant",  examen) ;
		 
		 centreExamen.setExamen(examen) ;
		 return "programmation/centres-examen" ;
	}
	
	
	
	@GetMapping ("/programmation/ajout-centreexamen")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, @SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire) throws Exception {
		CentreExamen centreExamen = new CentreExamen() ;
		centreExamen.setEtablissement(etablissement);
		centreExamen.setAnneeScolaire(anneeScolaire);
		model.addAttribute("centreExamen", centreExamen) ;
		return "programmation/ajout-centreexamen" ;
	}
	
	
	@GetMapping ("/programmation/ajout-centreexamen/{idExamen}")
	public String initAjout1 (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateur, 
			@SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire,
			@PathVariable("idExamen") Long idExamen) throws Exception {
		Examen examen = rest.getForObject("http://academia-programmation/ws/examen/etablissement/{idEtablissement}/id/{id}", 
				Examen.class, etablissement.getId(), idExamen) ;
		CentreExamen centreExamen = new CentreExamen() ;
		centreExamen.setEtablissement(etablissement);
		centreExamen.setAnneeScolaire(anneeScolaire);
		centreExamen.setExamen(examen);
		model.addAttribute("examenCourant", examen) ;
		model.addAttribute("centreExamen", centreExamen) ;
		return "programmation/ajout-centreexamen" ;
	}
	
	
	@PostMapping ("/programmation/centreexamen/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  
			 @SessionAttribute ("examenCourant") Examen examen, 
			 @SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire,  CentreExamen centreExamen) throws Exception {
		try {
			centreExamen.setCreateur(utilisateurCourant.getEmail());
			centreExamen.setModificateur(utilisateurCourant.getEmail());
			centreExamen.setEtablissement(etablissement) ;
			centreExamen.setExamen(examen);
			centreExamen.setAnneeScolaire(anneeScolaire);
			ResponseEntity<CentreExamen> response = rest.postForEntity("http://academia-programmation/ws/centreexamen/ajout", centreExamen, CentreExamen.class);			
			return "redirect:/programmation/centres-examen" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			remplirModele(model, centreExamen);
			model.addAttribute("centreExamen", centreExamen) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/ajout-centreexamen" ;
		}
	}
	
	
	@GetMapping ("/programmation/modification-centreexamen/{id}")
	public String initMaj (Model model, @PathVariable("id") Long id, @SessionAttribute("etablissementCourant") Etablissement etablissement,
			@SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire) throws Exception {
		CentreExamen centreExamen = rest.getForObject("http://academia-programmation/ws/centreexamen/etablissement/{idEtablissement}/id/{id}", 
				CentreExamen.class, etablissement.getId(),  id) ;
		model.addAttribute("centreExamenExistant", centreExamen) ;
		return "programmation/modification-centreexamen" ;
	}
	
	
	@PostMapping ("/programmation/centreexamen/modification")
	public String modifier (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute("etablissementCourant") Etablissement etablissement, @SessionAttribute ("examenCourant") Examen examen, 
			@SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire, CentreExamen centreExamenExistant) throws Exception {
		try {
			CentreExamen centreExamen = rest.getForObject("http://academia-programmation/ws/centreexamen/etablissement/{idEtablissement}/id/{id}", 
					CentreExamen.class, etablissement.getId(), centreExamenExistant.getId()) ;
			centreExamen.setModificateur(utilisateurCourant.getEmail()) ;
			centreExamen.setLibelle(centreExamenExistant.getLibelle());
			centreExamen.setNumero(centreExamenExistant.getNumero());
			centreExamen.setExamen(examen);
			centreExamen.setAnneeScolaire(anneeScolaire);
			ResponseEntity<CentreExamen> response =  rest.postForEntity("http://academia-programmation/ws/centreexamen/modification", centreExamen, CentreExamen.class);
			return "redirect:/programmation/centres-examen" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			remplirModele(model, centreExamenExistant);
			model.addAttribute("centreExamenExistant", centreExamenExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/modification-centreexamen" ;
		}
	}
	
	
	@GetMapping ("/programmation/details-centreexamen-1/{id}")
	public String getDetails_1 (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		CentreExamen centreExamen = rest.getForObject("http://academia-programmation/ws/centreexamen/etablissement/{idEtablissement}/id/{id}", 
				CentreExamen.class, etablissement.getId(),  id) ;
		model.addAttribute("centreExamenExistant", centreExamen) ;
		
		ResponseEntity<SalleExamen[]> response = rest.getForEntity("http://academia-programmation/ws/salleexamen/etablissement/{idEtablissement}/centreexamen/{idCentreExamen}", 
				SalleExamen[].class, etablissement.getId(), id) ;
		model.addAttribute("listeSallesExamen", Arrays.asList(response.getBody())) ;
		return "programmation/details-centreexamen-1" ;
	}
	
	

	@GetMapping ("/programmation/details-centreexamen-2/{id}")
	public String getDetails_2 (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		CentreExamen centreExamen = rest.getForObject("http://academia-programmation/ws/centreexamen/etablissement/{idEtablissement}/id/{id}", 
				CentreExamen.class, etablissement.getId(),  id) ;
		model.addAttribute("centreExamenExistant", centreExamen) ;
		
		ResponseEntity<Candidat[]> response = rest.getForEntity("http://academia-programmation/ws/candidat/etablissement/{idEtablissement}/centreexamen/{idCentreExamen}", 
				Candidat[].class, etablissement.getId(), id) ;
		model.addAttribute("listeCandidats", Arrays.asList(response.getBody())) ;
		return "programmation/details-centreexamen-2" ;
	}
	
	
	private void remplirModele (Model model, CentreExamen centreExamen) {
		model.addAttribute("libelle", centreExamen.getLibelle()) ;
	}
	
}
