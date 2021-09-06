package cm.deepdream.academia.souscription.service;
import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.deepdream.academia.souscription.repository.OffreRepository;
import cm.deepdream.academia.souscription.dao.SequenceDAO;
import cm.deepdream.academia.souscription.data.Localite;
import cm.deepdream.academia.souscription.data.Offre;
@Service
@Transactional
public class OffreService {
	private Logger logger = Logger.getLogger(OffreService.class.getName()) ;
	@Autowired
	private OffreRepository offreRepository ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	
	public Offre creer (Offre offre) throws Exception {
		try {
			offre.setId(sequenceDAO.nextGlobalId(Offre.class.getName()));
			offreRepository.save(offre) ;
			return offre ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}	
	
	public Offre modifier (Offre offre) throws Exception {
		try {
			offreRepository.save(offre) ;
			return offre ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (Offre offre) throws Exception {
		try {
			offreRepository.delete(offre) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public void supprimer (long idOffre) throws Exception {
		try {
			Optional<Offre> optOffre = offreRepository.findById(idOffre) ;
			if(optOffre.isPresent())
				offreRepository.delete(optOffre.get()) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public Offre rechercher (Long id) throws Exception {
		try {
			Optional<Offre> optOffre = offreRepository.findById(id) ;
			if(optOffre.isPresent()) return optOffre.get() ;
			else return null ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public BigDecimal rechercherCout (final Integer nbEleves) throws Exception {
		try {
			List<Offre> listeOffres = offreRepository.findByMaxElevesGreaterThanEqual(nbEleves) ;
			List<Offre> listeOffresFiltrees = listeOffres.stream().filter(offre -> {
				return offre.getMaxEleves() >= nbEleves && offre.getMinEleves() <= nbEleves ;
			}).collect(Collectors.toList()) ;
			
			if (listeOffresFiltrees.size() == 0) return BigDecimal.ZERO ;
			Offre offre = listeOffresFiltrees.get(0) ;
			BigDecimal cout = offre.getMontantBase() ;
			for (Integer millier = offre.getMinEleves() ; millier <= nbEleves ; millier = millier + 1000)
				cout = cout.add(offre.getMontantMillier()) ;
			return cout ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public Long rechercherNbUtilisateurs (final Integer nbEleves) throws Exception {
		try {
			List<Offre> listeOffres = offreRepository.findByMaxElevesGreaterThanEqual(nbEleves) ;
			List<Offre> listeOffresFiltrees = listeOffres.stream().filter(offre -> {
				return offre.getMaxEleves() >= nbEleves && offre.getMinEleves() <= nbEleves ;
			}).collect(Collectors.toList()) ;
			return listeOffresFiltrees.size() == 0 ? 0L : listeOffresFiltrees.get(0).getMaxUtilisateurs() ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Offre> rechercher(Offre offre) throws Exception {
		try {
			Iterable<Offre> itOffres = offreRepository.findAll() ;
			List<Offre> listeOffres = new ArrayList() ;
			itOffres.forEach(listeOffres::add);
			return listeOffres ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	
	public List<Offre> rechercherTout (Offre offre) throws Exception {
		try {
			Iterable<Offre> itOffres = offreRepository.findAll() ;
			List<Offre> listeOffres = new ArrayList() ;
			itOffres.forEach(listeOffres::add);
			return listeOffres ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			throw ex ;
		}
	}
}
