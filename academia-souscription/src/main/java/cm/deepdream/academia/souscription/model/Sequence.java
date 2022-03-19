package cm.deepdream.academia.souscription.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cm.deepdream.academia.souscription.model.Etablissement;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Sequence implements Serializable{
	@Id
	@Column(name = "id")
	private long id ;
	
	@Column (name = "key")
	private  Long key ;
	
	@Column (name = "classname")
	private  String classname ;
	
	@ManyToOne
	@JoinColumn (name = "id_etablissement")
	private  Etablissement etablissement ;

	
}
