package cm.deepdream.academia.security.data;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode (callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Localisation extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_utilisateur")
	private Utilisateur utilisateur ;
	
	@Column(name = "date_acces")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateAcces ;
	
	@Column (name = "lattitude")
	private String lattitude ;
	
	@Column (name = "longitude")
	private String longitude ;
	
	@Column (name = "localite")
	private String localite ;
	
	@Column (name = "adresse_ip")
	private String adresseIP ;
}
