package cm.deepdream.academia.souscription.transfert;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ResponsableDTO implements Serializable{
	private Long id ;

	private String matricule ;

	private String nom ;

	private String prenom ;
	
	private String sexe ;

	private LocalDate dateNaissance ;

	private String lieuNaissance ;

	private LocalDate datePriseService ;

	private String telephone ;

	private String email ;

}
