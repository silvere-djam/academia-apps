package cm.deepdream.academia.programmation.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Classe extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column (name = "abreviation")
	private String abreviation ;
	
	@Column (name = "libelle")
	private String libelle ;
	
	@ManyToOne
	@JoinColumn (name = "id_niveau")
	private Niveau niveau ;
	
	@ManyToOne
	@JoinColumn (name = "id_filiere")
	private Filiere filiere ;
	
	@Column(name = "effectif")
	private Integer effectif ;
	

}
