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
public class Indicateur extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_discipline")
	private UE discipline ;
	
	@ManyToOne
	@JoinColumn (name = "id_classe")
	private Classe classe ;
	
	@ManyToOne
	@JoinColumn (name = "id_trimestre")
	private Trimestre trimestre ;
	
	@ManyToOne
	@JoinColumn (name = "id_enseignant")
	private Enseignant enseignant ;
	
	@ManyToOne
	@JoinColumn (name = "id_anneescolaire")
	private AnneeScolaire anneeScolaire ;
	
	@Column (name = "taux_couv_heures")
	private Float tauxCouvHeures ;
	
	@Column (name = "taux_couv_programme")
	private Float tauxCouvProgramme ;
	
	@Column (name = "taux_assiduite")
	private Float tauxAssiduite ;
	
	@Column (name = "taux_execution_tp")
	private Float tauxExecutionTp ;
	
	@Column (name = "taux_reussite")
	private Float tauxReussite ;
	
	@Column (name = "moyenne_generale")
	private Float moyenneGenerale ;
	
	@Column (name = "date_conseil")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateConseil ;

}
