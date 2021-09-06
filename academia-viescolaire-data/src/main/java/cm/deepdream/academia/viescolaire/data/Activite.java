package cm.deepdream.academia.viescolaire.data;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Enseignant;
import cm.deepdream.academia.programmation.data.Semestre;
import cm.deepdream.academia.programmation.data.Trimestre;
import cm.deepdream.academia.programmation.data.UE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="discriminator", discriminatorType = DiscriminatorType.INTEGER)
public class Activite extends EntiteGenerique{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_ue")
	private UE ue ;
	
	@ManyToOne
	@JoinColumn (name = "id_classe")
	private Classe classe ;

	@ManyToOne
	@JoinColumn (name = "id_enseignant")
	private Enseignant enseignant ;
	
	@ManyToOne
	@JoinColumn (name = "id_annee_scolaire")
	private AnneeScolaire anneeScolaire ;

	@Column (name = "date_")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date ;
	
	@Column (name = "heure_debut")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime heureDebut ;
	
	@Column (name = "heure_fin")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime heureFin ;
	
	@Column (name = "presents")
	private Integer presents ;
	
	@Column (name = "absents")
	private Integer absents ;
	
	@Column (name = "statut")
	private String statut ;
	
	@Column (name = "type")
	private String type ;
	
	@ManyToOne
	@JoinColumn (name = "id_trimestre")
	private Trimestre trimestre ;
	
	@ManyToOne
	@JoinColumn (name = "id_semestre")
	private Semestre semestre ;
}
