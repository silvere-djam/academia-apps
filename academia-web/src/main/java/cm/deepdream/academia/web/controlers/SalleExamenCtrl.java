package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
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
import cm.deepdream.academia.programmation.data.SalleExamen;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Examen;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "anneeScolaireCourante", 
	"examenCourant", "listeExamens", "centreExamenCourant", "listeCentresExamen"})
public class SalleExamenCtrl implements Serializable{
	private Logger logger = Logger.getLogger(SalleExamenCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/programmation/salles-examen")
	public String indexExamen (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaireCourante) throws Exception {

		 ResponseEntity<Examen[]> response = rest.getForEntity("http://academia-programmation/ws/examen/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Examen[].class, etablissement.getId(), anneeScolaireCourante.getId()) ;
		 List<Examen> listeExamens = Arrays.asList(response.getBody());
		 model.addAttribute("listeExamens", listeExamens) ;
		 
		 Examen examen = listeExamens.size() == 0 ? null:listeExamens.get(0) ;
		 model.addAttribute("examenCourant", examen) ;
		 
		 ResponseEntity<CentreExamen[]> response1 = rest.getForEntity("http://academia-programmation/ws/centreexamen/etablissement/{idEtablissement}/examen/{idExamen}", 
				 CentreExamen[].class, etablissement.getId(),  examen == null ? 0:examen.getId()) ;
		 List<CentreExamen> listeCentresExamen = Arrays.asList(response1.getBody());
		 model.addAttribute("listeCentresExamen", listeCentresExamen) ;
		 
		 CentreExamen centreExamen = listeCentresExamen.size() == 0 ? null:listeCentresExamen.get(0) ;
		 model.addAttribute("centreExamenCourant", centreExamen) ;
		 
		 ResponseEntity<SalleExamen[]> response2 = rest.getForEntity("http://academia-programmation/ws/salleexamen/etablissement/{idEtablissement}/centreexamen/{idCentreExamen}", 
				 SalleExamen[].class, etablissement.getId(), centreExamen == null ? 0:centreExamen.getId()) ;
		 List<SalleExamen> listeSallesExamen = Arrays.asList(response2.getBody());
		 model.addAttribute("listeSallesExamen", listeSallesExamen) ;
		 		 
		 SalleExamen salleExamen = new SalleExamen () ;
		 salleExamen.setExamen(examen);
		 salleExamen.setCentreExamen(centreExamen);
		 model.addAttribute("salleExamen", salleExamen) ;
		 return "programmation/salles-examen" ;
	}
	
	
	
	@PostMapping ("/programmation/salleexamen/recherche")
	public String rechercher (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, SalleExamen salleExamen) throws Exception {
		 
		 ResponseEntity<SalleExamen[]> response = rest.getForEntity("http://academia-programmation/ws/salleexamen/etablissement/{idEtablissement}/centreexamen/{idCentreExamen}", 
				 SalleExamen[].class, etablissement.getId(), salleExamen.getCentreExamen().getId()) ;
		 List<SalleExamen> listeSallesExamen = Arrays.asList(response.getBody());
		 model.addAttribute("listeSallesExamen", listeSallesExamen) ;
		 
		 model.addAttribute("salleExamen", salleExamen) ;
		 return "programmation/salles-examen" ;
	}
	
	
	
	@GetMapping ("/programmation/ajout-salleexamen")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute("examenCourant") Examen examen,
			@SessionAttribute("centreExamenCourant") CentreExamen centreExamen) throws Exception {
		SalleExamen salleExamen = new SalleExamen() ;
		salleExamen.setEtablissement(etablissement);
		salleExamen.setExamen(examen);
		salleExamen.setCentreExamen(centreExamen);
		return "programmation/ajout-salleexamen" ;
	}
	
	
	@GetMapping ("/programmation/ajout-salleexamen/{idCentreExamen}")
	public String initAjout2 (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute("examenCourant") Examen examen,
			@PathVariable("idCentreExamen") Long idCentreExamen) throws Exception {
		CentreExamen centreExamen = rest.getForObject("http://academia-programmation/ws/centreexamen/etablissement/{idEtablissement}/id/{id}", 
				CentreExamen.class, etablissement.getId(),  idCentreExamen) ;
		SalleExamen salleExamen = new SalleExamen() ;
		salleExamen.setEtablissement(etablissement);
		salleExamen.setExamen(examen);
		salleExamen.setCentreExamen(centreExamen);
		model.addAttribute("salleExamen", salleExamen) ;
		model.addAttribute("centreExamenCourant", centreExamen) ;
		model.addAttribute("examenCourant", centreExamen.getExamen()) ;
		return "programmation/ajout-salleexamen" ;
	}
	
	
	@PostMapping ("/programmation/salleexamen/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  
			 @SessionAttribute("examenCourant") Examen examen, 
			 @SessionAttribute("centreExamenCourant") CentreExamen centreExamen,  SalleExamen salleExamen) throws Exception {
		try {
			salleExamen.setCreateur(utilisateurCourant.getEmail());
			salleExamen.setModificateur(utilisateurCourant.getEmail());
			salleExamen.setEtablissement(etablissement) ;
			salleExamen.setExamen(examen) ;
			salleExamen.setCentreExamen(centreExamen);
			ResponseEntity<SalleExamen> response = rest.postForEntity("http://academia-programmation/ws/salleexamen/ajout", salleExamen, SalleExamen.class);			
			if(response.getStatusCode() == HttpStatus.OK) {
				return "redirect:/programmation/centres-examen" ;
			} else {
				remplirModele(model, salleExamen);
				model.addAttribute("salleExamen", salleExamen) ;
				model.addAttribute("messageEchec", "Echec de l'opération") ;
				return "programmation/ajout-salleexamen" ;
			}
			
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			remplirModele(model, salleExamen);
			model.addAttribute("salleExamen", salleExamen) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/ajout-salleexamen" ;
		}
	}
	
	
	@GetMapping ("/programmation/modification-salleexamen/{id}")
	public String initMaj (Model model, @PathVariable("id") Long id, @SessionAttribute("etablissementCourant") Etablissement etablissement,
			@SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire) throws Exception {
		SalleExamen salleExamen = rest.getForObject("http://academia-programmation/ws/salleexamen/etablissement/{idEtablissement}/id/{id}", 
				SalleExamen.class, etablissement.getId(),  id) ;
		model.addAttribute("salleExamenExistante", salleExamen) ;
		return "programmation/modification-salleexamen" ;
	}
	
	
	@PostMapping ("/programmation/salleexamen/modification")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, @SessionAttribute("etablissementCourant") Etablissement etablissement, 
			 @SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire, SalleExamen salleExamenExistante) throws Exception {
		try {
			SalleExamen salleExamen = rest.getForObject("http://academia-programmation/ws/salleexamen/etablissement/{idEtablissement}/id/{id}", 
					SalleExamen.class, etablissement.getId(), salleExamenExistante.getId()) ;
			salleExamen.setModificateur(utilisateurCourant.getEmail()) ;
			salleExamen.setLibelle(salleExamenExistante.getLibelle());
			ResponseEntity<SalleExamen> response =  rest.postForEntity("http://academia-programmation/ws/salleexamen/modification", salleExamen, SalleExamen.class);
			if(response.getStatusCode() == HttpStatus.OK) {
				return "redirect:/programmation/centres-examen" ;
			} else {
				remplirModele(model, salleExamen);
				model.addAttribute("salleExamenExistante", salleExamenExistante) ;
				model.addAttribute("messageEchec", "Echec de l'opération") ;
				return "programmation/modification-salleexamen" ;
			}
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			remplirModele(model, salleExamenExistante);
			model.addAttribute("salleExamenExistante", salleExamenExistante) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/modification-salleexamen" ;
		}
	}
	
	
	@GetMapping ("/programmation/details-salleexamen/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		SalleExamen salleExamen = rest.getForObject("http://academia-programmation/ws/salleexamen/etablissement/{idEtablissement}/id/{id}", 
				SalleExamen.class, etablissement.getId(),  id) ;
		model.addAttribute("salleExamenExistante", salleExamen) ;
		
		ResponseEntity<Candidat[]> response = rest.getForEntity("http://academia-programmation/ws/candidat/etablissement/{idEtablissement}/salleexamen/{idSalleExamen}", 
				Candidat[].class, etablissement.getId(), id) ;
		model.addAttribute("listeCandidats", Arrays.asList(response.getBody())) ;
		return "programmation/details-salleexamen" ;
	}
	
	
	private void remplirModele (Model model, SalleExamen salleExamen) {
		model.addAttribute("libelle", salleExamen.getLibelle()) ;
	}
	
}
