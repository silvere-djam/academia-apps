package cm.deepdream.academia.programmation.data;
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
public class UE extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column (name = "code")
	private String code ;
	
	@ManyToOne
	@JoinColumn (name = "id_domaine")
	private Domaine domaine ;
	
	@ManyToOne
	@JoinColumn (name = "id_classe")
	private Classe classe ;
	
	@Column (name = "coefficient")
	private Float coefficient ;
	
	@Column (name = "numero")
	private Integer numero ;
	
	@Column (name = "credits")
	private Float credits ;
	
	@ManyToOne
	@JoinColumn (name = "id_groupe")
	private Groupe groupe ;
	
}
