package cm.deepdream.academia.programmation.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TypeExamen extends EntiteGenerique{
	@Id
	@Column (name = "id")
	private Long id ;
	
	@Column(name = "libelle")
	private String libelle ;
	
	@Column(name = "periodicite")
	private String periodicite ;

	@ManyToOne
	@JoinColumn (name = "id_niveau")
	private Niveau niveau ;
	
}
