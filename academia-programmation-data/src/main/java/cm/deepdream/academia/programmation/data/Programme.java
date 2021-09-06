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
public class Programme extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_anneescolaire")
	private AnneeScolaire anneeScolaire ;
	
	@ManyToOne
	@JoinColumn (name = "id_ue")
	private UE ue ;
	
	@ManyToOne
	@JoinColumn (name = "id_classe")
	private Classe classe ;
	
	@Column (name = "duree_theorique")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime dureeTheorique ;
	
	@Column (name = "duree_effectuee")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime dureeEffectuee ;
	
	@Column (name = "taux_horaire")
	private Float tauxHoraire ;
	
	@Column (name = "taux_couverture")
	private Float tauxCouverture ;
	
}
