package cm.deepdream.academia.programmation.data;
import java.time.LocalDate;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import cm.deepdream.academia.souscription.data.Pays;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Eleve extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@NotNull
	@Column (name = "matricule")
	private String matricule ;
	
	@NotNull
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
	
	@ManyToOne
	@JoinColumn (name = "id_classe")
	private Classe classe ;
	
	@Column (name = "date_admission")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateAdmission ;
	
	@ManyToOne
	@JoinColumn (name = "id_pays")
	private Pays pays ;
	
	@Column (name = "statut")
	private String statut ;
	
	@Column (name = "nom_parent")
	private String nomParent ;
	
	@Column (name = "adresse_parent")
	private String adresseParent ;
	
	@Column (name = "adresse")
	private String adresse ;
	
	@Column (name = "telephone")
	private String telephone ;
	
	@Column (name = "email")
	private String email ;
	
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
