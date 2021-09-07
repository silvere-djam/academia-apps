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
import cm.deepdream.academia.programmation.data.Candidat;

@RestController
@RequestMapping("/api/candidat")
public class CandidatAPI {
	private Logger logger = Logger.getLogger(CandidatAPI.class.getName()) ;
	@LoadBalanced
	@Autowired
	private RestTemplate rest ;
	
	@GetMapping("/etablissement/{idEtablissement}/salleexamen/{idSalleExamen}")
	public List<Candidat> getById(@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idSalleExamen") Long idSalleExamen){
		 logger.info("Recherche  des candidats de la salle d'examen "+idSalleExamen+" l'établissement "+idEtablissement);
		 ResponseEntity<Candidat[]> response = rest.getForEntity("http://academia-programmation/ws/candidat/etablissement/{idEtablissement}/salleexamen/{idSalleExamen}",  
				 Candidat[].class, idEtablissement, idSalleExamen) ;
		 List<Candidat> listeCandidats = Arrays.asList(response.getBody());
		 return listeCandidats ;
	}

}
