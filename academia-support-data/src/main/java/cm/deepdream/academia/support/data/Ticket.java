package cm.deepdream.academia.support.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import cm.deepdream.academia.security.data.Utilisateur;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode (callSuper = false)
public class Ticket extends EntiteGenerique{
	@ManyToOne
	@JoinColumn(name = "id_emetteur")
	private Utilisateur emeteur ;
	
	@ManyToOne
	@JoinColumn (name = "id_categorie")
	private Categorie categorie ;
	
	@Column (name = "description")
	private String description ;
	
	@Column (name = "statut")
	private String statut ;
	
}
