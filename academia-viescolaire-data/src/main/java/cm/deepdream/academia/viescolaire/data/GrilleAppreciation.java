package cm.deepdream.academia.viescolaire.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
@Entity
@Data
@AllArgsConstructor
public class GrilleAppreciation extends EntiteGenerique{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id ;
	
	@Column(name = "note_min")
	private Float noteMin ;
	
	@Column(name = "note_max")
	private Float noteMax ;
	
	@Column(name = "appreciation")
	private String appreciation ;
	
	@Column(name = "valide")
	private String valide ;
}
