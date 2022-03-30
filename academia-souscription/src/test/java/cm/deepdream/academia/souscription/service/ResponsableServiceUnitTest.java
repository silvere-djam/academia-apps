package cm.deepdream.academia.souscription.service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cm.deepdream.academia.souscription.exceptions.ResponsableDuplicationException;
import cm.deepdream.academia.souscription.exceptions.ResponsableNotFoundException;
import cm.deepdream.academia.souscription.model.Etablissement;
import cm.deepdream.academia.souscription.model.Responsable;
import cm.deepdream.academia.souscription.repository.ResponsableRepository;
import cm.deepdream.academia.souscription.transfert.ResponsableDTO;
import static org.mockito.Mockito.* ;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Random ;

@ExtendWith(MockitoExtension.class)
public class ResponsableServiceUnitTest {
	@Mock
	private ResponsableRepository responsableRepository  ;
	@InjectMocks
	private ResponsableService responsableService ;
	
	@Test
	public void tester_creerResponsable_avecDonneesValides() {
		final String matricule = "201001" ;
		final String nom = "Baliaba" ;
		final String prenom = "Gatien" ;
		final String sexe = "Homme" ;
		final LocalDate dateNaissance = LocalDate.of(1990, Month.SEPTEMBER, 25) ;
		final String lieuNaissance = "Lable" ;
		final LocalDate datePriseService = LocalDate.of(2010, Month.APRIL, 10) ;
		final String telephone = "+237698037324" ;
		final String email = "silvere.djam@gmail.com" ;
		final Long idEtablissement = 1000000L ;
		final String libelleEtablissement = "Lycée Général Leclerc" ;
		
		Responsable responsable_Entry = Responsable.builder().matricule(matricule)
				.nom(nom).prenom(prenom).sexe(sexe).dateNaissance(dateNaissance).lieuNaissance(lieuNaissance)
				.datePriseService(datePriseService).telephone(telephone).email(email)
				.etablissement(Etablissement.builder().id(idEtablissement).libelle(libelleEtablissement).build())
				.build() ; 
		
		Responsable responsable_Return = Responsable.builder().id(new Random().nextLong()).matricule(matricule)
				.nom(nom).prenom(prenom).sexe(sexe).dateNaissance(dateNaissance).lieuNaissance(lieuNaissance)
				.datePriseService(datePriseService).telephone(telephone).email(email)
				.etablissement(Etablissement.builder().id(idEtablissement).libelle(libelleEtablissement).build())
				.build() ; 
		
		ResponsableDTO responsableDTO_Entry = ResponsableDTO.builder().matricule(matricule)
				.nom(nom).prenom(prenom).sexe(sexe).dateNaissance(dateNaissance).lieuNaissance(lieuNaissance)
				.datePriseService(datePriseService).telephone(telephone).email(email)
				.idEtablissement(idEtablissement).libelleEtablissement(libelleEtablissement).build() ; 
		
		when(responsableRepository.existsByMatricule(matricule)).thenReturn(false) ;
		when(responsableRepository.save(responsable_Entry)).thenReturn(responsable_Return) ;

		ResponsableDTO responsableDTO_Return = responsableService.creer(responsableDTO_Entry) ;
		
		Assertions.assertNotNull(responsableDTO_Return.getId()) ;
		
		Assertions.assertTrue(matricule.equals(responsableDTO_Return.getMatricule())
				&& nom.equals(responsableDTO_Return.getNom())
				&& prenom.equals(responsableDTO_Return.getPrenom())
				&& sexe.equals(responsableDTO_Return.getSexe())
				&& dateNaissance.equals(responsableDTO_Return.getDateNaissance())
				&& lieuNaissance.equals(responsableDTO_Return.getLieuNaissance())
				&& datePriseService.equals(responsableDTO_Return.getDatePriseService())
				&& telephone.equals(responsableDTO_Return.getTelephone())
				&& email.equals(responsableDTO_Return.getEmail())
				&& idEtablissement.equals(responsableDTO_Return.getIdEtablissement())
				&& libelleEtablissement.equals(responsableDTO_Return.getLibelleEtablissement())) ;
		
		verify(responsableRepository, times(1)).existsByMatricule(matricule) ;
		verify(responsableRepository, times(1)).save(responsable_Entry) ;
	}
	
	
	
	
	@Test
	public void tester_creerResponsable_avecDoublon() {
		final String matricule = "201001" ;
		final String nom = "Baliaba" ;
		final String prenom = "Gatien" ;
		final String sexe = "Homme" ;
		final LocalDate dateNaissance = LocalDate.of(1990, Month.SEPTEMBER, 25) ;
		final String lieuNaissance = "Lable" ;
		final LocalDate datePriseService = LocalDate.of(2010, Month.APRIL, 10) ;
		final String telephone = "+237698037324" ;
		final String email = "silvere.djam@gmail.com" ;
		final Long idEtablissement = 1000000L ;
		final String libelleEtablissement = "Lycée Général Leclerc" ;
		
		ResponsableDTO responsableDTO_Entry = ResponsableDTO.builder().matricule(matricule)
				.nom(nom).prenom(prenom).sexe(sexe).dateNaissance(dateNaissance).lieuNaissance(lieuNaissance)
				.datePriseService(datePriseService).telephone(telephone).email(email)
				.idEtablissement(idEtablissement).libelleEtablissement(libelleEtablissement).build() ; 
		
		when(responsableRepository.existsByMatricule(matricule)).thenReturn(true) ;

		Assertions.assertThrows(ResponsableDuplicationException.class, 
				()-> responsableService.creer(responsableDTO_Entry)) ;
		
		verify(responsableRepository, times(1)).existsByMatricule(matricule) ;
	}
	
	
	
