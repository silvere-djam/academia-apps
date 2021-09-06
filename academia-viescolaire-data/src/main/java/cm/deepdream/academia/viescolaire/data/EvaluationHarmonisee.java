package cm.deepdream.academia.viescolaire.data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.format.annotation.DateTimeFormat;
import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.Domaine;
import cm.deepdream.academia.programmation.data.GroupeClasse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationHarmonisee extends EntiteGenerique{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id ;
	
	@OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Classe> listeClasses = new ArrayList<Classe>() ;
	
	@ManyToOne
	@JoinColumn(name = "id_domaine")
	private Domaine domaine ;
	
	@ManyToOne
	@JoinColumn(name = "id_groupe_classe")
	private GroupeClasse groupeClasse ;
	
	@Column (name = "date_")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date ;
	
	@Column (name = "heure_debut")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime heureDebut ;
	
	@Column (name = "heure_fin")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime heureFin ;
	
	@Column (name = "taux_reussite")
	private Float tauxReussite ;
	
	@Column (name = "moyenne_generale")
	private Float moyenneGenerale ;
	
	@Column (name = "statut")
	private String statut ;

}
