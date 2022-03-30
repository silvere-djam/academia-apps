package cm.deepdream.academia.souscription.webservice;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import cm.deepdream.academia.souscription.transfert.ResponsableDTO;
import lombok.extern.log4j.Log4j2;
@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Log4j2
public class ResponsableWebServiceIntegrationTest {
	@Autowired
	private TestRestTemplate restTemplate ;

	@Test
	@Sql(scripts = "classpath:./scripts/responsable/init_data_creerResponsable.sql")
	public void tester_creerResponsable_avecDonneesValides() throws Exception {
		final String matricule = "201001" ;
		final String nom = "Baliaba" ;
		final String prenom = "Gatien" ;
		final String sexe = "Homme" ;
		final LocalDate dateNaissance = LocalDate.of(1990, Month.SEPTEMBER, 25) ;
		final String lieuNaissance = "Lable" ;
		final LocalDate datePriseService = LocalDate.of(2010, Month.APRIL, 10) ;
		final String telephone = "+237698037324" ;
		final String email = "silvere.djam@gmail.com" ;
		final Long idEtablissement = 1000000000L ;
		final String libelleEtablissement = "Lycée Général Leclerc" ;

		ResponsableDTO responsableDTO_Entry = ResponsableDTO.builder().matricule(matricule)
				.nom(nom).prenom(prenom).sexe(sexe).dateNaissance(dateNaissance).lieuNaissance(lieuNaissance)
				.datePriseService(datePriseService).telephone(telephone).email(email)
				.idEtablissement(idEtablissement).libelleEtablissement(libelleEtablissement)
				.build() ; 
		
		ResponseEntity<ResponsableDTO> response = restTemplate.postForEntity("/ws/responsable/ajout", responsableDTO_Entry, 
				ResponsableDTO.class) ;
		ResponsableDTO responsableDTO_Return = response.getBody() ;
		
		log.info("tester_creerResponsable_avecDonneesValides.responsableDTO_Entry = "+responsableDTO_Return) ;

		Assertions.assertTrue(response.getStatusCode() == HttpStatus.OK && responsableDTO_Return.getId() != null
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
	}
	
	
	@Test
	@Sql(scripts = "classpath:./scripts/responsable/init_data_modifierResponsable.sql")
	public void tester_modifierResponsable_avecDonneesValides() throws Exception {
		final Long id = 1000000000L ;
		final String matricule = "201701" ;
		final String nom = "Djamilaa" ;
		final String prenom = "Silverine" ;
		final String sexe = "Femme" ;
		final LocalDate dateNaissance = LocalDate.of(1995, Month.SEPTEMBER, 10) ;
		final String lieuNaissance = "Lable" ;
		final LocalDate datePriseService = LocalDate.of(2017, Month.APRIL, 10) ;
		final String telephone = "+237698037323" ;
		final String email = "silverine.djamilaa@gmail.com" ;

		ResponsableDTO responsableDTO_Entry = ResponsableDTO.builder().id(id).matricule(matricule)
				.nom(nom).prenom(prenom).sexe(sexe)
				.dateNaissance(dateNaissance).lieuNaissance(lieuNaissance)
				.datePriseService(datePriseService).telephone(telephone)
				.email(email).build() ; 
		
		ResponseEntity<ResponsableDTO> response = restTemplate.exchange("/ws/responsable/modification", HttpMethod.PUT, 
				new HttpEntity<ResponsableDTO>(responsableDTO_Entry), ResponsableDTO.class) ;
		ResponsableDTO responsableDTO_Return = response.getBody() ;
		
		log.info("tester_modifierResponsable_avecDonneesValides.responsableDTO_Entry = "+responsableDTO_Return+ ", Status Code="+response.getStatusCode()) ;

		Assertions.assertTrue(response.getStatusCode() == HttpStatus.OK && id.equals(responsableDTO_Return.getId()) 
				&& matricule.equals(responsableDTO_Return.getMatricule())
				&& nom.equals(responsableDTO_Return.getNom())
				&& prenom.equals(responsableDTO_Return.getPrenom())
				&& sexe.equals(responsableDTO_Return.getSexe())
				&& dateNaissance.equals(responsableDTO_Return.getDateNaissance())
				&& lieuNaissance.equals(responsableDTO_Return.getLieuNaissance())
				&& datePriseService.equals(responsableDTO_Return.getDatePriseService())
				&& telephone.equals(responsableDTO_Return.getTelephone())
				&& email.equals(responsableDTO_Return.getEmail())) ;
	}
	
	
	@Test
	@Sql(scripts = "classpath:./scripts/responsable/init_data_supprimerResponsable.sql")
	public void tester_supprimerResponsable_avecDonneesValides() throws Exception {
		final Long id = 1000000001L ;

		ResponsableDTO responsableDTO_Entry = ResponsableDTO.builder().id(id).build() ;
		
		ResponseEntity<ResponsableDTO> response = restTemplate.exchange("/ws/responsable/suppression", HttpMethod.DELETE, 
				new HttpEntity<ResponsableDTO>(responsableDTO_Entry), ResponsableDTO.class) ;

		Assertions.assertTrue(response.getStatusCode() == HttpStatus.OK) ;
	}
	
	
	@Test
	@Sql(scripts = "classpath:./scripts/responsable/init_data_rechercherResponsable.sql")
	public void tester_rechercherResponsable_avecDonneesValides() throws Exception {
		final Long id = 1000000002L ;
		final String matricule = "202201" ;
		final String nom = "Molemb" ;
		final String prenom = "Cyrille" ;
		final String sexe = "Homme" ;
		final LocalDate dateNaissance = LocalDate.of(1990, Month.SEPTEMBER, 25) ;
		final String lieuNaissance = "Douala" ;
		final LocalDate datePriseService = LocalDate.of(2022, Month.APRIL, 10) ;
		final String telephone = "+237698037324" ;
		final String email = "cyrille.molemb@gmail.com" ;

		ResponseEntity<ResponsableDTO> response = restTemplate.getForEntity("/ws/responsable/id/{id}",  ResponsableDTO.class, id) ;
		ResponsableDTO responsableDTO_Return = response.getBody() ;
		

		log.info("tester_rechercherResponsable_avecDonneesValides.responsableDTO_Entry = "+responsableDTO_Return+ ", Status Code="+response.getStatusCode()) ;
		
		Assertions.assertTrue(response.getStatusCode() == HttpStatus.OK && id.equals(responsableDTO_Return.getId()) 
				&& matricule.equals(responsableDTO_Return.getMatricule())
				&& nom.equals(responsableDTO_Return.getNom())
				&& prenom.equals(responsableDTO_Return.getPrenom())
				&& sexe.equals(responsableDTO_Return.getSexe())
				&& dateNaissance.equals(responsableDTO_Return.getDateNaissance())
				&& lieuNaissance.equals(responsableDTO_Return.getLieuNaissance())
				&& datePriseService.equals(responsableDTO_Return.getDatePriseService())
				&& telephone.equals(responsableDTO_Return.getTelephone())
				&& email.equals(responsableDTO_Return.getEmail())) ;
	}
	
	
	@Test
	@Sql(scripts = "classpath:./scripts/responsable/init_data_rechercherPlusieursResponsables.sql")
	public void tester_rechercherPlusieursResponsables_avecDonneesValides() throws Exception {
		ResponseEntity<ResponsableDTO[]> response = restTemplate.getForEntity("/ws/responsable/all",  ResponsableDTO[].class) ;
		List<ResponsableDTO> listeResponsablesDTO = Arrays.asList(response.getBody());
		
		Assertions.assertTrue(listeResponsablesDTO.size() > 0) ;
	}

}
