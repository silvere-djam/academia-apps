package cm.deepdream.academia.viescolaire.data;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.Trimestre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Absence extends EntiteGenerique{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_eleve")
	private Eleve eleve ;
	
	@ManyToOne
	@JoinColumn (name = "id_cours")
	private Cours cours ;
	
	@ManyToOne
	@JoinColumn (name = "id_evaluation")
	private Evaluation evaluation ;
	
	@Column (name = "date_absence")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateAbsence ;
	
	@Column (name = "duree")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime duree ;
	

	

}