	@Test
	public void tester_modifierResponsable_avecDonneesValides() {
		final Long id = 1000000L ;
		final String matricule = "201001" ;
		final String nom = "Baliaba" ;
		final String prenom = "Gatien" ;
		final String sexe = "Homme" ;
		final LocalDate dateNaissance = LocalDate.of(1990, Month.SEPTEMBER, 25) ;
		final String lieuNaissance = "Lable" ;
		final LocalDate datePriseService = LocalDate.of(2010, Month.APRIL, 10) ;
		final String telephone = "+237698037324" ;
		final String email = "silvere.djam@gmail.com" ;
		final Long idEtablissement = 1000000L ;
		final String libelleEtablissement = "Lycée Général Leclerc" ;
		
		Responsable responsable_Entry = Responsable.builder().id(id).matricule(matricule)
				.nom(nom).prenom(prenom).sexe(sexe).dateNaissance(dateNaissance).lieuNaissance(lieuNaissance)
				.datePriseService(datePriseService).telephone(telephone).email(email)
				.etablissement(Etablissement.builder().id(idEtablissement).libelle(libelleEtablissement).build())
				.build() ; 
		
		ResponsableDTO responsableDTO_Entry = ResponsableDTO.builder().id(id).matricule(matricule)
				.nom(nom).prenom(prenom).sexe(sexe).dateNaissance(dateNaissance).lieuNaissance(lieuNaissance)
				.datePriseService(datePriseService).telephone(telephone).email(email)
				.idEtablissement(idEtablissement).libelleEtablissement(libelleEtablissement).build() ; 
		
		when(responsableRepository.findById(id)).thenReturn(Optional.of(responsable_Entry)) ;
		when(responsableRepository.save(responsable_Entry)).thenReturn(responsable_Entry) ;

		ResponsableDTO responsableDTO_Return = responsableService.modifier(responsableDTO_Entry) ;

		Assertions.assertTrue(id.equals(responsableDTO_Return.getId())
				&& matricule.equals(responsableDTO_Return.getMatricule())
				&& nom.equals(responsableDTO_Return.getNom())
				&& prenom.equals(responsableDTO_Return.getPrenom())
				&& sexe.equals(responsableDTO_Return.getSexe())
				&& dateNaissance.equals(responsableDTO_Return.getDateNaissance())
				&& lieuNaissance.equals(responsableDTO_Return.getLieuNaissance())
				&& datePriseService.equals(responsableDTO_Return.getDatePriseService())
				&& telephone.equals(responsableDTO_Return.getTelephone())
				&& email.equals(responsableDTO_Return.getEmail())
				&& idEtablissement.equals(responsableDTO_Return.getIdEtablissement())
				&& libelleEtablissement.equals(responsableDTO_Return.getLibelleEtablissement())) ;
		
		verify(responsableRepository, times(1)).findById(id) ;
		verify(responsableRepository, times(1)).save(responsable_Entry) ;
	}
	
	
	
