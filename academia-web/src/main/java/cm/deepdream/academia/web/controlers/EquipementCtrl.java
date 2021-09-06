package cm.deepdream.academia.web.controlers;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.security.data.Equipement;
import cm.deepdream.academia.security.data.Utilisateur ;
import cm.deepdream.academia.souscription.data.Etablissement;
@Controller
@SessionAttributes({"etablissementCourant", "listeUtilisateurs"})
public class EquipementCtrl implements Serializable{
	private Logger logger = Logger.getLogger(EquipementCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;

	
	@GetMapping ("/administration/equipements")
	public String getAll (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<Utilisateur[]> response0 = rest.getForEntity("http://academia-security/ws/utilisateur/etablissement/{idEtablissement}", 
				 Utilisateur[].class, etablissement.getId()) ;
		 List<Utilisateur> listeUtilisateurs = Arrays.asList(response0.getBody());
		 
		 model.addAttribute("listeUtilisateurs", listeUtilisateurs) ;
		 
		 Utilisateur utilisateur = listeUtilisateurs.size() == 0 ? new Utilisateur():listeUtilisateurs.get(0) ;
		
		 ResponseEntity<Equipement[]> response1 = rest.getForEntity("http://academia-security/ws/equipement/etablissement/{idEtablissement}/utilisateur/{idUtilisateur}", 
				 Equipement[].class, etablissement.getId(), utilisateur.getId()==null ? 0L:utilisateur.getId()) ;
		 List<Equipement> listeEquipements = Arrays.asList(response1.getBody());
		 model.addAttribute("listeEquipements", listeEquipements) ;
		 
		 Equipement equipement = new Equipement() ;
		 equipement.setUtilisateur(utilisateur);
		 model.addAttribute("equipement", equipement) ;
		 return "administration/equipements" ;
	}
	
	@PostMapping ("/administration/equipement/recherche")
	public String getByUtilisateur (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement,
			@SessionAttribute ("listeUtilisateurs") List<Utilisateur> listeUtilisateurs, Equipement equipement) throws Exception {
		 model.addAttribute("listeUtilisateurs", listeUtilisateurs) ;
		
		 ResponseEntity<Equipement[]> response1 = rest.getForEntity("http://academia-security/ws/equipement/etablissement/{idEtablissement}/utilisateur/{idUtilisateur}", 
				 Equipement[].class, etablissement.getId(), equipement.getUtilisateur().getId()) ;
		 List<Equipement> listeEquipements = Arrays.asList(response1.getBody());
		 model.addAttribute("listeEquipements", listeEquipements) ;
		 
		 model.addAttribute("equipement", equipement) ;
		 return "administration/equipements" ;
	}
	

}
