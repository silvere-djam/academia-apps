package cm.deepdream.academia.integration.recognition;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Client;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import cm.deepdream.academia.integration.model.SimpleReponse;
import cm.deepdream.academia.integration.model.SimpleRequete;
import org.apache.commons.codec.binary.Base64;
@RestController
@RequestMapping("/api/requete")
public class RequeteAPI {
	private Logger logger = Logger.getLogger(RequeteAPI.class.getName()) ;
	@Autowired
	private RestTemplate rest ;
	
	

	@PostMapping(path = "/salleexamen/recognize")
	public SimpleReponse recognize (@RequestParam("idSalleExamen") Long idSalleExamen, MultipartFile file) {
		try {
			
			logger.log(Level.INFO, "Recherche dans salle d'examen "+idSalleExamen);
			SimpleRequete requete = new SimpleRequete() ;
			requete.setIdSalleExamen(idSalleExamen) ;
			requete.setPhoto(Base64.encodeBase64String(file.getBytes()));
			RestTemplate restTemplate = new RestTemplate();
			SimpleReponse reponse = restTemplate.postForObject("http://192.168.100.40:8000/api/requete/salleexamen/recognize/", requete, SimpleReponse.class) ;
			logger.log(Level.INFO, "Reponse : Recognized="+reponse.getRecognized()+", Eleve="+reponse.getEleve()+", Confidence="+reponse.getConfidence());
			return reponse ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
		}
		return null ;
	}

}
