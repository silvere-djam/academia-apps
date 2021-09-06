package cm.deepdream.academia.programmation.data;
import java.time.LocalTime;
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
public class Chapitre extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column (name = "numero")
	private Integer numero ;
	
	@Column (name = "libelle")
	private String libelle ;
	
	@Column (name = "duree_theorique")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime dureeTheorique ;
	
	@Column (name = "duree_effectuee")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime dureeEffectuee ;
	
	@ManyToOne
	@JoinColumn (name = "id_programme")
	private Programme programme ;
	
	@ManyToOne
	@JoinColumn (name = "id_trimestre")
	private Trimestre trimestre ;
	
	@ManyToOne
	@JoinColumn (name = "id_semestre")
	private Semestre semestre ;

}
