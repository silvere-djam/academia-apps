package cm.deepdream.academia.programmation.data;
import java.time.LocalDate;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Enseignant extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column (name = "civilite")
	private String civilite ;
	
	@Column (name = "matricule")
	private String matricule ;
	
	@Column (name = "nom")
	private String nom ;
	
	@Column (name = "prenom")
	private String prenom ;
	
	@Column (name = "sexe")
	private String sexe ;
	
	@Column (name = "date_naissance")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateNaissance ;
	
	@Column (name = "lieu_naissance")
	private String lieuNaissance ;
	
	@Column (name = "date_prise_service")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate datePriseService ;
	
	@ManyToOne
	@JoinColumn (name = "id_domaine")
	private Domaine domaine ;
	
	@ManyToOne
	@JoinColumn(name = "id_fonction")
	private Fonction fonction ;
	
	@Column (name = "telephone")
	private String telephone ;
	
	@Column (name = "email")
	private String email ;

	@Column (name = "situation")
	private String situation ;
	
	@Embedded
	@AttributeOverrides({
		 @AttributeOverride( name = "path", column = @Column(name = "photo_path")),
		 @AttributeOverride( name = "subPath1", column = @Column(name = "photo_subpath_1")),
		 @AttributeOverride( name = "subPath2", column = @Column(name = "phto_subpath_2")),
		 @AttributeOverride( name = "fileName", column = @Column(name = "photo_filename")),
		 @AttributeOverride( name = "contentType", column = @Column(name = "photo_content_type")),
		 @AttributeOverride( name = "size", column = @Column(name = "photo_size"))
	})
	private Photo photo ;
}
