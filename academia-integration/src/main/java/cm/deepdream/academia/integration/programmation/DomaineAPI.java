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
import cm.deepdream.academia.programmation.data.Domaine;

@RestController
@RequestMapping("/api/domaine")
public class DomaineAPI {
	private Logger logger = Logger.getLogger(DomaineAPI.class.getName()) ;
	@LoadBalanced
	@Autowired
	private RestTemplate rest ;
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Domaine> getAll(@PathVariable("idEtablissement") Long idEtablissement){
		 logger.info("Recherche des domaines ");
		 ResponseEntity<Domaine[]> response = rest.getForEntity("http://academia-programmation/ws/domaine/etablissement/{idEtablissement}",  
				 Domaine[].class, idEtablissement) ;
		 List<Domaine> listeDomaines = Arrays.asList(response.getBody());
		 return listeDomaines ;
	}
	
	@GetMapping("/domaine/{idDomaine}")
	public Domaine getDomaineById(@PathVariable("idDomaine") Long idDomaine){
		 logger.info("Recherche  de le domaine "+idDomaine);
		 Domaine domaine = rest.getForObject("http://academia-programmation/ws/domaine/id/{id}",  
				 Domaine.class, idDomaine) ;
		 return domaine ;
	}
}
