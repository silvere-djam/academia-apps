package cm.deepdream.academia.souscription.service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cm.deepdream.academia.souscription.exceptions.RegionDuplicationException;
import cm.deepdream.academia.souscription.exceptions.RegionNotFoundException;
import cm.deepdream.academia.souscription.model.Pays;
import cm.deepdream.academia.souscription.model.Region;
import cm.deepdream.academia.souscription.repository.RegionRepository;
import cm.deepdream.academia.souscription.transfert.RegionDTO;
import static org.mockito.Mockito.* ;
import java.util.List;
import java.util.Optional;
import java.util.Random ;

@ExtendWith(MockitoExtension.class)
public class RegionServiceUnitTest {
	@Mock
	private RegionRepository regionRepository  ;
	
	@InjectMocks
	private RegionService regionService ;
	
	
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
		
		when(regionRepository.existsByLibelle(libelle)).thenReturn(false) ;
		when(regionRepository.save(region_Entry)).thenReturn(region_Return) ;

		RegionDTO regionDTO_Return = regionService.creer(regionDTO_Entry) ;
		
		Assertions.assertNotNull(regionDTO_Return.getId()) ;
		
		Assertions.assertTrue(code.equals(regionDTO_Return.getCode())
				&& libelle.equals(regionDTO_Return.getLibelle())
				&& idPays.equals(regionDTO_Return.getIdPays())
				&& codePays.equals(regionDTO_Return.getCodePays())) ;
		
		verify(regionRepository, times(1)).existsByLibelle(libelle) ;
		verify(regionRepository, times(1)).save(region_Entry) ;
	}
	
	
	
	@Test
	public void tester_creerRegion_avecDonneeInexistante() {
		final String code = "NO" ;
		final String libelle = "NORD" ;
		final Long idPays = 1000000000L ;
		final String codePays = "CM" ;

		RegionDTO regionDTO_Entry = RegionDTO.builder().code(code).libelle(libelle).idPays(idPays)
									         .codePays(codePays).build() ;
		
		when(regionRepository.existsByLibelle(libelle)).thenReturn(true) ;

		Assertions.assertThrows(RegionDuplicationException.class, 
				() -> regionService.creer(regionDTO_Entry)) ;
		
		verify(regionRepository, times(1)).existsByLibelle(libelle) ;
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

		RegionDTO regionDTO_Return = regionService.modifier(regionDTO_Entry) ;
		
		Assertions.assertTrue(id.equals(regionDTO_Return.getId())
				&& code.equals(regionDTO_Return.getCode())
				&& libelle.equals(regionDTO_Return.getLibelle())
				&& idPays.equals(regionDTO_Return.getIdPays())
				&& codePays.equals(regionDTO_Return.getCodePays())) ;
		
		verify(regionRepository, times(1)).findById(id) ;
		verify(regionRepository, times(1)).save(region_Entry) ;
	}
	
	
	
	@Test
	public void tester_modifierRegion_avecDonnnesInexistantes() {
		final Long id = 1000000000L ;
		final String code = "ES" ;
		final String libelle = "EST" ;
		final Long idPays = 1000000000L ;
		final String codePays = "CM" ;

		RegionDTO regionDTO_Entry = RegionDTO.builder().id(id).code(code).libelle(libelle).idPays(idPays)
									   .codePays(codePays).build() ;
				                       
		when(regionRepository.findById(id)).thenReturn(Optional.empty()) ;

		Assertions.assertThrows(RegionNotFoundException.class, 
				() -> regionService.modifier(regionDTO_Entry)) ;
		
		verify(regionRepository, times(1)).findById(id) ;
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

		Assertions.assertDoesNotThrow(() -> regionService.supprimer(regionDTO_Entry) ) ;
		
		verify(regionRepository, times(1)).findById(id) ;
	}
	
	
	
	@Test
	public void tester_supprimerRegion_avecDonneesInexistantes() {
		final Long id = 1000000000L ;
		final String code = "CE" ;
		final String libelle = "CENTRE" ;
		final Long idPays = 1000000000L ;
		final String codePays = "CM" ;

		RegionDTO regionDTO_Entry = RegionDTO.builder().id(id).code(code).libelle(libelle).idPays(idPays)
				   .codePays(codePays).build() ;

		when(regionRepository.findById(id)).thenReturn(Optional.empty()) ;

		Assertions.assertThrows(RegionNotFoundException.class, () -> regionService.supprimer(regionDTO_Entry)) ;
		
		verify(regionRepository, times(1)).findById(id) ;
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

		RegionDTO regionDTO_Return = regionService.rechercher(id) ;
		
		Assertions.assertTrue(id.equals(regionDTO_Return.getId()) 
				&& code.equals(regionDTO_Return.getCode())
				&& libelle.equals(regionDTO_Return.getLibelle())
				&& idPays.equals(regionDTO_Return.getIdPays())
				&& codePays.equals(regionDTO_Return.getCodePays())) ;
		
		verify(regionRepository, times(1)).findById(id) ;
	}
	
	
	
	@Test
	public void tester_rechercherRegion_avecDonneesInexistantes() {
		final Long id = 1000000000L ;

		when(regionRepository.findById(id)).thenReturn(Optional.empty()) ;

		Assertions.assertThrows(RegionNotFoundException.class, () -> regionService.rechercher(id)) ;
		
		verify(regionRepository, times(1)).findById(id) ;
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
		
		Assertions.assertTrue(listeRegionsDTO.size() >= 2) ;
	}

}
