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
import cm.deepdream.academia.programmation.data.Niveau;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.programmation.data.TypeExamen;

@Controller
@SessionAttributes({"utilisateurCourant", "etablissementCourant", "listeNiveaux"})
public class TypeExamenCtrl implements Serializable{
	private Logger logger = Logger.getLogger(TypeExamenCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/parametrage/types-examen")
	public String indexTypeExamen (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 
		 ResponseEntity<TypeExamen[]> response = rest.getForEntity("http://academia-programmation/ws/typeexamen/etablissement/{idEtablissement}", 
				 TypeExamen[].class, etablissement.getId()) ;
		 List<TypeExamen> listeTypesExamen = Arrays.asList(response.getBody());
		 model.addAttribute("listeTypesExamen", listeTypesExamen) ;
		 initDependencies(model, etablissement);
		 TypeExamen typeExamen = new TypeExamen () ;
		 return "parametrage/types-examen" ;
	}
	
	
	@GetMapping ("/parametrage/ajout-typeexamen")
	public String initAjout (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		TypeExamen typeExamen = new TypeExamen() ;
		typeExamen.setEtablissement(etablissement);
		model.addAttribute("typeExamen", typeExamen) ;
		return "parametrage/ajout-typeexamen" ;
	}
	
	@PostMapping ("/parametrage/typeexamen/ajout")
	public String ajouter (Model model,   @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, 
			 @SessionAttribute ("etablissementCourant") Etablissement etablissement,  TypeExamen typeExamen) throws Exception {
		try {
			typeExamen.setCreateur(utilisateurCourant.getEmail());
			typeExamen.setModificateur(utilisateurCourant.getEmail());
			typeExamen.setEtablissement(etablissement) ;
			ResponseEntity<TypeExamen> response = rest.postForEntity("http://academia-programmation/ws/typeexamen/ajout", typeExamen, TypeExamen.class);			
			return "redirect:/parametrage/types-examen" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			model.addAttribute("typeExamen", typeExamen) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/ajout-typeexamen" ;
		}
	}
	
	
	@GetMapping ("/parametrage/modification-typeexamen/{id}")
	public String initMaj (Model model, @PathVariable("id") Long id, @SessionAttribute("etablissementCourant") Etablissement etablissement,
			@SessionAttribute("anneeScolaireCourante") AnneeScolaire anneeScolaire) throws Exception {
		TypeExamen typeExamen = rest.getForObject("http://academia-programmation/ws/typeexamen/etablissement/{idEtablissement}/id/{id}", 
				TypeExamen.class, etablissement.getId(),  id) ;
		model.addAttribute("typeExamenExistant", typeExamen) ;
		initDependencies(model, etablissement);
		return "parametrage/modification-typeexamen" ;
	}
	
	
	@PostMapping ("/parametrage/typeexamen/modification")
	public String maj (Model model, @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant, @SessionAttribute("etablissementCourant") Etablissement etablissement, 
			 TypeExamen typeExamenExistant) throws Exception {
		try {
			TypeExamen typeExamen = rest.getForObject("http://academia-programmation/ws/typeexamen/etablissement/{idEtablissement}/id/{id}", 
					TypeExamen.class, etablissement.getId(), typeExamenExistant.getId()) ;
			typeExamen.setModificateur(utilisateurCourant.getTelephone()) ;
			typeExamen.setLibelle(typeExamenExistant.getLibelle());
			typeExamen.setPeriodicite(typeExamenExistant.getPeriodicite()) ;
			typeExamen.setNiveau(typeExamenExistant.getNiveau());
			ResponseEntity<TypeExamen> response = rest.postForEntity("http://academia-programmation/ws/typeexamen/modification", typeExamen, TypeExamen.class);
			return "redirect:/parametrage/types-examen" ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			initDependencies(model, etablissement);
			model.addAttribute("typeExamenExistant", typeExamenExistant) ;
			model.addAttribute("messageEchec", "Echec de l'opération") ;
			return "parametrage/modification-examen" ;
		}
	}
	
	
	
	@GetMapping ("/parametrage/details-typeexamen/{id}")
	public String initDetails (Model model, @PathVariable("id") long id, @SessionAttribute("etablissementCourant") Etablissement etablissement) throws Exception {
		TypeExamen typeTypeExamen = rest.getForObject("http://academia-programmation/ws/typeexamen/etablissement/{idEtablissement}/id/{id}", 
				TypeExamen.class, etablissement.getId(),  id) ;
		model.addAttribute("typeExamenExistant", typeTypeExamen) ;
		return "parametrage/details-typeexamen" ;
	}
	
	
	private void initDependencies(Model model, Etablissement etablissement) {
		ResponseEntity<Niveau[]> response = rest.getForEntity("http://academia-programmation/ws/niveau/etablissement/{idEtablissement}", 
				Niveau[].class, etablissement.getId()) ;
		 List<Niveau> listeNiveaux = Arrays.asList(response.getBody());
		 model.addAttribute("listeNiveaux", listeNiveaux) ;
	}
	
}
