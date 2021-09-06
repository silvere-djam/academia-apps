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
import cm.deepdream.academia.programmation.data.Chapitre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ContenuCours extends EntiteGenerique{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_cours")
	private Cours cours ;
	
	@Column (name = "date_cours")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateCours ;
	
	@Column (name = "duree")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime duree ;
	
	@ManyToOne
	@JoinColumn (name = "id_chapitre")
	private Chapitre chapitre ;

}
