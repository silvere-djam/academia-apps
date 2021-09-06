package cm.deepdream.academia.security.service;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.deepdream.academia.souscription.data.Contact;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.security.dao.SequenceDAO;
import cm.deepdream.academia.security.repository.UtilisateurRepository;
import cm.deepdream.academia.security.util.CryptoSystem;
import cm.deepdream.academia.security.util.EmailSender;
import cm.deepdream.academia.security.data.Utilisateur;
import cm.deepdream.academia.security.enums.RoleU;
import cm.deepdream.academia.security.enums.StatutU;
@Transactional
@Service
public class UtilisateurService {
	private Logger logger = Logger.getLogger(UtilisateurService.class.getName()) ;
	@Autowired
	private UtilisateurRepository utilisateurDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private EmailSender emailSender ;
	@Autowired
	private CryptoSystem cryptoSystem ;
	@Autowired
	private Environment env ;
	
	public Utilisateur creer (Utilisateur utilisateur) throws Exception {
		try {
			String url = env.getProperty("app.academia.web.url")+"/administration/validation-email-utilisateur/%s/%s" ;
			utilisateur.setId(sequenceDAO.nextGlobalId(Utilisateur.class.getName()));
			utilisateur.setNum(sequenceDAO.nextId(utilisateur.getEtablissement(), Utilisateur.class.getName())) ;
			utilisateur.setDateCreation(LocalDateTime.now()) ;
			utilisateur.setDateDernMaj(LocalDateTime.now()) ;
			utilisateur.setMotDePasse(new BCryptPasswordEncoder().encode(env.getProperty("app.security.default_password"))) ;
			utilisateur.setDateExpMdp(LocalDateTime.now().plusMonths(Long.parseLong(env.getProperty("app.security.password_validity_period")))) ;
			//@Todo A affiner dans UtilisateurRest - en remplaçant la dateExp par min(dateCreation + 1an + 1 an et date de fin abonnement)
			utilisateur.setDateExp(LocalDateTime.now().plusMonths(Long.parseLong(env.getProperty("app.security.account_validity_period")))) ;
			//utilisateur.setLibelleProfil(TypeProfil.getLibelle(utilisateur.getIdProfil()));
			utilisateur.setPseudonyme(getPseudo(utilisateur.getNom()));
			utilisateur.setStatut(StatutU.Non_valide.name()) ;
			utilisateur.setCodeActivation(RandomStringUtils.randomAlphanumeric(10));
			Utilisateur utilisateurCree = utilisateurDAO.save(utilisateur) ;
			//Send email
			Map<String, Object> templateModel = new HashMap<String, Object>() ;
			templateModel.put("idUtilisateur", String.valueOf(utilisateur.getId())) ;
			templateModel.put("recipientName", utilisateur.getNom()) ;
			templateModel.put("telephone", utilisateur.getTelephone()) ;
			templateModel.put("libelleRole", utilisateur.getLibelleRole()) ;
			templateModel.put("lienApplication", String.format(url, Long.toString(utilisateur.getId()), 
					utilisateur.getCodeActivation())) ;
			templateModel.put("dureeValMdp", env.getProperty("app.security.password_validity_period")) ;
			try {
				emailSender.sendMessage(utilisateur.getEmail(), "Votre compte utilisateur Academia", "email-account-creation.html", templateModel) ;
			}catch(Exception exx) {
				logger.log(Level.SEVERE, exx.getMessage(), exx) ;
			}
			return utilisateurCree ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}	
	
	
	public void creerUtilisateurs (Etablissement etablissement) throws Exception {
		try {
			
			String url = env.getProperty("app.academia.web.url")+"/administration/validation-email-utilisateur/%s/%s" ;
			
			Utilisateur utilisateur1 = new Utilisateur() ;
			utilisateur1.setId(sequenceDAO.nextGlobalId(Utilisateur.class.getName()));
			utilisateur1.setNum(sequenceDAO.nextId(etablissement, Utilisateur.class.getName())) ;
			utilisateur1.setDateCreation(LocalDateTime.now()) ;
			utilisateur1.setDateDernMaj(LocalDateTime.now()) ;
			
			Contact contactChef = etablissement.getContactChef() ;
			Contact contactInformaticien = etablissement.getContactInformaticien() ;
			
			utilisateur1.setNom(contactChef.getNom());
			utilisateur1.setTelephone(contactChef.getTelephone());
			utilisateur1.setEmail(contactChef.getEmail());
			utilisateur1.setEtablissement(etablissement);
			utilisateur1.setMotDePasse(new BCryptPasswordEncoder().encode(env.getProperty("app.security.default_password"))) ;
			utilisateur1.setDateExpMdp(LocalDateTime.now().plusMonths(Long.parseLong(env.getProperty("app.security.password_validity_period")))) ;
			utilisateur1.setDateExp(LocalDateTime.now().plusMonths(Long.parseLong(env.getProperty("app.security.account_validity_period")))) ;
			utilisateur1.setStatut(StatutU.Non_valide.name()) ;
			utilisateur1.setCodeActivation(RandomStringUtils.randomAlphanumeric(10));
			utilisateur1.setIdRole(RoleU.Responsable_Etablissement.getId());
			utilisateur1.setLibelleRole(RoleU.getLibelle(utilisateur1.getIdRole()));
			utilisateur1.setPseudonyme(getPseudo(utilisateur1.getNom()));
			Utilisateur utilisateur1Cree = utilisateurDAO.save(utilisateur1) ;
			//Send email
			Map<String, Object> templateModel1 = new HashMap<String, Object>() ;
			templateModel1.put("libelleRole", utilisateur1.getLibelleRole()) ;
			templateModel1.put("pseudonyme", utilisateur1Cree.getPseudonyme()) ;
			templateModel1.put("telephone", utilisateur1.getTelephone()) ;
			templateModel1.put("lienApplication", String.format(url, Long.toString(utilisateur1.getId()),
					utilisateur1.getCodeActivation())) ;
			try {
				emailSender.sendMessage(utilisateur1.getEmail(), "Votre compte utilisateur Academia", 
						"new-user-message.html", templateModel1) ;
			}catch(Exception exx) {
				logger.log(Level.SEVERE, exx.getMessage(), exx) ;
			}
			
			Utilisateur utilisateur2 = new Utilisateur() ;
			utilisateur2.setId(sequenceDAO.nextGlobalId(Utilisateur.class.getName()));
			utilisateur2.setNum(sequenceDAO.nextId(etablissement, Utilisateur.class.getName())) ;
			utilisateur2.setNom(contactInformaticien.getNom()) ;
			utilisateur2.setEmail(contactInformaticien.getEmail());
			utilisateur2.setTelephone(contactInformaticien.getTelephone());
			utilisateur2.setEtablissement(etablissement);
			utilisateur2.setDateCreation(LocalDateTime.now()) ;
			utilisateur2.setDateDernMaj(LocalDateTime.now()) ;
			utilisateur2.setMotDePasse(new BCryptPasswordEncoder().encode(env.getProperty("app.security.default_password"))) ;
			utilisateur2.setDateExpMdp(LocalDateTime.now().plusMonths(Long.parseLong(env.getProperty("app.security.password_validity_period")))) ;
			utilisateur2.setDateExp(LocalDateTime.now().plusMonths(Long.parseLong(env.getProperty("app.security.account_validity_period")))) ;
			//utilisateur.setLibelleProfil(TypeProfil.getLibelle(utilisateur.getIdProfil()));
			utilisateur2.setCodeActivation(RandomStringUtils.randomAlphanumeric(10));
			utilisateur2.setStatut(StatutU.Non_valide.name()) ;
			utilisateur2.setIdRole(RoleU.Administrateur.getId());
			utilisateur2.setLibelleRole(RoleU.getLibelle(utilisateur2.getIdRole()));
			utilisateur2.setPseudonyme(getPseudo(utilisateur2.getNom()));
			Utilisateur utilisateur2Cree = utilisateurDAO.save(utilisateur2) ;
			//Send email
			Map<String, Object> templateModel2 = new HashMap<String, Object>() ;
			templateModel2.put("libelleRole", utilisateur2.getLibelleRole()) ;
			templateModel2.put("pseudonyme", utilisateur2Cree.getPseudonyme()) ;
			templateModel2.put("telephone", utilisateur2.getTelephone()) ;
			templateModel2.put("lienApplication", String.format(url, Long.toString(utilisateur2.getId()), 
					utilisateur2.getCodeActivation())) ;
			try {
				emailSender.sendMessage(utilisateur2Cree.getEmail(), "Votre compte utilisateur Academia", 
						"new-user-message.html", templateModel2) ;
			}catch(Exception exx) {
				logger.log(Level.SEVERE, exx.getMessage(), exx) ;
			}
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	
	public Utilisateur modifier (Utilisateur utilisateur) throws Exception {
		try {
			utilisateur.setDateCreation(LocalDateTime.now()) ;
			utilisateur.setDateDernMaj(LocalDateTime.now()) ;
			utilisateur.setPseudonyme(getPseudo(utilisateur.getNom()));
			utilisateurDAO.save(utilisateur) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public Utilisateur definirMotDePasse (Utilisateur utilisateur) throws Exception {
		try {
			utilisateur.setDateDernMaj(LocalDateTime.now()) ;
			utilisateur.setPseudonyme(getPseudo(utilisateur.getNom()));
			utilisateur.setStatut(StatutU.Valide.name());
			utilisateurDAO.save(utilisateur) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public Utilisateur modifierMotDePasse (Utilisateur utilisateur) throws Exception {
		try {
			utilisateur.setDateDernMaj(LocalDateTime.now()) ;
			utilisateur.setStatut(StatutU.Valide.name());
			LocalDateTime dateExpMdp = LocalDateTime.now().plusDays(Long.parseLong(env.getProperty("app.security.password_lifetime"))) ;
			utilisateur.setDateExpMdp(dateExpMdp);
			utilisateurDAO.save(utilisateur) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	
	public Utilisateur modifierMdp1 (Utilisateur utilisateur) throws Exception {
		try {
			utilisateur.setDateCreation(LocalDateTime.now()) ;
			utilisateur.setDateDernMaj(LocalDateTime.now()) ;
			utilisateur.setStatut(StatutU.Valide.name()) ;
			utilisateurDAO.save(utilisateur) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public Utilisateur activer (Utilisateur utilisateur) throws Exception {
		try {
			utilisateur.setDateDernMaj(LocalDateTime.now()) ;
			utilisateur.setStatut(StatutU.Valide.name()) ;
			utilisateurDAO.save(utilisateur) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public Utilisateur suspendre (Utilisateur utilisateur) throws Exception {
		try {
			utilisateur.setDateCreation(LocalDateTime.now()) ;
			utilisateur.setDateDernMaj(LocalDateTime.now()) ;
			utilisateur.setStatut(StatutU.Suspendu.name()) ;
			utilisateurDAO.save(utilisateur) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (Utilisateur utilisateur) throws Exception {
		try {
			utilisateurDAO.delete(utilisateur) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (long idUtilisateur) throws Exception {
		try {
			Optional<Utilisateur> optUtilisateur = utilisateurDAO.findById(idUtilisateur) ;
			if(optUtilisateur.isPresent())
				utilisateurDAO.delete(optUtilisateur.get()) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public Utilisateur rechercher (Long id) throws Exception {
		try {
			Optional<Utilisateur> optUtilisateur = utilisateurDAO.findById(id) ;
			if(optUtilisateur.isPresent()) return optUtilisateur.get() ;
			else return null ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public Utilisateur rechercher (Long id, String codeActivation) throws Exception {
		try {
			Utilisateur utilisateur = utilisateurDAO.findByIdAndCodeActivationAndStatut(id, 
					codeActivation, StatutU.Non_valide.toString());
			return utilisateur ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public Utilisateur rechercher (Etablissement etablissement, long id) throws Exception {
		try {
			Utilisateur utilisateur = utilisateurDAO.findByEtablissementAndId(etablissement, id);
			return utilisateur ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public Utilisateur rechercherEmail (String email) throws Exception {
		try {
			Utilisateur utilisateur = utilisateurDAO.findByEmail(email) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public Utilisateur rechercherTelephone (String telephone) throws Exception {
		try {
			Utilisateur utilisateur = utilisateurDAO.findByTelephone(telephone) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Utilisateur> rechercher(Etablissement etablissement) throws Exception {
		try {
			List<Utilisateur> listeUtilisateurs = utilisateurDAO.findByEtablissement(etablissement) ;
			return listeUtilisateurs ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	
	public List<Utilisateur> rechercherTout (Utilisateur utilisateur) throws Exception {
		try {
			Iterable<Utilisateur> itUtilisateurs = utilisateurDAO.findAll() ;
			List<Utilisateur> listeUtilisateurs = new ArrayList() ;
			itUtilisateurs.forEach(listeUtilisateurs::add);
			return listeUtilisateurs ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	private String getPseudo (String nom) {
		List<String> pseudos =  Arrays.asList(nom.split(" ") );
		return pseudos.get(pseudos.size()-1) ;
	}
	
	
}
