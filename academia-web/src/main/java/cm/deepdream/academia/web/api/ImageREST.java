package cm.deepdream.academia.web.api;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.programmation.data.Photo;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Logo;
@SessionAttributes({"etablissementCourant"})
@RestController
public class ImageREST implements Serializable{
	private static final String FILE_EXTENSION = ".jpg";
	@Autowired
	@LoadBalanced 
	private RestTemplate rest ;
	
	
	@RequestMapping(path = "/eleve/photo/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> photoEleve(@PathVariable("id") String id, 
    		@SessionAttribute ("etablissementCourant") Etablissement etablissement) throws IOException {
		Photo photo = rest.getForObject("http://academia-programmation/ws/eleve/photo/etablissement/{idEtablissement}/id/{id}", 
				Photo.class, etablissement.getId(), id) ;	
		
		if(photo == null) {
			return ResponseEntity.noContent().build();
		}

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+photo.getFileName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        ByteArrayResource resource = new ByteArrayResource(Base64.decodeBase64(photo.getBytesStr()));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(photo.getSize())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    } 
	
	
	@RequestMapping(path = "/enseignant/photo/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> photoEnseignant(@PathVariable("id") String id, 
    		@SessionAttribute ("etablissementCourant") Etablissement etablissement) throws IOException {
		Photo photo = rest.getForObject("http://academia-programmation/ws/enseignant/photo/etablissement/{idEtablissement}/id/{id}", 
				Photo.class, etablissement.getId(), id) ;	
		
		if(photo == null) {
			return ResponseEntity.noContent().build();
		}

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+photo.getFileName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        ByteArrayResource resource = new ByteArrayResource(Base64.decodeBase64(photo.getBytesStr()));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(photo.getSize())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    } 
	
	
	@RequestMapping(path = "/etablissement/logo/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getLogo(@PathVariable("id") Long id) throws IOException {
		Logo logo = rest.getForObject("http://academia-souscription/ws/etablissement/logo/id/{id}", 
				Logo.class, id) ;	
		
		if(logo == null) {
			return ResponseEntity.noContent().build();
		}

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=logo"+logo.getFileName());
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ByteArrayResource resource = new ByteArrayResource(logo.getBytes());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(logo.getSize())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    } 

	
	@RequestMapping(path = "/captcha/{idSession}", method = RequestMethod.GET)
    public ResponseEntity<Resource> captcha(@PathVariable("idSession") String idSession) throws IOException {
		File captchaFile = new File(getCaptchaFolder(), "captcha-"+idSession+FILE_EXTENSION) ;
		byte[] allBytes = Files.readAllBytes(Paths.get(captchaFile.getAbsolutePath()));
        
				HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+captchaFile.getAbsolutePath());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        ByteArrayResource resource = new ByteArrayResource(allBytes);

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(allBytes.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    } 
	
	private File getCaptchaFolder() {
		File parent = new File("captcha") ;
		if(parent.exists()) {
			parent.mkdir() ;
		}
		return parent ;
	}

}
