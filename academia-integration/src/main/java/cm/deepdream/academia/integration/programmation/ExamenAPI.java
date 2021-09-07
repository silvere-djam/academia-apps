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
import cm.deepdream.academia.programmation.data.Examen;

@RestController
@RequestMapping("/api/examen")
public class ExamenAPI {
	private Logger logger = Logger.getLogger(ExamenAPI.class.getName()) ;
	@LoadBalanced
	@Autowired
	private RestTemplate rest ;
	
	@GetMapping("/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}")
	public List<Examen> getById(@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idAnneeScolaire") Long idAnneeScolaire){
		 logger.info("Recherche  des examens de l'année scolaire '"+idAnneeScolaire+"' l'établissement '"+idEtablissement);
		 ResponseEntity<Examen[]> response = rest.getForEntity("http://academia-programmation/ws/examen/etablissement/{idEtablissement}/anneescolaire/{idAnneeScolaire}",  
				 Examen[].class, idEtablissement, idAnneeScolaire) ;
		 List<Examen> listeExamens = Arrays.asList(response.getBody());
		 return listeExamens ;
	}

}
