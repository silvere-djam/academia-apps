package cm.deepdream.academia.integration.security;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.integration.model.LoginStatus;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.security.data.Utilisateur;

@RestController
@RequestMapping("/api/auth")
public class AuthentificationAPI {
	private Logger logger = Logger.getLogger(AuthentificationAPI.class.getName()) ;
	@LoadBalanced
	@Autowired
	private RestTemplate rest ;
	
	@GetMapping("/telephone/{telephone}/pwd/{password}")
	public LoginStatus auth(@PathVariable("telephone") String telephone, @PathVariable("password") String password) throws Exception{
		try {
			logger.info("Tentative d'authentification de "+telephone);
			Utilisateur utilisateur = rest.getForObject("http://academia-security/ws/auth/telephone/{telephone}/pwd/{password}", 
				Utilisateur.class, telephone, password) ;
			if(utilisateur == null) {
				return new LoginStatus(null,
						false, 
						LocalDateTime.now(),
						null) ;
			}
			
			AnneeScolaire anneeScolaire = rest.getForObject("http://academia-programmation/ws/anneescolaire/etablissement/{idEtablissement}/courante", 
					 AnneeScolaire.class, utilisateur.getEtablissement().getId()) ;

			LoginStatus loginStatus = new LoginStatus(utilisateur, 
				true,  
				LocalDateTime.now(),
				anneeScolaire) ;
			logger.info("Statut d'authentification "+loginStatus);
			return loginStatus ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw ex ;
		}
	}

}
