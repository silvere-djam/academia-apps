package cm.deepdream.academia.programmation.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GroupeClasse extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column (name = "libelle")
	private String libelle ;
}
