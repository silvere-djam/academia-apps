package cm.deepdream.academia.security.webservice;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.security.data.Session;
import cm.deepdream.academia.security.service.EtablissementService;
import cm.deepdream.academia.security.service.SessionService;

@RestController
@RequestMapping("/ws/session")
public class SessionWS {
	private Logger logger = Logger.getLogger(SessionWS.class.getName()) ;
	@Autowired
	private SessionService sessionService ;
	@Autowired
	private EtablissementService etablissementService ;
	
	@PostMapping("/ajout")
	public Session ajout (@RequestBody Session session) {
		try {
			Session sessionCree = sessionService.creer(session) ;
			return sessionCree ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	@PutMapping("/maj")
	@ResponseStatus(code =  HttpStatus.OK)
	public Session maj (@RequestBody Session session) {
		try {
			Session sessionMaj = sessionService.modifier(session) ;
			return sessionMaj ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@DeleteMapping("/suppr")
	@ResponseStatus(code =  HttpStatus.NO_CONTENT)
	public int suppr (Session session) {
		try {
			sessionService.supprimer(session) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return 0 ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/id/{id}")
	public Session getById (@PathVariable("idEtablissement") long idEtablissement, @PathVariable("id") long id) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			Session session = sessionService.rechercher(etablissement, id) ;
			return session ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}/utilisateur/{idUtilisateur}")
	public List<Session> getByUser(@PathVariable("idUtilisateur") long idUtilisateur) {
		try {
			List<Session> listeSessions = sessionService.rechercherParUtilisateur(idUtilisateur) ;
			return listeSessions ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Session>() ;
		}
	}
	
	@GetMapping("/etablissement/{idEtablissement}")
	public List<Session> getAll (@PathVariable ("idEtablissement") long idEtablissement) {
		try {
			Etablissement etablissement = etablissementService.rechercher(idEtablissement) ;
			List<Session> listeSessions = sessionService.rechercher(etablissement) ;
			return listeSessions ;
		}catch(Exception ex) {
			logger.log (Level.SEVERE, ex.getMessage(), ex) ;
			return new ArrayList<Session>() ;
		}
	}
	
}
