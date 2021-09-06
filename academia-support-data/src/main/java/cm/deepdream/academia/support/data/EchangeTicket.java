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
@EqualsAndHashCode(callSuper = false)
public class EchangeTicket extends EntiteGenerique{
	@ManyToOne
	@JoinColumn (name = "id_ticket")
	private Ticket ticket ;
	
	@Column (name = "index")
	private Integer index ;
	
	@Column (name = "message")
	private String message ;
	
	@ManyToOne
	@JoinColumn(name = "id_utilisateur")
	private Utilisateur utilisateur ;
}
