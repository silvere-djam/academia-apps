package cm.deepdream.academia.souscription.data;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Abonnement extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column(name = "nb_eleves")
	private Integer nbEleves ;
	
	@Column (name = "date_debut")
	private LocalDate dateDebut ;
	
	@Column (name = "date_fin")
	private LocalDate dateFin ;
	
	@ManyToOne
	@JoinColumn(name = "id_etablissement")
	private Etablissement etablissement ;
	
	@Column (name = "duree")
	private Integer duree ;
	
	@Column (name = "statut")
	private String statut ;

	@Column (name = "evaluation")
	private Boolean evaluation ;
	
	@ManyToOne
	@JoinColumn (name = "id_offre")
	private Offre offre ;
	
	
}
