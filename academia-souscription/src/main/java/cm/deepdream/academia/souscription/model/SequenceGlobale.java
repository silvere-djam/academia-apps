package cm.deepdream.academia.souscription.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class SequenceGlobale implements Serializable{
	@Id
	@Column(name = "id")
	private Long id ;
	
	@Column (name = "key")
	private  Long key ;
	
	@Column (name = "classname")
	private  String classname ;
	
}
