package cm.deepdream.academia.programmation.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalleExamen extends EntiteGenerique{
	@Id
	@Column (name = "id")
	private Long id ;
	
	@Column(name = "numero")
	private String numero ;
	
	@Column(name = "libelle")
	private String libelle ;
	
	@Column(name = "nb_candidats")
	private Long nbCandidats ;
	
	@ManyToOne
	@JoinColumn (name = "id_centre_examen")
	private CentreExamen centreExamen ;
	
	@ManyToOne
	@JoinColumn (name = "id_examen")
	private Examen examen ;
}
