package cm.deepdream.academia.web.api;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.souscription.data.Etablissement;

@RestController
@SessionAttributes({"etablissementCourant"})
public class AnneeScolaireREST {
	private Logger logger = Logger.getLogger(AnneeScolaireREST.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@CrossOrigin(origins = "*")
	@GetMapping ("/api/anneesacademiques")
	public  List<AnneeScolaire> getAnneesScolaires (Model model, @SessionAttribute ("etablissementCourant") Etablissement etablissement) throws Exception {
		 ResponseEntity<AnneeScolaire[]> response = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}", 
				 AnneeScolaire[].class, etablissement.getId()) ;
		 List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response.getBody());
		 return listeAnneesScolaires ;
	}
}
