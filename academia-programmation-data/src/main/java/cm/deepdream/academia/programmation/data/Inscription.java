package cm.deepdream.academia.programmation.data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Inscription extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_eleve")
	private Eleve eleve ;
	
	@ManyToOne
	@JoinColumn (name = "id_classe")
	private Classe classe ;
	
	@ManyToOne
	@JoinColumn (name = "id_anneescolaire")
	private AnneeScolaire anneeScolaire ;
	
	@Column (name = "date_inscription")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateInscription ;
	
	/**
	 * Nombre d'années passées à ce niveau
	 */
	@Column (name = "statut")
	private String statut ;


	
}
