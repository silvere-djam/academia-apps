package cm.deepdream.academia.viescolaire.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import cm.deepdream.academia.programmation.data.Eleve;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Note extends EntiteGenerique{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_eleve")
	private Eleve eleve ;
	
	@ManyToOne
	@JoinColumn (name = "id_evaluation")
	private Evaluation evaluation ;
	
	@Column (name = "valeur")
	private Float valeur ;
	
	@Column (name = "coefficient")
	private Float coefficient ;
	
	@Column (name = "valeur_coefficiee")
	private Float valeurCoefficiee ;
	
	@Column (name = "appreciation")
	private String appreciation ;
	

}
