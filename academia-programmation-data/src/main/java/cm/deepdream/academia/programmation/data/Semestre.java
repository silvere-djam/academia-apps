package cm.deepdream.academia.programmation.data;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Semestre extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column(name = "libelle")
	private String libelle ;
	
	@Column(name = "numero")
	private String numero ;
	
	@Column (name = "date_debut")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateDebut ;
	
	@Column (name = "date_fin")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateFin ;
	
	@ManyToOne
	@JoinColumn (name = "id_anneescolaire")
	private AnneeScolaire anneeScolaire ;

	@Column(name = "courant")
	private Integer courant ;
}
