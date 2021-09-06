package cm.deepdream.academia.viescolaire.data;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
@DiscriminatorValue("1")
public class Cours extends Activite{
	@Column (name = "type_cours")
	private String typeCours ;
	
}
