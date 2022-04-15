package cm.deepdream.academia.souscription.repository;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cm.deepdream.academia.souscription.model.Pays;
import lombok.extern.log4j.Log4j2;
@Log4j2
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class PaysRepositoryUnitTest {
	 @Autowired
	 private PaysRepository paysRepository ;
	 @Autowired
	 private TestEntityManager entityManager ;
	 
	 @Test
	 public void tester_creerPays_avecDonneesValides() {
		final String code = "CM" ;
		final String libelle = "Cameroun" ;
		final String codeTel = "237" ;
		final String monnaie = "CFA" ;
			
		Pays pays_Entry = Pays.builder().code(code).libelle(libelle).monnaie(monnaie)
					                    .codeTel(codeTel).build() ;
			
		Pays pays_Return = paysRepository.save(pays_Entry) ;
			
		assertThat(pays_Return.getId()).isNotNull();
		assertThat(pays_Return).hasFieldOrPropertyWithValue("code", code) ;
		assertThat(pays_Return).hasFieldOrPropertyWithValue("libelle", libelle) ;
		assertThat(pays_Return).hasFieldOrPropertyWithValue("codeTel", codeTel);
		assertThat(pays_Return).hasFieldOrPropertyWithValue("monnaie", monnaie);
	 }
	 
	 
	 @Test
	 @Transactional
	 public void tester_modifierPays_avecDonneesValides() {
		final String code = "CM" ;
		final String libelle = "Cameroun" ;
		final String codeTel = "237" ;
		final String monnaie = "XAF" ;
			
		Pays pays_Entry = Pays.builder().code("CI").libelle("Côte d'Ivoire").monnaie("XOF")
					.codeTel("230").build() ;
		
		Pays pays_Entry_0 = entityManager.persist(pays_Entry) ; 
		
		final Long id = pays_Entry_0.getId() ;
		
		pays_Entry_0.setCode(code) ;
		pays_Entry_0.setLibelle(libelle) ;
		pays_Entry_0.setCodeTel(codeTel) ;
		pays_Entry_0.setMonnaie(monnaie) ;
			
		Pays pays_Return = paysRepository.save(pays_Entry_0) ;
			
		assertThat(pays_Return).hasFieldOrPropertyWithValue("id", id) ;
		assertThat(pays_Return).hasFieldOrPropertyWithValue("code", code) ;
		assertThat(pays_Return).hasFieldOrPropertyWithValue("libelle", libelle) ;
		assertThat(pays_Return).hasFieldOrPropertyWithValue("codeTel", codeTel);
		assertThat(pays_Return).hasFieldOrPropertyWithValue("monnaie", monnaie);
	 }
	 
	 
	 @Test
	 public void tester_supprimerPays_avecDonneesValides() {
		final Long id = 1L ;
		final String code = "CM" ;
		final String libelle = "Cameroun" ;
		final String codeTel = "237" ;
		final String monnaie = "CFA" ;
			
		Pays pays_Entry = Pays.builder().id(id).code(code).libelle(libelle).monnaie(monnaie)
					.codeTel(codeTel).build() ;
		Assertions.assertDoesNotThrow(() -> paysRepository.delete(pays_Entry)) ;
	 }
	 
	 
	 @Test
	 @Transactional
	 public void tester_recherchererPays_avecDonneesValides() {
		final String code = "CM" ;
		final String libelle = "Cameroun" ;
		final String codeTel = "237" ;
		final String monnaie = "CFA" ;
			
		Pays pays_Entry = Pays.builder().code(code).libelle(libelle).monnaie(monnaie)
					                    .codeTel(codeTel).build() ;
		
		Pays pays_Return_0 = entityManager.persist(pays_Entry) ; 
		
		final Long id = pays_Return_0.getId() ;
	
		Pays pays_Return = paysRepository.findById(id).get() ;
		
		log.info("pays_Return="+pays_Return) ;
		assertThat(pays_Return).hasFieldOrPropertyWithValue("id", id) ;
		assertThat(pays_Return).hasFieldOrPropertyWithValue("code", code) ;
		assertThat(pays_Return).hasFieldOrPropertyWithValue("libelle", libelle) ;
		assertThat(pays_Return).hasFieldOrPropertyWithValue("codeTel", codeTel);
		assertThat(pays_Return).hasFieldOrPropertyWithValue("monnaie", monnaie);
	 }
	 
	 
	 @Test
	 @Transactional
	 public void tester_recherchererTousLesPays_avecDonneesValides() {
		Pays pays_Entry_1 = Pays.builder().code("CM").libelle("Cameroun").monnaie("237")
					                      .codeTel("XAF").build() ;
		Pays pays_Entry_2 = Pays.builder().code("CI").libelle("Côte d'Ivoire").monnaie("XOF")
				.codeTel("230").build() ;
		
		Pays pays_Return_1 = entityManager.persist(pays_Entry_1) ; 
		Pays pays_Return_2 = entityManager.persist(pays_Entry_2) ; 
		
		Iterable<Pays> itPays = paysRepository.findAll() ;
		List<Pays> listePays =  StreamSupport.stream(itPays.spliterator(), false)
				    					     .collect(Collectors.toList()) ;
		
		assertThat(listePays).contains(pays_Return_1, pays_Return_2) ;
	 }
}
