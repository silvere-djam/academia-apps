package cm.deepdream.academia.souscription.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Localite implements Serializable{
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id ;

	@NotBlank
	@Column (name = "libelle")
	private String libelle ;

	@ManyToOne
	@JoinColumn (name = "id_region")
	private Region region ;
}