	@Test
	public void tester_modifierResponsable_avecDonneesInexistantes() {
		final Long id = 1000000L ;
		final String matricule = "201001" ;
		final String nom = "Baliaba" ;
		final String prenom = "Gatien" ;
		final String sexe = "Homme" ;
		final LocalDate dateNaissance = LocalDate.of(1990, Month.SEPTEMBER, 25) ;
		final String lieuNaissance = "Lable" ;
		final LocalDate datePriseService = LocalDate.of(2010, Month.APRIL, 10) ;
		final String telephone = "+237698037324" ;
		final String email = "silvere.djam@gmail.com" ;
		final Long idEtablissement = 1000000L ;
		final String libelleEtablissement = "Lycée Général Leclerc" ;

		ResponsableDTO responsableDTO_Entry = ResponsableDTO.builder().id(id).matricule(matricule)
				.nom(nom).prenom(prenom).sexe(sexe).dateNaissance(dateNaissance).lieuNaissance(lieuNaissance)
				.datePriseService(datePriseService).telephone(telephone).email(email)
				.idEtablissement(idEtablissement).libelleEtablissement(libelleEtablissement).build() ; 
		
		when(responsableRepository.findById(id)).thenReturn(Optional.empty()) ;

		Assertions.assertThrows(ResponsableNotFoundException.class, 
				() -> responsableService.modifier(responsableDTO_Entry)) ;
		
		verify(responsableRepository, times(1)).findById(id) ;
	}
	
	
	
	@Test
	public void tester_supprimerResponsable_avecDonneesValides() {
		final Long id = 1000000L ;
		final String matricule = "201001" ;
		final String nom = "Baliaba" ;
		final String prenom = "Gatien" ;
		final String sexe = "Homme" ;
		final LocalDate dateNaissance = LocalDate.of(1990, Month.SEPTEMBER, 25) ;
		final String lieuNaissance = "Lable" ;
		final LocalDate datePriseService = LocalDate.of(2010, Month.APRIL, 10) ;
		final String telephone = "+237698037324" ;
		final String email = "silvere.djam@gmail.com" ;
		final Long idEtablissement = 1000000L ;
		final String libelleEtablissement = "Lycée Général Leclerc" ;
		
		Responsable responsable_Entry = Responsable.builder().id(id).matricule(matricule)
				.nom(nom).prenom(prenom).sexe(sexe).dateNaissance(dateNaissance).lieuNaissance(lieuNaissance)
				.datePriseService(datePriseService).telephone(telephone).email(email)
				.etablissement(Etablissement.builder().id(idEtablissement).libelle(libelleEtablissement).build())
				.build() ; 
		
		ResponsableDTO responsableDTO_Entry = ResponsableDTO.builder().id(id).matricule(matricule)
				.nom(nom).prenom(prenom).sexe(sexe).dateNaissance(dateNaissance).lieuNaissance(lieuNaissance)
				.datePriseService(datePriseService).telephone(telephone).email(email)
				.idEtablissement(idEtablissement).libelleEtablissement(libelleEtablissement).build() ; 
		
		when(responsableRepository.findById(id)).thenReturn(Optional.of(responsable_Entry)) ;
		doNothing().when(responsableRepository).delete(responsable_Entry) ;

		Assertions.assertDoesNotThrow(() -> responsableService.supprimer(responsableDTO_Entry)) ;
		
		verify(responsableRepository, times(1)).findById(id) ;
		verify(responsableRepository, times(1)).delete(responsable_Entry) ;
	}
	
	
	
	@Test
	public void tester_supprimerResponsable_avecDonneesInvalides() {
		final Long id = 1000000L ;
		final String matricule = "201001" ;
		final String nom = "Baliaba" ;
		final String prenom = "Gatien" ;
		final String sexe = "Homme" ;
		final LocalDate dateNaissance = LocalDate.of(1990, Month.SEPTEMBER, 25) ;
		final String lieuNaissance = "Lable" ;
		final LocalDate datePriseService = LocalDate.of(2010, Month.APRIL, 10) ;
		final String telephone = "+237698037324" ;
		final String email = "silvere.djam@gmail.com" ;
		final Long idEtablissement = 1000000L ;
		final String libelleEtablissement = "Lycée Général Leclerc" ;

		ResponsableDTO responsableDTO_Entry = ResponsableDTO.builder().id(id).matricule(matricule)
				.nom(nom).prenom(prenom).sexe(sexe).dateNaissance(dateNaissance).lieuNaissance(lieuNaissance)
				.datePriseService(datePriseService).telephone(telephone).email(email)
				.idEtablissement(idEtablissement).libelleEtablissement(libelleEtablissement).build() ; 
		
		when(responsableRepository.findById(id)).thenReturn(Optional.empty()) ;

		Assertions.assertThrows(ResponsableNotFoundException.class, 
				() -> responsableService.supprimer(responsableDTO_Entry)) ;
		
		verify(responsableRepository, times(1)).findById(id) ;
	}
	
	

