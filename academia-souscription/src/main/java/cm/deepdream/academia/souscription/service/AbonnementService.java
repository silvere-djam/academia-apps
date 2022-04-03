package cm.deepdream.academia.souscription.service;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.deepdream.academia.souscription.repository.AbonnementRepository;
import cm.deepdream.academia.souscription.repository.OffreRepository;
import cm.deepdream.academia.souscription.transfert.AbonnementDTO;
import cm.deepdream.academia.souscription.transfert.EtablissementDTO;
import cm.deepdream.academia.souscription.exceptions.AbonnementDuplicationException;
import cm.deepdream.academia.souscription.exceptions.AbonnementNotFoundException;
import cm.deepdream.academia.souscription.exceptions.OffreNotFoundException;
import cm.deepdream.academia.souscription.model.Abonnement;
import cm.deepdream.academia.souscription.model.Etablissement;
import cm.deepdream.academia.souscription.model.Offre;

@Transactional
@Service
public class AbonnementService {
	private AbonnementRepository abonnementRepository ;
	private OffreRepository offreRepository ;
	
	
	public AbonnementService(AbonnementRepository abonnementRepository, OffreRepository offreRepository) {
		this.abonnementRepository = abonnementRepository;
		this.offreRepository = offreRepository;
	}


	public AbonnementDTO creer (AbonnementDTO abonnementDTO)   {
		Abonnement abonnementEntry = this.transformer(abonnementDTO) ;
		
		if(abonnementRepository.existsByEtablissementAndDateDebut(abonnementEntry.getEtablissement(),
				abonnementEntry.getDateDebut())) {
			throw new AbonnementDuplicationException(String.valueOf(abonnementDTO)) ;
		}
		
		Optional<Offre> offreOpt = offreRepository.findById(abonnementDTO.getIdOffre()) ;
		
		Offre offre = offreOpt.map(Function.identity())
				              .orElseThrow(() -> new OffreNotFoundException(String.valueOf(abonnementDTO.getIdOffre()))) ;
		
		abonnementEntry.setDateFin(abonnementEntry.getDateDebut()
				.plusMonths(abonnementDTO.getEvaluation() ? offre.getDureeEssai() :  abonnementDTO.getDuree())) ;
		//abonnementEntry.setDateCreation(LocalDateTime.now()) ;
		//abonnementEntry.setDateDernMaj(LocalDateTime.now()) ;
		abonnementEntry.setOffre(offre) ;
		Abonnement abonnementReturn = abonnementRepository.save(abonnementEntry) ;
		return this.transformer(abonnementReturn) ;
	}	
	
	
	public AbonnementDTO modifier (AbonnementDTO abonnementDTO) {
		Abonnement abonnementEntry = this.transformer(abonnementDTO) ;
		Abonnement abonnementExistant = abonnementRepository.findById(abonnementDTO.getId())
				                                         .map(Function.identity())
				                                         .orElseThrow(() -> new AbonnementNotFoundException(String.valueOf(abonnementDTO.getIdOffre()))) ;
		
		abonnementExistant.setStatut(abonnementEntry.getStatut()) ;
		
		//abonnementExistant.setDateDernMaj(LocalDateTime.now()) ;
		Abonnement abonnementReturn = abonnementRepository.save(abonnementEntry) ;
		return this.transformer(abonnementReturn) ;
	}
	
	
	public void supprimer (AbonnementDTO abonnementDTO) {
		abonnementRepository.findById(abonnementDTO.getId())
							.ifPresentOrElse(abonnementRepository::delete, 
										() -> {
											throw new AbonnementNotFoundException(String.valueOf(abonnementDTO.getId()));
										}); 
	}
	
	
	public void supprimer (Long id) {
		abonnementRepository.findById(id)
						    .ifPresentOrElse(abonnementRepository::delete, 
						    		() -> {
						    				throw new AbonnementNotFoundException(String.valueOf(id));
						    			}); 
	}
	
	
	public AbonnementDTO rechercher (Long id) {
		return abonnementRepository.findById(id)
				 				   .map(Function.identity())
				 				   .map(this::transformer)
				 				   .orElseThrow(()-> new AbonnementNotFoundException(String.valueOf(id))) ;
	}
	

	public List<AbonnementDTO> rechercher(EtablissementDTO etablissementDTO) {
		Etablissement etablissement = Etablissement.builder().id(etablissementDTO.getId()).build() ;
		return abonnementRepository.findByEtablissement(etablissement).stream()
				                   .map(Function.identity())
				                   .map(this::transformer)
				                   .collect(Collectors.toList());
	}
	
	
	public List<AbonnementDTO> rechercher(String statut) {
		return abonnementRepository.findByStatut(statut).stream()
				                   .map(Function.identity()) 
				                   .map(this::transformer)
				                   .collect(Collectors.toList()) ; 
	}
	
	
	public List<AbonnementDTO> rechercher(LocalDate dateDebut, LocalDate dateFin) {
		return abonnementRepository.findByDateDebutBetween(dateDebut, dateFin).stream()
				                   .map(Function.identity())
				                   .map(this::transformer)
				                   .collect(Collectors.toList()) ; 
	}
	
	
	public List<AbonnementDTO> rechercherTout () {
		Iterable<Abonnement> itAbonnements = abonnementRepository.findAll() ;
		List<AbonnementDTO> listeAbonnementsDTO = new ArrayList<>() ;
		itAbonnements.forEach(abonnement -> listeAbonnementsDTO.add(this.transformer(abonnement)));
		return listeAbonnementsDTO ;
	}
	
	
	private AbonnementDTO transformer (Abonnement abonnement) {
		Etablissement etablissement = abonnement.getEtablissement() ;
		Offre offre = abonnement.getOffre() ;
		return AbonnementDTO.builder().id(abonnement.getId()).dateDebut(abonnement.getDateDebut())
							.dateFin(abonnement.getDateFin()).duree(abonnement.getDuree())
							.evaluation(abonnement.getEvaluation())
							.idEtablissement(etablissement == null ? null : etablissement.getId())
							.libelleEtablissement(etablissement == null ? null : etablissement.getLibelle())
							.idOffre(offre == null ? null : offre.getId())
							.libelleOffre(offre == null ? null : offre.getLibelle())
							.nbEleves(abonnement.getNbEleves()).statut(abonnement.getStatut())
							.build() ;
	}
	
	
	private Abonnement transformer (AbonnementDTO abonnementDTO) {
		Etablissement etablissement = abonnementDTO.getIdEtablissement() == null ? null : 
			Etablissement.builder().id(abonnementDTO.getIdEtablissement()).libelle(abonnementDTO.getLibelleEtablissement()).build() ;
		Offre offre = abonnementDTO.getIdOffre() == null ? null : 
			Offre.builder().id(abonnementDTO.getIdOffre()).libelle(abonnementDTO.getLibelleOffre()).build() ;
		return Abonnement.builder()
							.id(abonnementDTO.getId()).dateDebut(abonnementDTO.getDateDebut())
							.dateFin(abonnementDTO.getDateFin()).duree(abonnementDTO.getDuree())
							.dateDebut(abonnementDTO.getDateDebut()).evaluation(abonnementDTO.getEvaluation())
							.nbEleves(abonnementDTO.getNbEleves()).statut(abonnementDTO.getStatut())
							.etablissement(etablissement).offre(offre)
							.build() ;
	}
}
