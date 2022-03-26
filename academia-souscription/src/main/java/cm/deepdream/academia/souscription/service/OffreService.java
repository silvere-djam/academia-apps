package cm.deepdream.academia.souscription.service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.deepdream.academia.souscription.repository.OffreRepository;
import cm.deepdream.academia.souscription.transfert.OffreDTO;
import cm.deepdream.academia.souscription.exceptions.OffreNotFoundException;
import cm.deepdream.academia.souscription.model.Offre;

@Service
@Transactional
public class OffreService {
	private OffreRepository offreRepository ;
	
	public OffreService(OffreRepository offreRepository) {
		this.offreRepository = offreRepository;
	}

	
	public OffreDTO creer (OffreDTO offreDTO) {
		Offre offreEntry = this.transformer(offreDTO) ;
		Offre offreReturn = offreRepository.save(offreEntry) ;
		return this.transformer(offreReturn) ;
	}	
	
	
	public OffreDTO modifier (OffreDTO offreDTO) {
		Offre offreEntry = this.transformer(offreDTO) ;
		Offre offreExistante = offreRepository.findById(offreDTO.getId())
											  .map(Function.identity())
											  .orElseThrow(() ->  new OffreNotFoundException(String.valueOf(offreDTO.getId()))) ;
		
		offreExistante.setDescription(offreEntry.getDescription()) ;
		offreExistante.setDureeEssai(offreEntry.getDureeEssai()) ;
		offreExistante.setLibelle(offreEntry.getLibelle()) ;
		offreExistante.setMaxEleves(offreEntry.getMaxEleves()) ;
		offreExistante.setMaxUtilisateurs(offreEntry.getMaxUtilisateurs()) ;
		offreExistante.setMinEleves(offreExistante.getMinEleves()) ;
		offreExistante.setMontantBase(offreEntry.getMontantBase()) ;
		offreExistante.setMontantMillier(offreEntry.getMontantMillier()) ;
		
		Offre offreReturn = offreRepository.save(offreExistante) ;
		return this.transformer(offreReturn) ;
	}
	
	
	public void supprimer (OffreDTO offreDTO)  {
		offreRepository.findById(offreDTO.getId())
        .ifPresentOrElse(offreRepository::delete, 
     		   () -> {
     			   throw new OffreNotFoundException(String.valueOf(offreDTO.getId())) ;
     		   }) ;
	}
	
	
	public void supprimer (Long id) {
		offreRepository.findById(id)
		               .ifPresentOrElse(offreRepository::delete, 
		            		   () -> {
		            			   throw new OffreNotFoundException(String.format("%s", id)) ;
		            		   }) ;
	}
	
	
	public OffreDTO rechercher (Long id) {
		return offreRepository.findById(id)
				              .map(Function.identity())
				              .map(this::transformer)
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

	
	public List<OffreDTO> rechercherTout () {
		Iterable<Offre> itOffres = offreRepository.findAll() ;
		List<OffreDTO> listeOffresDTO = new ArrayList() ;
		itOffres.forEach(offre -> listeOffresDTO.add(this.transformer(offre))) ;
		return listeOffresDTO ;
	}
	
	
	private OffreDTO transformer (Offre offre) {
		return OffreDTO.builder().id(offre.getId()).libelle(offre.getLibelle()).description(offre.getDescription())
					   .dureeEssai(offre.getDureeEssai()).maxEleves(offre.getMaxEleves()).maxUtilisateurs(offre.getMaxUtilisateurs())
					   .minEleves(offre.getMinEleves()).montantBase(offre.getMontantMillier()).montantMillier(offre.getMontantMillier())
					   .build() ;
	}
	
	
	private Offre transformer (OffreDTO offreDTO) {
		return   Offre.builder().id(offreDTO.getId()).libelle(offreDTO.getLibelle()).description(offreDTO.getDescription())
					   .dureeEssai(offreDTO.getDureeEssai()).maxEleves(offreDTO.getMaxEleves()).maxUtilisateurs(offreDTO.getMaxUtilisateurs())
					   .minEleves(offreDTO.getMinEleves()).montantBase(offreDTO.getMontantMillier()).montantMillier(offreDTO.getMontantMillier())
					   .build() ;
	}
}
