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
import cm.deepdream.academia.programmation.data.SalleExamen;

@RestController
@RequestMapping("/api/salleexamen")
public class SalleExamenAPI {
	private Logger logger = Logger.getLogger(SalleExamenAPI.class.getName()) ;
	@LoadBalanced
	@Autowired
	private RestTemplate rest ;
	
	@GetMapping("/etablissement/{idEtablissement}/centreexamen/{idCentreExamen}")
	public List<SalleExamen> getById(@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idCentreExamen") Long idCentreExamen){
		 logger.info("Recherche  des salles d'examen du centre "+idCentreExamen+" l'établissement "+idEtablissement);
		 ResponseEntity<SalleExamen[]> response = rest.getForEntity("http://academia-programmation/ws/salleexamen/etablissement/{idEtablissement}/centreexamen/{idCentreExamen}",  
				 SalleExamen[].class, idEtablissement, idCentreExamen) ;
		 List<SalleExamen> listeSallesExamen = Arrays.asList(response.getBody());
		 return listeSallesExamen ;
	}

}
