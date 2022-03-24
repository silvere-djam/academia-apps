package cm.deepdream.academia.souscription.service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.souscription.repository.OffreRepository;
import cm.deepdream.academia.souscription.exceptions.OffreNotFoundException;
import cm.deepdream.academia.souscription.model.Offre;

@Service
@Transactional
public class OffreService {
	private OffreRepository offreRepository ;
	
	public OffreService(OffreRepository offreRepository) {
		this.offreRepository = offreRepository;
	}

	
	public Offre creer (Offre offre) {
		return offreRepository.save(offre) ;
	}	
	
	
	public Offre modifier (Offre offre) {
		return offreRepository.save(offre) ;
	}
	
	
	public void supprimer (Offre offre)  {
		offreRepository.findById(offre.getId())
        .ifPresentOrElse(offreRepository::delete, 
     		   () -> {
     			   throw new OffreNotFoundException(String.format("%s", offre.getId())) ;
     		   }) ;
	}
	
	
	public void supprimer (Long id) {
		offreRepository.findById(id)
		               .ifPresentOrElse(offreRepository::delete, 
		            		   () -> {
		            			   throw new OffreNotFoundException(String.format("%s", id)) ;
		            		   }) ;
	}
	
	
	public Offre rechercher (Long id) {
		return offreRepository.findById(id)
				              .map(Function.identity())
				              .orElseThrow(() -> new OffreNotFoundException(String.format("%s", id))) ;
	}                         
	
	
	public BigDecimal rechercherCout (final Integer nbEleves) {
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
	}
	
	
	public List<Offre> rechercher(Offre offre) {
		Iterable<Offre> itOffres = offreRepository.findAll() ;
		List<Offre> listeOffres = new ArrayList() ;
		itOffres.forEach(listeOffres::add);
		return listeOffres ;
	}
	
	
	public List<Offre> rechercherTout (Offre offre) {
		Iterable<Offre> itOffres = offreRepository.findAll() ;
		List<Offre> listeOffres = new ArrayList() ;
		itOffres.forEach(listeOffres::add);
		return listeOffres ;
	}
}
