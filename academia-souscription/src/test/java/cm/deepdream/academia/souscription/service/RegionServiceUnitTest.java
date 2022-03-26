package cm.deepdream.academia.souscription.service;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import cm.deepdream.academia.souscription.model.Pays;
import cm.deepdream.academia.souscription.model.Region;
import cm.deepdream.academia.souscription.repository.RegionRepository;
import cm.deepdream.academia.souscription.transfert.RegionDTO;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.* ;

import java.util.List;
import java.util.Optional;
import java.util.Random ;
@RunWith(SpringRunner.class)
public class RegionServiceUnitTest {
	@MockBean
	private RegionRepository regionRepository  ;
	
	
	@Test
	public void tester_creerRegion_avecDonneesValides() {
		final String code = "NO" ;
		final String libelle = "NORD" ;
		final Long idPays = 1000000000L ;
		final String codePays = "CM" ;


		Region region_Entry = Region.builder().code(code).libelle(libelle)
				.pays(Pays.builder().id(idPays).code(codePays).build()).build() ;
		
		Region region_Return = Region.builder().id(new Random().nextLong()).code(code).libelle(libelle)
				.pays(Pays.builder().id(idPays).code(codePays).build()).build() ;
		
		RegionDTO regionDTO_Entry = RegionDTO.builder().code(code).libelle(libelle).idPays(idPays)
									   .codePays(codePays).build() ;
				                       
		when(regionRepository.save(region_Entry)).thenReturn(region_Return) ;
		
		RegionService regionService = new RegionService(regionRepository) ;
		
		RegionDTO regionDTO_Return = regionService.creer(regionDTO_Entry) ;
		
		Assert.assertTrue(regionDTO_Return.getId() != null
				&& code.equals(regionDTO_Return.getCode())
				&& libelle.equals(regionDTO_Return.getLibelle())
				&& idPays == regionDTO_Return.getIdPays()
				&& codePays.equals(regionDTO_Return.getCodePays())) ;
	}
	
	
	
	@Test
	public void tester_modifierRegion_avecDonneesValides() {
		final Long id = 1000000000L ;
		final String code = "ES" ;
		final String libelle = "EST" ;
		final Long idPays = 1000000000L ;
		final String codePays = "CM" ;

		Region region_Entry = Region.builder().id(id).code(code).libelle(libelle)
				.pays(Pays.builder().id(idPays).code(codePays).build()).build() ;
		
		Region region_Return = Region.builder().id(id).code(code).libelle(libelle)
				.pays(Pays.builder().id(idPays).code(codePays).build()).build() ;
		
		RegionDTO regionDTO_Entry = RegionDTO.builder().id(id).code(code).libelle(libelle).idPays(idPays)
									   .codePays(codePays).build() ;
				                       
		when(regionRepository.save(region_Entry)).thenReturn(region_Return) ;
		when(regionRepository.findById(id)).thenReturn(Optional.of(region_Return)) ;
		
		RegionService regionService = new RegionService(regionRepository) ;
		
		RegionDTO regionDTO_Return = regionService.modifier(regionDTO_Entry) ;
		
		Assert.assertTrue(regionDTO_Return.getId() != null
				&& code.equals(regionDTO_Return.getCode())
				&& libelle.equals(regionDTO_Return.getLibelle())
				&& idPays == regionDTO_Return.getIdPays()
				&& codePays.equals(regionDTO_Return.getCodePays())) ;
	}
	
	
	@Test
	public void tester_supprimerRegion_avecDonneesValides() {
		final Long id = 1000000000L ;
		final String code = "CE" ;
		final String libelle = "CENTRE" ;
		final Long idPays = 1000000000L ;
		final String codePays = "CM" ;

		RegionDTO regionDTO_Entry = RegionDTO.builder().id(id).code(code).libelle(libelle).idPays(idPays)
				   .codePays(codePays).build() ;

		Region region = Region.builder().id(id).code(code).libelle(libelle)
				.pays(Pays.builder().id(idPays).build()).build() ;
		
		when(regionRepository.findById(id)).thenReturn(Optional.of(region)) ;
		doNothing().when(regionRepository).delete(region) ;
		
		RegionService regionService = new RegionService(regionRepository) ;
		
		try {
			regionService.supprimer(regionDTO_Entry) ;
			Assert.assertTrue(true) ;
		}catch(Exception e) {
			Assert.fail() ;
		}
	}
	
	
	@Test
	public void tester_rechercherRegion_avecDonneesValides() {
		final Long id = 1000000000L ;
		final String code = "CE" ;
		final String libelle = "CENTRE" ;
		final Long idPays = 1000000000L ;
		final String codePays = "CM" ;

		Region regionReturn = Region.builder().id(id).code(code).libelle(libelle)
				.pays(Pays.builder().id(idPays).code(codePays).build()).build() ;
		
		when(regionRepository.findById(id)).thenReturn(Optional.of(regionReturn)) ;
		
		RegionService regionService = new RegionService(regionRepository) ;
		
		RegionDTO regionDTO_Return = regionService.rechercher(id) ;
		
		Assert.assertTrue(id == regionDTO_Return.getId()
				&& code.equals(regionDTO_Return.getCode())
				&& libelle.equals(regionDTO_Return.getLibelle())
				&& idPays == regionDTO_Return.getIdPays()
				&& codePays.equals(regionDTO_Return.getCodePays())) ;
	}
	
	
	
	@Test
	public void tester_rechercherPlusieursRegions_avecDonneesValides() {
		Region region_Return_1 = Region.builder().id(100000L).code("CE").libelle("CENTRE")
				.pays(Pays.builder().id(100000L).code("CM").build()).build() ;
		
		Region region_Return_2 = Region.builder().id(100001L).code("NO").libelle("NORD")
				.pays(Pays.builder().id(100000L).code("CM").build()).build() ;

		when(regionRepository.findAll()).thenReturn(List.of(region_Return_1, region_Return_2)) ;
		
		RegionService regionService = new RegionService(regionRepository) ;
		List<RegionDTO> listeRegionsDTO = regionService.rechercherTout() ;
		
		Assert.assertThat(listeRegionsDTO, hasSize(2));
	}

}
