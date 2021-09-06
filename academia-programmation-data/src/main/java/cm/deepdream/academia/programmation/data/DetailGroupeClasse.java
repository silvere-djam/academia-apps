package cm.deepdream.academia.programmation.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DetailGroupeClasse extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@JoinColumn (name = "id_classe")
	private Classe classe ;
	
	@JoinColumn (name = "id_groupe_classe")
	private GroupeClasse groupeClasse ;
}
