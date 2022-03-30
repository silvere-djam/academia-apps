package cm.deepdream.academia.souscription.model;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class Abonnement implements Serializable{
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
	
	@Column(name = "date_creation")
	private LocalDateTime dateCreation ;
	
	@Column(name = "date_dern_maj")
	private LocalDateTime dateDernMaj ;
	
	@Column(name = "createur")
	private String createur ;
	
	@Column(name = "modificateur")
	private String modificateur ;
}
