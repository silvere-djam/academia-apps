package cm.deepdream.academia.souscription.data;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Offre implements Serializable{	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id ;
	//Academia Basic, Academia Medium, Academia Light, Academia First 
	@Column (name = "libelle")
	private String libelle ;
	
	@Column (name = "min_eleves")
	private Integer minEleves ;
	
	@Column (name = "max_eleves")
	private Integer maxEleves ;
	
	@Column (name = "max_utilisateurs")
	private Integer maxUtilisateurs ;
	
	@Column (name = "duree_essai")
	private Integer dureeEssai ;
	
	@Column (name = "montant_base")
	private BigDecimal montantBase ;
	
	@Column (name = "montant_millier")
	private BigDecimal montantMillier ;

	@Column (name = "description")
	private String description ;
}
