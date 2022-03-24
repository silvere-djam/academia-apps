package cm.deepdream.academia.souscription.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Etablissement implements Serializable{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id ;
	
	@Column (name = "libelle")
	private String libelle ;
	
	@Column (name = "telephone")
	private String telephone ;
	
	@Column (name = "email")
	private String email ;
	
	@Column (name = "boite_postale")
	private String boitePostale ;
	
	@Column (name = "cycle")
	private String cycle ;
	
	@Column (name = "nb_eleves_approx")
	private Integer nbElevesApprox ;
	
	@ManyToOne
	@JoinColumn (name = "id_localite")
	private Localite localite ;

	
}
