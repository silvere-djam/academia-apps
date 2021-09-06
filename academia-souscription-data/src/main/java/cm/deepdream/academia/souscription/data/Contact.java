package cm.deepdream.academia.souscription.data;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class Contact implements Serializable{
	@Column(name = "nom")
	private String nom ;
	
	@Column(name = "telephone")
	private String telephone ;
	
	@Column(name = "email")
	private String email ;
	
}
