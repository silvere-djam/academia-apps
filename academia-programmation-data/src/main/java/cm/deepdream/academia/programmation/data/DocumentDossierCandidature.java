package cm.deepdream.academia.programmation.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DocumentDossierCandidature extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@NotNull
	@Column(name = "libelle")
	private String libelle ;
	
	@ManyToOne
	@JoinColumn (name = "id_candidature")
	private Candidature candidature ;
	
	@NotNull
	@Lob
	@Column(name = "contenu")
	private byte[] contenu ;
}
