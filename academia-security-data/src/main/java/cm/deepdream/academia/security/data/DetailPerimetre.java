package cm.deepdream.academia.security.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import cm.deepdream.academia.programmation.data.Classe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode (callSuper = false)
public class DetailPerimetre extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_utilisateur")
	private Utilisateur utilisateur ;
	
	@ManyToOne
	@JoinColumn (name = "id_classe")
	private Classe classe ;
}
