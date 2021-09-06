package cm.deepdream.academia.web.service;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.security.data.Utilisateur;

@Service
public class AuthentificationService implements UserDetailsService{	
	private Logger logger = Logger.getLogger(AuthentificationService.class.getName()) ;
	@LoadBalanced 
	@Autowired
	private RestTemplate rest ;
	
	@Autowired
    private HttpServletRequest request ;
 

    
    public UserDetails loadUserByUsername(String telephone) throws UsernameNotFoundException {
    	try {
    		logger.info("Tentative d'authentification de "+telephone);
    		Utilisateur utilisateur = rest.getForObject("http://academia-security/ws/auth/telephone/{telephone}", Utilisateur.class, telephone.trim()) ;
    		UserDetails details = User.withUsername(utilisateur.getTelephone()).password(utilisateur.getMotDePasse()).authorities(utilisateur.getLibelleRole()).build();
    		return details ;
    	}catch(Exception ex) {
    		logger.log(Level.SEVERE, ex.getMessage(), ex); ;
    		throw ex ;
    	}
    }
    
    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
