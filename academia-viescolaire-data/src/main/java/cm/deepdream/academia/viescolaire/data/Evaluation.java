package cm.deepdream.academia.viescolaire.data;
import javax.persistence.Column;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import cm.deepdream.academia.programmation.data.Examen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("2")
public class Evaluation extends Activite{
	@Column (name = "taux_reussite")
	private Float tauxReussite ;
	
	@Column (name = "moyenne_generale")
	private Float moyenneGenerale ;

	@ManyToOne
	@JoinColumn(name = "id_examen")
	private Examen examen ;

}
