package cm.deepdream.academia.programmation.data;
import java.time.LocalTime;
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
public class DetailEmploiTemps extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_emploi_temps")
	private EmploiTemps emploiTemps ;
	
	@ManyToOne
	@JoinColumn (name = "id_ue")
	private UE ue ;
	
	@ManyToOne
	@JoinColumn (name = "id_enseignant")
	private Enseignant enseignant ;
	
	@Column (name = "jour_semaine")
	private Integer jourSemaine ;
	
	@Column (name = "libelle_jour")
	private String libelleJour ;
	
	@Column (name = "heure_debut")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime heureDebut ;
	
	@Column (name = "heure_fin")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime heureFin ;
	
	
}
