package cm.deepdream.academia.souscription.transfert;
import java.io.Serializable;

import java.math.BigDecimal;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OffreDTO implements Serializable{	
	private Long id ;

	private String libelle ;

	private Integer minEleves ;

	private Integer maxEleves ;

	private Integer maxUtilisateurs ;

	private Integer dureeEssai ;

	private BigDecimal montantBase ;

	private BigDecimal montantMillier ;

	private String description ;
}
