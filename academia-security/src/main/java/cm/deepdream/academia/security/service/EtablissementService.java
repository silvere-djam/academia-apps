package cm.deepdream.academia.security.service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.deepdream.academia.security.repository.EtablissementRepository;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.security.dao.SequenceDAO;
@Transactional
@Service
public class EtablissementService {
	private Logger logger = Logger.getLogger(EtablissementService.class.getName()) ;
	@Autowired
	private EtablissementRepository etablissementRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	
	public Etablissement creer (Etablissement etablissement) throws Exception{
		try {
			etablissementRepository.save(etablissement) ;
			return etablissement ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Etablissement modifier (Etablissement etablissement) throws Exception{
		try {
			etablissementRepository.save(etablissement) ;
			return etablissement ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (long idEtablissement) throws Exception {
		try {
			Etablissement etablissement = etablissementRepository.findById(idEtablissement).orElseThrow(Exception::new) ;
			etablissementRepository.delete(etablissement) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public Etablissement rechercher (long idEtablissement) throws Exception {
		try {
			Etablissement etablissement = etablissementRepository.findById(idEtablissement).orElseThrow(Exception::new) ;
			return etablissement ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public void supprimer (Etablissement etablissement) throws Exception{
		try {
			etablissementRepository.delete(etablissement) ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	public List<Etablissement> rechercher (Etablissement etablissement) throws Exception {
		try {
			Iterable<Etablissement> source = etablissementRepository.findAll() ;
			List<Etablissement> cible = new ArrayList<Etablissement>();
			source.forEach(cible::add) ;
			return cible ;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

}
