package cm.deepdream.academia.souscription.model;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Responsable implements Serializable{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id ;
	
	@NotBlank
	@Column (name = "matricule")
	private String matricule ;
	
	@NotBlank
	@Column (name = "nom")
	private String nom ;
	
	@Column (name = "prenom")
	private String prenom ;
	
	@Column (name = "sexe")
	private String sexe ;
	
	@Past
	@DateTimeFormat(pattern = "date_naissance")
	@Column (name = "date_naissance")
	private LocalDate dateNaissance ;
	
	@Column (name = "lieu_naissance")
	private String lieuNaissance ;
	
	@PastOrPresent
	@DateTimeFormat(pattern = "date_prise_service")
	@Column (name = "date_prise_service")
	private LocalDate datePriseService ;
	
	@NotBlank
	@Column(name = "telephone")
	private String telephone ;
	
	@Email
	@Column(name = "email")
	private String email ;
	
	@ManyToOne
	@JoinColumn(name = "id_etablissement")
	private Etablissement etablissement ;

}
