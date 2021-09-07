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

import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.CentreExamen;

@RestController
@RequestMapping("/api/anneescolaire")
public class AnneeScolaireAPI {
	private Logger logger = Logger.getLogger(AnneeScolaireAPI.class.getName()) ;
	@LoadBalanced
	@Autowired
	private RestTemplate rest ;
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<AnneeScolaire> getById(@PathVariable("idEtablissement") Long idEtablissement){
		 logger.info("Recherche  des années scolaires de l'établissement "+idEtablissement);
		 ResponseEntity<AnneeScolaire[]> response = rest.getForEntity("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}",  
				 AnneeScolaire[].class, idEtablissement) ;
		 List<AnneeScolaire> listeAnneesScolaires = Arrays.asList(response.getBody());
		 return listeAnneesScolaires ;
	}

}