	@Test
	public void tester_rechercherResponsable_avecDonneesValides() {
		final Long id = 1000000L ;
		final String matricule = "201001" ;
		final String nom = "Baliaba" ;
		final String prenom = "Gatien" ;
		final String sexe = "Homme" ;
		final LocalDate dateNaissance = LocalDate.of(1990, Month.SEPTEMBER, 25) ;
		final String lieuNaissance = "Lable" ;
		final LocalDate datePriseService = LocalDate.of(2010, Month.APRIL, 10) ;
		final String telephone = "+237698037324" ;
		final String email = "silvere.djam@gmail.com" ;
		final Long idEtablissement = 1000000L ;
		final String libelleEtablissement = "Lycée Général Leclerc" ;
		
		Responsable responsable = Responsable.builder().id(id).matricule(matricule)
				.nom(nom).prenom(prenom).sexe(sexe).dateNaissance(dateNaissance).lieuNaissance(lieuNaissance)
				.datePriseService(datePriseService).telephone(telephone).email(email)
				.etablissement(Etablissement.builder().id(idEtablissement).libelle(libelleEtablissement).build())
				.build() ; 

		when(responsableRepository.findById(id)).thenReturn(Optional.of(responsable)) ;

		ResponsableDTO responsableDTO_Return = responsableService.rechercher(id) ;

		Assertions.assertTrue(id.equals(responsableDTO_Return.getId())
				&& matricule.equals(responsableDTO_Return.getMatricule())
				&& nom.equals(responsableDTO_Return.getNom())
				&& prenom.equals(responsableDTO_Return.getPrenom())
				&& sexe.equals(responsableDTO_Return.getSexe())
				&& dateNaissance.equals(responsableDTO_Return.getDateNaissance())
				&& lieuNaissance.equals(responsableDTO_Return.getLieuNaissance())
				&& datePriseService.equals(responsableDTO_Return.getDatePriseService())
				&& telephone.equals(responsableDTO_Return.getTelephone())
				&& email.equals(responsableDTO_Return.getEmail())
				&& idEtablissement.equals(responsableDTO_Return.getIdEtablissement())
				&& libelleEtablissement.equals(responsableDTO_Return.getLibelleEtablissement())) ;
		
		verify(responsableRepository, times(1)).findById(id) ;
	}
	
	
	@Test
	public void tester_rechercherResponsables_avecDonneesValides() {
		final Long id = 1000000L ;
		final String matricule = "201001" ;
		final String nom = "Baliaba" ;
		final String prenom = "Gatien" ;
		final String sexe = "Homme" ;
		final LocalDate dateNaissance = LocalDate.of(1990, Month.SEPTEMBER, 25) ;
		final String lieuNaissance = "Lable" ;
		final LocalDate datePriseService = LocalDate.of(2010, Month.APRIL, 10) ;
		final String telephone = "+237698037324" ;
		final String email = "silvere.djam@gmail.com" ;
		final Long idEtablissement = 1000000L ;
		final String libelleEtablissement = "Lycée Général Leclerc" ;
		
		Responsable responsable = Responsable.builder().id(id).matricule(matricule)
				.nom(nom).prenom(prenom).sexe(sexe).dateNaissance(dateNaissance).lieuNaissance(lieuNaissance)
				.datePriseService(datePriseService).telephone(telephone).email(email)
				.etablissement(Etablissement.builder().id(idEtablissement).libelle(libelleEtablissement).build())
				.build() ; 

		when(responsableRepository.findAll()).thenReturn(List.of(responsable)) ;

		List<ResponsableDTO> listeResponsablesDTO = responsableService.rechercherTout() ;

		Assertions.assertTrue(listeResponsablesDTO.size() == 1) ;
		
		verify(responsableRepository, times(1)).findAll() ;
	}
	

}
