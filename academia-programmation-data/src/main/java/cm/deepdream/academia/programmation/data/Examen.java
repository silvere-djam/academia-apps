package cm.deepdream.academia.programmation.data;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Examen extends EntiteGenerique{
	@Id
	@Column (name = "id")
	private Long id ;
	
	@Column (name = "libelle")
	private String libelle ;
	
	@Column(name = "nbre_centres")
	private Long nbreCentres ;
	
	@Column(name = "nbre_candidats")
	private Long nbreCandidats ;
	
	@ManyToOne
	@JoinColumn (name = "id_type_examen")
	private TypeExamen typeExamen ;
	
	@Column(name = "date_debut")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateDebut ;
	
	@Column(name = "date_fin")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateFin ;

	@ManyToOne
	@JoinColumn (name = "id_trimestre")
	private Trimestre trimestre ;
	
	@ManyToOne
	@JoinColumn (name = "id_semestre")
	private Semestre semestre ;
	
	@ManyToOne
	@JoinColumn (name = "id_annee_scolaire")
	private AnneeScolaire anneeScolaire ;
	
}
