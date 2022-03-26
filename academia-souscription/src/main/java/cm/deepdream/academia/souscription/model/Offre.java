package cm.deepdream.academia.souscription.model;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

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
public class Offre implements Serializable{	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id ;
	//Academia Basic, Academia Medium, Academia Light, Academia First 
	@NotBlank
	@Column (name = "libelle")
	private String libelle ;
	
	@Positive
	@Column (name = "min_eleves")
	private Integer minEleves ;
	
	@Positive
	@Column (name = "max_eleves")
	private Integer maxEleves ;
	
	@Positive
	@Column (name = "max_utilisateurs")
	private Integer maxUtilisateurs ;
	
	@Positive
	@Column (name = "duree_essai")
	private Integer dureeEssai ;
	
	@Positive
	@Column (name = "montant_base")
	private BigDecimal montantBase ;
	
	@Positive
	@Column (name = "montant_millier")
	private BigDecimal montantMillier ;

	@Column (name = "description")
	private String description ;
}
