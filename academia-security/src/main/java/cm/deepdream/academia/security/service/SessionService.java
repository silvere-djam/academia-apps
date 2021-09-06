package cm.deepdream.academia.security.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.security.dao.SequenceDAO;
import cm.deepdream.academia.security.repository.SessionRepository;
import cm.deepdream.academia.security.repository.UtilisateurRepository;
import cm.deepdream.academia.security.data.Session;
import cm.deepdream.academia.security.data.Utilisateur;
@Transactional
@Service
public class SessionService {
	private Logger logger = Logger.getLogger(SessionService.class.getName()) ;
	@Autowired
	private SessionRepository sessionRepository ;
	@Autowired
	private UtilisateurRepository utilisateurRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	public Session creer (Session session) throws Exception {
		try {
			session.setId(sequenceDAO.nextGlobalId(Session.class.getName())) ;
			session.setNum(sequenceDAO.nextId(session.getEtablissement(), Session.class.getName())) ;
			sessionRepository.save(session) ;
			return session ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}	
	
	public Session modifier (Session session) throws Exception {
		try {
			sessionRepository.save(session) ;
			return session ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (Session session) throws Exception {
		try {
			sessionRepository.delete(session) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (long idSession) throws Exception {
		try {
			Optional<Session> optSession = sessionRepository.findById(idSession) ;
			if(optSession.isPresent())
				sessionRepository.delete(optSession.get()) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public Session rechercher (Etablissement etablissement, Long id) throws Exception {
		try {
			return sessionRepository.findByEtablissementAndId(etablissement, id) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Session> rechercherParUtilisateur (long idUtilisateur) throws Exception {
		try {
			Optional<Utilisateur> optUtilisateur = utilisateurRepository.findById(idUtilisateur) ;
			if(optUtilisateur.isPresent()) {
				Utilisateur utilisateur = optUtilisateur.get() ;
				List<Session> listeSessions = sessionRepository.findByUtilisateur(utilisateur) ;
				return listeSessions ;
			}
			return new ArrayList<Session>() ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Session> rechercher(Etablissement etablissement) throws Exception {
		try {
			List<Session> listeSessions  = sessionRepository.findByEtablissement(etablissement) ;
			return listeSessions ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Session> rechercher(Session session) throws Exception {
		try {
			Iterable<Session> itSessions = sessionRepository.findAll() ;
			List<Session> listeSessions = new ArrayList() ;
			itSessions.forEach(listeSessions::add);
			return listeSessions ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Session> rechercherTout (Session session) throws Exception {
		try {
			Iterable<Session> itSessions = sessionRepository.findAll() ;
			List<Session> listeSessions = new ArrayList() ;
			itSessions.forEach(listeSessions::add);
			return listeSessions ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
}
