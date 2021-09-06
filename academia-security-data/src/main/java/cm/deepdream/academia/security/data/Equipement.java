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
public class Equipement extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_utilisateur")
	private Utilisateur utilisateur ;
	
	@Column(name = "date_acces")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateAcces ;
	
	@Column (name = "adresse_mac")
	private String adresseMAC ;
	
	@Column (name = "marque")
	private String marque ;
	
	@Column (name = "modele")
	private String modele ;
	
	@Column (name = "systeme")
	private String systeme ;
	
	@Column (name = "numero")
	private String numero ;
}
