package cm.deepdream.academia.souscription.data;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Localite implements Serializable{
	@Id
	@Column(name = "id")
	private Long id ;

	@Column (name = "libelle")
	private String libelle ;

	@ManyToOne
	@JoinColumn (name = "id_pays")
	private Pays pays ;
}
