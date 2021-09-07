package cm.deepdream.academia.integration.programmation;
import java.util.ArrayList;
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
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.souscription.data.Etablissement;

@RestController
@RequestMapping("/api/classe")
public class ClasseAPI {
	private Logger logger = Logger.getLogger(ClasseAPI.class.getName()) ;
	@LoadBalanced
	@Autowired
	private RestTemplate rest ;
	
	@GetMapping("/etablissement/{idEtablissement}/classe/{idClasse}")
	public Classe getById(@PathVariable("idEtablissement") Long idEtablissement, 
			@PathVariable("idClasse") Long idClasse){
		 logger.info("Recherche  de la classe "+idClasse+" l'établissement "+idEtablissement);
		 Classe classe = rest.getForObject("http://academia-programmation/ws/classe/etablissement/{idEtablissement}/classe/{idClasse}",  
				 Classe.class, idEtablissement, idClasse) ;
		 return classe ;
	}
	

	@GetMapping("/etablissement/{id}")
	public List<Classe> getAll(@PathVariable("id") Long id){
		 logger.info("Recherche des classes de l'établissement "+id);
		 ResponseEntity<Classe[]> response = rest.getForEntity("http://academia-programmation/ws/classe/etablissement/{idEtablissement}",  
				 Classe[].class, id) ;
		 List<Classe> listeClasses = Arrays.asList(response.getBody());

		 return listeClasses ;
	}

}
