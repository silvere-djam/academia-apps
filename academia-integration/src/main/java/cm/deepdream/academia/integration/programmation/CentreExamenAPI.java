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
import cm.deepdream.academia.programmation.data.CentreExamen;

@RestController
@RequestMapping("/api/centreexamen")
public class CentreExamenAPI {
	private Logger logger = Logger.getLogger(CentreExamenAPI.class.getName()) ;
	@LoadBalanced
	@Autowired
	private RestTemplate rest ;
	
	@GetMapping("/etablissement/{idEtablissement}/examen/{idExamen}")
	public List<CentreExamen> getById(@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idExamen") Long idExamen){
		 logger.info("Recherche  des centres de l'examen "+idExamen+" l'établissement "+idEtablissement);
		 ResponseEntity<CentreExamen[]> response = rest.getForEntity("http://academia-programmation/ws/centreexamen/etablissement/{idEtablissement}/examen/{idExamen}",  
				 CentreExamen[].class, idEtablissement, idExamen) ;
		 List<CentreExamen> listeCentresExamen = Arrays.asList(response.getBody());
		 return listeCentresExamen ;
	}

}
