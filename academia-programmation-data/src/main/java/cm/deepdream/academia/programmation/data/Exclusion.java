package cm.deepdream.academia.programmation.data;
import java.time.LocalDate;
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
public class Exclusion extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_eleve")
	private Eleve eleve ;
	
	@Column (name = "date_exclusion")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateExclusion ;
	
	@Column (name = "date_naissance")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateNaissance ;
	
	@Column (name = "lieu_naissance")
	private String lieuNaissance ;
	
	@ManyToOne
	@JoinColumn (name = "id_niveau")
	private Niveau niveau ;
	
	@ManyToOne
	@JoinColumn (name = "id_filiere")
	private Filiere filiere ;
	
	@ManyToOne
	@JoinColumn (name = "id_classe")
	private Classe classe ;
	
	/**
	 * 1 : Nouveau
	 * 2 : Ancien
	 * **/
	@Column (name = "statut")
	private int statut ;
	
	@Column (name = "telephone")
	private String telephone ;
	
	@Column (name = "email")
	private String email ;

}
