package cm.deepdream.academia.support.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
@AllArgsConstructor
public class Categorie extends EntiteGenerique{
	@NotNull
	@Column (name = "libelle")
	private String libelle ;
	
}
