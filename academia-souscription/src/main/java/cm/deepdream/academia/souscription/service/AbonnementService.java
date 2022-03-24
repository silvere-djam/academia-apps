package cm.deepdream.academia.souscription.service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.deepdream.academia.souscription.repository.AbonnementRepository;
import cm.deepdream.academia.souscription.repository.EtablissementRepository;
import cm.deepdream.academia.souscription.repository.OffreRepository;
import cm.deepdream.academia.souscription.enums.StatutA;
import cm.deepdream.academia.souscription.exceptions.AbonnementNotFoundException;
import cm.deepdream.academia.souscription.model.Abonnement;
import cm.deepdream.academia.souscription.model.Etablissement;
import cm.deepdream.academia.souscription.model.Offre;
@Transactional
@Service
public class AbonnementService {
	private AbonnementRepository abonnementRepository ;
	private EtablissementRepository etablissementRepository ;
	private OffreRepository offreRepository ;
	
	
	public AbonnementService(AbonnementRepository abonnementRepository, EtablissementRepository etablissementRepository,
			OffreRepository offreRepository) {
		this.abonnementRepository = abonnementRepository;
		this.etablissementRepository = etablissementRepository;
		this.offreRepository = offreRepository;
	}


	public Abonnement creer (Abonnement abonnement)   {
		Optional<Offre> offreOpt = offreRepository.findById(abonnement.getOffre().getId()) ;
		abonnement.setDateDebut(LocalDate.now()) ;
		abonnement.setDateFin(abonnement.getDateDebut().plusMonths(offreOpt.get().getDureeEssai())) ;//???????
		abonnement.setDateCreation(LocalDateTime.now()) ;
		abonnement.setDateDernMaj(LocalDateTime.now()) ;
		return abonnementRepository.save(abonnement) ;
	}	
	
	
	
	public Abonnement creer (Etablissement etablissement) {
		Etablissement etablissementCree = etablissementRepository.save(etablissement) ;
		
		Abonnement abonnement = new Abonnement() ;
		
		abonnement.setEtablissement(etablissementCree);
		abonnement.setNbEleves(etablissement.getNbElevesApprox());
		
		List<Offre> listeOffres = offreRepository.findByMinElevesLessThanEqualAndMaxElevesGreaterThanEqualOrderByMinElevesAsc(abonnement.getNbEleves(), 
				abonnement.getNbEleves()) ;
		
		if(listeOffres.size() == 0) {
			
		}
		
		Offre offre = listeOffres.get(0) ;
		
		abonnement.setEtablissement(etablissementCree);
		abonnement.setOffre(offre);
		abonnement.setDuree(offre.getDureeEssai());
		abonnement.setDateDebut(LocalDate.now()) ;
		abonnement.setDateFin(abonnement.getDateDebut().plusDays(offre.getDureeEssai())) ;
		abonnement.setDateCreation(LocalDateTime.now()) ;
		abonnement.setDateDernMaj(LocalDateTime.now()) ;
		abonnement.setEvaluation(true);
		abonnement.setStatut(StatutA.En_Cours.name());
		abonnement.setNbEleves(etablissement.getNbElevesApprox());
		return abonnementRepository.save(abonnement) ;
	}	
	
	
	public Abonnement modifier (Abonnement abonnement) {
		abonnement.setDateDernMaj(LocalDateTime.now()) ;
		abonnementRepository.save(abonnement) ;
		return abonnement ;
	}
	
	
	public void supprimer (Abonnement abonnement) {
		abonnementRepository.delete(abonnement) ;
	}
	
	
	public void supprimer (Long id) {
		abonnementRepository.findById(id)
						    .ifPresentOrElse(abonnementRepository::delete, 
						    		() -> {
						    				throw new AbonnementNotFoundException(String.format("%s", id));
						    			}); 
	}
	
	
	public Abonnement rechercher (Long id) throws Exception {
		return abonnementRepository.findById(id)
				 				   .map(Function.identity())
				 				   .orElseThrow(()-> new AbonnementNotFoundException(String.format("%s", id))) ;
	}
	
	
	public List<Abonnement> rechercher(Abonnement abonnement) throws Exception {
		Iterable<Abonnement> itAbonnements = abonnementRepository.findAll() ;
		List<Abonnement> listeAbonnements = new ArrayList<>() ;
		itAbonnements.forEach(listeAbonnements::add);
		return listeAbonnements ;
	}
	
	
	public List<Abonnement> rechercher(Etablissement etablissement) {
		return abonnementRepository.findByEtablissement(etablissement) ;
	}
	
	
	public List<Abonnement> rechercher(String statut) {
		return abonnementRepository.findByStatut(statut) ; 
	}
	
	
	public List<Abonnement> rechercher(LocalDate dateDebut, LocalDate dateFin) {
		return abonnementRepository.findByDateDebutBetween(dateDebut, dateFin) ; 
	}
	
	
	public List<Abonnement> rechercherTout (Abonnement abonnement) {
		Iterable<Abonnement> itAbonnements = abonnementRepository.findAll() ;
		List<Abonnement> listeAbonnements = new ArrayList<>() ;
		itAbonnements.forEach(listeAbonnements::add);
		return listeAbonnements ;
	}
}
