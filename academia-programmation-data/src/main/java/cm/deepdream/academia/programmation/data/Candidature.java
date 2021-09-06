package cm.deepdream.academia.programmation.data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Candidature extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column (name = "nom")
	private String nom ;
	
	@Column (name = "prenom")
	private String prenom ;
	
	@Column (name = "date_depot")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime dateDepot ;
	
	@Column (name = "date_naissance")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateNaissance ;
	
	@Column (name = "lieu_naissance")
	private String lieuNaissance ;
	
	@Column(name = "ancien_etablissement")
	private String ancienEtablissement ;
	
	@Column(name = "ancienne_classe")
	private String ancienneClasse ;
	
	@Column(name = "dernier_diplome")
	private String dernierDiplome ;
	
	@ManyToOne
	@JoinColumn (name = "id_niveau")
	private Niveau idNiveau ;
	
	@ManyToOne
	@JoinColumn (name = "id_filiere")
	private Filiere filiere ;
	
	@ManyToOne
	@JoinColumn (name = "id_anneescolaire")
	private AnneeScolaire anneeScolaire ;
	
	@Column (name = "telephone")
	private String telephone ;
	
	@Column (name = "email")
	private String email ;
	
	@Column (name = "motif")
	private String motif ;
	
	@Lob
	@Column(name = "photo")
	private String photo ;
}
