package cm.deepdream.academia.souscription.service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cm.deepdream.academia.souscription.model.Etablissement;
import cm.deepdream.academia.souscription.model.Responsable;
import cm.deepdream.academia.souscription.repository.ResponsableRepository;
import cm.deepdream.academia.souscription.transfert.ResponsableDTO;
import static org.mockito.Mockito.* ;

import java.time.LocalDate;
import java.time.Month;
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
	
	
	
	/*@Test
	public void tester_modifierPays_avecDonneesValides() {
		final Long id = 100000L ;
		final String code = "CM" ;
		final String libelle = "Cameroun" ;
		final String codeTel = "237" ;
		final String monnaie = "CFA" ;
		
		Pays pays_Entry = Pays.builder().id(id).code(code).libelle(libelle).monnaie(monnaie)
				.codeTel(codeTel).build() ;
		
		Pays pays_Return = Pays.builder().id(id).code(code).libelle(libelle).monnaie(monnaie)
				.codeTel(codeTel).build() ;
		
		PaysDTO paysDTO_Entry = PaysDTO.builder().id(id).code(code).libelle(libelle).monnaie(monnaie)
				.codeTel(codeTel).build() ;
		
		when(paysRepository.findById(id)).thenReturn(Optional.of(pays_Return)) ;
		when(paysRepository.save(pays_Entry)).thenReturn(pays_Return) ;
		
		PaysService paysService = new PaysService(paysRepository) ;
		
		PaysDTO paysDTO_Return = paysService.modifier(paysDTO_Entry) ;
		
		Assert.assertTrue(id == paysDTO_Return.getId()
				&& code.equals(paysDTO_Return.getCode())
				&& libelle.equals(paysDTO_Return.getLibelle())
				&& codeTel.equals(paysDTO_Return.getCodeTel())
				&& monnaie.equals(paysDTO_Return.getMonnaie())) ;
	}
	
	
	@Test
	public void tester_supprimerPays_avecDonneesValides() {
		final Long id = 100000L ;
		final String code = "CM" ;
		final String libelle = "Cameroun" ;
		final String codeTel = "237" ;
		final String monnaie = "CFA" ;

		Pays pays_Return = Pays.builder().id(id).code(code).libelle(libelle).monnaie(monnaie)
				.codeTel(codeTel).build() ;
		
		PaysDTO paysDTO_Entry = PaysDTO.builder().id(id).code(code).libelle(libelle).monnaie(monnaie)
				.codeTel(codeTel).build() ;
		
		when(paysRepository.findById(id)).thenReturn(Optional.of(pays_Return)) ;
		doNothing().when(paysRepository).delete(pays_Return) ;
		
		PaysService paysService = new PaysService(paysRepository) ;
		
		try {
			paysService.supprimer(paysDTO_Entry) ;
			Assert.assertTrue(true) ;
		}catch(Exception e) {
			Assert.fail() ;
		}
	}
	
	
	@Test
	public void tester_rechercherPays_avecDonneesValides() {
		final Long id = 100000L ;
		final String code = "CM" ;
		final String libelle = "Cameroun" ;
		final String codeTel = "237" ;
		final String monnaie = "CFA" ;

		Pays pays_Return = Pays.builder().id(id).code(code).libelle(libelle).monnaie(monnaie)
				.codeTel(codeTel).build() ;
		
		when(paysRepository.findById(id)).thenReturn(Optional.of(pays_Return)) ;
		
		PaysService paysService = new PaysService(paysRepository) ;
		
		PaysDTO paysDTO_Return = paysService.rechercher(id) ;
		
		Assert.assertTrue(id == paysDTO_Return.getId()
				&& code.equals(paysDTO_Return.getCode())
				&& libelle.equals(paysDTO_Return.getLibelle())
				&& codeTel.equals(paysDTO_Return.getCodeTel())
				&& monnaie.equals(paysDTO_Return.getMonnaie())) ;
	}
	
	
	
	@Test
	public void tester_rechercherPlusieursPays_avecDonneesValides() {
		Pays pays_Return_1 = Pays.builder().id(100000L).code("CM").libelle("Cameroun")
				.monnaie("CFA").codeTel("237").build() ;
		
		Pays pays_Return_2 = Pays.builder().id(100001L).code("CM").libelle("Cameroun")
				.monnaie("CFA").codeTel("237").build() ;

		when(paysRepository.findAll()).thenReturn(List.of(pays_Return_1, pays_Return_2)) ;
		
		PaysService paysService = new PaysService(paysRepository) ;
		List<PaysDTO> listePaysDTO = paysService.rechercherTout() ;
		
		Assert.assertThat(listePaysDTO, hasSize(2));
	}*/

}
