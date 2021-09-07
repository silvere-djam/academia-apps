package cm.deepdream.academia.security.data;
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
@EqualsAndHashCode (callSuper = false)
public class Action extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column (name = "code")
	private String code ;
	
	@Column (name = "libelle")
	private String libelle ;
	
	@Column (name = "description")
	private String description ;
}
