package cm.deepdream.academia.programmation.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Niveau extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column (name = "abreviation")
	private String abreviation ;
	
	@Column (name = "libelle")
	private String libelle ;
	
	
}
