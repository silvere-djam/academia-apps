package cm.deepdream.academia.programmation.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class EmploiTemps extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_classe")
	private Classe classe ;
	
	@ManyToOne
	@JoinColumn (name = "id_annee_scolaire")
	private AnneeScolaire anneeScolaire ;
	
	@ManyToOne
	@JoinColumn (name = "id_enseignant_principal")
	private Enseignant enseignantPrincipal ;
	
	@ManyToOne
	@JoinColumn (name = "id_trimestre")
	private Trimestre trimestre ;
	
	@ManyToOne
	@JoinColumn (name = "id_semestre")
	private Semestre semestre ;

}
