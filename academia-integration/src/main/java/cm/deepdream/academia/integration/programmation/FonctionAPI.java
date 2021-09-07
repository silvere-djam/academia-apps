package cm.deepdream.academia.integration.programmation;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.programmation.data.Fonction;

@RestController
@RequestMapping("/api/fonction")
public class FonctionAPI {
	private Logger logger = Logger.getLogger(FonctionAPI.class.getName()) ;
	@LoadBalanced
	@Autowired
	private RestTemplate rest ;
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Fonction> getAll(@PathVariable ("idEtablissement") Long idEtablissement){
		 logger.info("Recherche des fonctions");
		 ResponseEntity<Fonction[]> response = rest.getForEntity("http://academia-programmation/ws/fonction/etablissement/{idEtablissement}",  
				 Fonction[].class, idEtablissement) ;
		 List<Fonction> listeFonctions = Arrays.asList(response.getBody());
		 return listeFonctions ;
	}
	
	@GetMapping("/fonction/{idFonction}")
	public Fonction getDomaineById(@PathVariable("idFonction") Long idFonction){
		 logger.info("Recherche  de la fonction "+idFonction);
		 Fonction fonction = rest.getForObject("http://academia-programmation/ws/fonction/id/{id}",  Fonction.class, idFonction) ;
		 return fonction ;
	}
}
