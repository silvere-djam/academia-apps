package cm.deepdream.academia.programmation.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import cm.deepdream.academia.souscription.data.Departement;
import cm.deepdream.academia.souscription.data.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CentreExamen extends EntiteGenerique{
	@Id
	@Column (name = "id")
	private Long id ;
	
	@Column(name = "numero")
	private String numero ;
	
	@Column(name = "libelle")
	private String libelle ;
	
	@ManyToOne
	@JoinColumn (name = "id_examen")
	private Examen examen ;
	
	@Column(name = "nbre_salles")
	private Long nbreSalles ;
	
	@Column(name = "nbre_candidats")
	private Long nbreCandidats ;
	
	@ManyToOne
	@JoinColumn (name = "id_annee_scolaire")
	private AnneeScolaire anneeScolaire ;
	
	@ManyToOne
	@JoinColumn (name = "id_departement")
	private Departement departement ;
	
	@ManyToOne
	@JoinColumn (name = "id_region")
	private Region region ;
}
