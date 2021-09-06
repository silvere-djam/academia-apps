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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.souscription.data.Etablissement;

@Controller
public class EtablissementCtrl implements Serializable{
	private Logger logger = Logger.getLogger(EtablissementCtrl.class.getName()) ;
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/souscription/etablissements")
	public String index (Model model) throws Exception {
		 ResponseEntity<Etablissement[]> response = rest.getForEntity("http://academia-souscription/ws/etablissement/all", Etablissement[].class) ;
		 List<Etablissement> listeEtablissements = Arrays.asList(response.getBody());
		 model.addAttribute("listeEtablissements", listeEtablissements) ;
		 return "souscription/etablissements" ;
	}
		
	@GetMapping ("/souscription/details-etablissement/{id}")
	public String initDetails (Model model, @PathVariable("id") Long id) throws Exception {
		Etablissement etablissement = rest.getForObject("http://academia-souscription/ws/etablissement/id/{id}", Etablissement.class, id) ;
		model.addAttribute("etablissementExistant", etablissement) ;
		return "souscription/details-etablissement" ;
	}
}
