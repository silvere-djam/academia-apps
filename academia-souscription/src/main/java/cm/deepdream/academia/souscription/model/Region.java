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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Region implements Serializable{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column (name = "libelle")
	private String libelle ;
	
	@ManyToOne
	@JoinColumn(name = "id_pays")
	private Pays pays ;
}
