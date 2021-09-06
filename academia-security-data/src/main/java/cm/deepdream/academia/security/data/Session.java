package cm.deepdream.academia.security.data;
import java.time.LocalDateTime;
import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Session  extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn(name = "id_utilisateur")
	private Utilisateur utilisateur ;
	
	@Column (name = "date_debut")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateDebut ;
	
	@Column (name = "adresse_ip")
	private String adresseIP ;

	@Column (name = "date_fin")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateFin ;
	
	@ManyToOne
	@JoinColumn (name = "id_equipement")
	private Equipement equipement ;
	
	@ManyToOne
	@JoinColumn (name = "id_localisation")
	private Localisation localisation ;

}
