package cm.deepdream.academia.souscription.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Pays implements Serializable{
	@Id
	@Column(name = "id")
	private Long id ;

	@Column(name = "code")
	private String code ;
	
	@Column(name = "libelle")
	private String libelle ;
	
	@Column(name = "code_tel")
	private String codeTel ;
	
	@Column(name = "monnaie")
	private String monnaie ;
	
	
}
