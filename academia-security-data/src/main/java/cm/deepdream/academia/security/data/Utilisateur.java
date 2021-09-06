package cm.deepdream.academia.security.data;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur  extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column(name = "mot_de_passe")
	private String motDePasse ;
	
	@Column(name = "id_personne")
	private Long idPersonne ;
	
	@Column(name = "nom")
	private String nom ;
	
	@Column(name = "pseudonyme")
	private String pseudonyme ;
	
	@Column(name = "date_dern_conn")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateDernConn ;
	
	@Column(name = "date_exp")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateExp ;
	
	@Column(name = "date_exp_mdp")
	@DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateExpMdp ;
	
	@Column(name = "nbre_tentatives")
	private Integer nbreTentatives ;
	
	@Column(name = "telephone")
	private String telephone ;
	
	@Column(name = "email", unique = true)
	private String email ;
	
	@Column (name = "statut")
	private  String statut ;
	
	@Column(name = "id_role")
	private Integer idRole ;
	
	@Column(name = "libelle_role")
	private String libelleRole ;
	
	@Column(name = "code_activation")
	private String codeActivation ;
	
	@Transient
	private String mdp1 ;
	
	@Transient
	private String mdp2 ;
	
	@Transient
	private String mdp3 ;


}
