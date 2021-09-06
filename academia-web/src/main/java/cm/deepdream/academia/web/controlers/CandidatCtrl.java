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
import cm.deepdream.academia.programmation.data.CentreExamen;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.Candidat;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.Examen;
import cm.deepdream.academia.programmation.data.SalleExamen;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "anneeScolaireCourante", "examenCourant", "listeExamens", 
	"listeCentresExamen", "listeSallesExamen", "listeEleves"})
public class CandidatCtrl implements Serializable{
	private Logger logger = Logger.getLogger(CandidatCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/programmation/candidats")
	public String indexExamen (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@SessionAttribute ("anneeScolaireCourante") AnneeScolaire anneeScolaireCourante) throws Exception {

		 ResponseEntity<Examen[]> response = rest.getForEntity("http://academia-programmation/ws/examen/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}", 
				 Examen[].class, etablissement.getId(), anneeScolaireCourante.getId()) ;
		 List<Examen> listeExamens = Arrays.asList(response.getBody());
		 model.addAttribute("listeExamens", listeExamens) ;
		 
		 Examen examen = listeExamens.size() == 0 ? null:listeExamens.get(0) ;
		 model.addAttribute("examenCourant", examen) ;
		 
		 ResponseEntity<CentreExamen[]> response1 = rest.getForEntity("http://academia-programmation/ws/centreexamen/etablissement/{idEtablissement}/examen/{idExamen}", 
				 CentreExamen[].class, etablissement.getId(), examen == null ? 0:examen.getId()) ;
		 List<CentreExamen> listeCentresExamen = Arrays.asList(response1.getBody());
		 model.addAttribute("listeCentresExamen", listeCentresExamen) ;
		 
		 CentreExamen centreExamen = listeCentresExamen.size() == 0 ? null:listeCentresExamen.get(0) ;
		 model.addAttribute("centreExamenCourant", centreExamen) ;
		 
		 ResponseEntity<SalleExamen[]> response2 = rest.getForEntity("http://academia-programmation/ws/salleexamen/etablissement/{idEtablissement}/centreexamen/{idCentreExamen}", 
				 SalleExamen[].class, etablissement.getId(), centreExamen == null ? 0:centreExamen.getId()) ;
		 List<SalleExamen> listeSallesExamen = Arrays.asList(response2.getBody());
		 model.addAttribute("listeSallesExamen", listeSallesExamen) ;
		 
		 SalleExamen salleExamen = listeSallesExamen.size() == 0 ? null:listeSallesExamen.get(0) ;
		 model.addAttribute("salleExamenCourante", salleExamen) ;
		 
		 ResponseEntity<Candidat[]> response3 = rest.getForEntity("http://academia-programmation/ws/candidat/etablissement/{idEtablissement}/salleexamen/{idSalleExamen}", 
				 Candidat[].class, etablissement.getId(), salleExamen == null ? 0:salleExamen.getId()) ;
		 List<Candidat> listeCandidats = Arrays.asList(response3.getBody());
		 model.addAttribute("listeCandidats", listeCandidats) ;
		 		 
		 Candidat candidat = new Candidat () ;
		 candidat.setExamen(examen);
		 candidat.setCentreExamen(centreExamen);
		 candidat.setSalleExamen(salleExamen);
		 model.addAttribute("candidat", candidat) ;
		 return "programmation/candidats" ;
	}
	
	
	
	@PostMapping ("/programmation/candidat/recherche")
	public String rechercher (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement, 
			Candidat candidat) throws Exception {
		 
		 ResponseEntity<Candidat[]> response = rest.getForEntity("http://academia-programmation/ws/candidat/etablissement/{idEtablissement}/salleexamen/{idSalleExamen}", 
				 Candidat[].class, etablissement.getId(), candidat.getSalleExamen().getId()) ;
		 List<Candidat> listeCandidats = Arrays.asList(response.getBody());
		 
		 model.addAttribute("candidat", candidat) ;
		 model.addAttribute("listeCandidats", listeCandidats) ;
		 return "programmation/candidats" ;
	}
	
	
	
	@GetMapping ("/programmation/ajout-candidat")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			@SessionAttribute("examenCourant") Examen examen,
			@SessionAttribute("centreExamenCourant") CentreExamen centreExamen) throws Exception {
		Candidat candidat = new Candidat() ;
		candidat.setEtablissement(etablissement);
		candidat.setExamen(examen);
		candidat.setCentreExamen(centreExamen);
		model.addAttribute("candidat", candidat) ;
		return "programmation/ajout-candidat" ;
	}
	
	
	@GetMapping ("/programmation/ajout-candidat/{idCentreExamen}")
	public String initAjout2 (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,   
			@PathVariable("idCentreExamen") Long idCentreExamen) throws Exception {
		CentreExamen centreExamen = rest.getForObject("http://academia-programmation/ws/centreexamen/etablissement/{idEtablissement}/id/{id}", 
				CentreExamen.class, etablissement.getId(),  idCentreExamen) ;
		Candidat candidat = new Candidat() ;
		candidat.setEtablissement(etablissement);
		candidat.setExamen(centreExamen.getExamen());
		candidat.setCentreExamen(centreExamen);
		model.addAttribute("centreExamenCourant", centreExamen) ;
		model.addAttribute("examenCourant", centreExamen.getExamen()) ;
		model.addAttribute("candidat", candidat) ;
		initDependencies(model, etablissement, centreExamen);
		return "programmation/ajout-candidat" ;
	}
	
	
	@PostMapping ("/programmation/candidat/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  @SessionAttribute ("examenCourant") Examen examen, 
			 @SessionAttribute ("centreExamenCourant") CentreExamen centreExamen,  Candidat candidat) throws Exception {
		try {
			logger.info("Candidat : "+candidat);
			candidat.setCreateur(utilisateurCourant.getEmail());
			candidat.setModificateur(utilisateurCourant.getEmail());
			candidat.setEtablissement(etablissement) ;
			candidat.setExamen(examen);
			candidat.setCentreExamen(centreExamen);
			ResponseEntity<Candidat> response = rest.postForEntity("http://academia-programmation/ws/candidat/ajout", candidat, Candidat.class);			
			if(response.getStatusCode() == HttpStatus.OK) {
				return "redirect:/programmation/centres-examen" ;
			}else {
				remplirModele(model, candidat);
				model.addAttribute("candidat", candidat) ;
				model.addAttribute("messageEchec", "Echec de l'opération") ;
				return "programmation/ajout-candidat" ;
			}
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			remplirModele(model, candidat);
			model.addAttribute("candidat", candidat) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/ajout-candidat" ;
		}
	}
	
	
	@GetMapping ("/programmation/modification-candidat/{id}")
	public String initMaj (Model model, @PathVariable("id") Long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Candidat candidat = rest.getForObject("http://academia-programmation/ws/candidat/etablissement/{idEtablissement}/id/{id}", 
				Candidat.class, etablissement.getId(),  id) ;
		model.addAttribute("candidatExistant", candidat) ;
		initDependencies (model, etablissement, candidat.getCentreExamen()) ;
		return "programmation/modification-candidat" ;
	}
	
	
	@PostMapping ("/programmation/candidat/modification")
	public String modifier (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, @SessionAttribute("etablissementCourant") Etablissement etablissement, 
			 @SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire, Candidat candidatExistant) throws Exception {
		try {
			Candidat candidat = rest.getForObject("http://academia-programmation/ws/candidat/etablissement/{idEtablissement}/id/{id}", 
					Candidat.class, etablissement.getId(), candidatExistant.getId()) ;
			candidat.setModificateur(utilisateurCourant.getEmail()) ;
			ResponseEntity<Candidat> response =  rest.postForEntity("http://academia-programmation/ws/candidat/modification", candidat, Candidat.class);
			if(response.getStatusCode() == HttpStatus.OK) {
				return "redirect:/programmation/centres-examen" ;
			}else {
				remplirModele(model, candidat);
				model.addAttribute("candidatExistant", candidatExistant) ;
				model.addAttribute("messageEchec", "Echec de l'opération") ;
				return "programmation/modification-candidat" ;
			}
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			remplirModele(model, candidatExistant);
			model.addAttribute("candidatExistant", candidatExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "programmation/modification-candidat" ;
		}
	}
	
	
	@GetMapping ("/programmation/details-candidat/{id}")
	public String initDetails (Model model, @PathVariable("id") Long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		Candidat candidat = rest.getForObject("http://academia-programmation/ws/centreexamen/etablissement/{idEtablissement}/id/{id}", 
				Candidat.class, etablissement.getId(),  id) ;
		model.addAttribute("candidatExistant", candidat) ;
		return "programmation/details-candidat" ;
	}
	
	
	private void remplirModele (Model model, Candidat candidat) {
		model.addAttribute("candidatExistant", candidat) ;
		model.addAttribute("numero", candidat.getNumero()) ;
		model.addAttribute("eleve", candidat.getEleve()) ;
		model.addAttribute("salleExamen", candidat.getSalleExamen()) ;
		model.addAttribute("type", candidat.getType()) ;
	}
	
	
	public void initDependencies (Model model, Etablissement etablissement, CentreExamen centreExamen) {
		ResponseEntity<Eleve[]> response0 = rest.getForEntity("http://academia-programmation/ws/eleve/etablissement/{idEtablissement}/niveau/{idNiveau}", 
				Eleve[].class, etablissement.getId(), centreExamen.getExamen().getTypeExamen().getNiveau().getId()) ;
		model.addAttribute("listeEleves", Arrays.asList(response0.getBody())) ;
		
		ResponseEntity<SalleExamen[]> response1 = rest.getForEntity("http://academia-programmation/ws/salleexamen/etablissement/{idEtablissement}/centreexamen/{idCentreExamen}", 
				SalleExamen[].class, etablissement.getId(), centreExamen.getId()) ;
		model.addAttribute("listeSallesExamen", Arrays.asList(response1.getBody())) ;
	}
	
}
