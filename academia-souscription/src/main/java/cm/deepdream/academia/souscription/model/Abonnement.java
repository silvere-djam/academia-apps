package cm.deepdream.academia.souscription.model;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
	@GeneratedValue
	@Column(name="id")
	private Long id ;
	
	@Positive
	@Column(name = "nb_eleves")
	private Integer nbEleves ;
	
	@FutureOrPresent
	@NotNull
	@Column (name = "date_debut")
	private LocalDate dateDebut ;
	
	@Future
	@NotNull
	@Column (name = "date_fin")
	private LocalDate dateFin ;
	
	@ManyToOne
	@JoinColumn(name = "id_etablissement")
	private Etablissement etablissement ;
	
	@Positive
	@Column (name = "duree")
	private Integer duree ;
	
	@NotNull
	@Column (name = "statut")
	private String statut ;

	@Column (name = "evaluation")
	private Boolean evaluation ;
	
	@ManyToOne
	@JoinColumn (name = "id_offre")
	private Offre offre ;
	
	
}
